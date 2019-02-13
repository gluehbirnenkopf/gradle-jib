// Pod Template
def cloud = env.CLOUD ?: "kubernetes"
def registryCredsID = "registry-credentials-id"
def serviceAccount = "jenkins"
def kubenamespace = "default"

podTemplate(label: 'ciPod', cloud: cloud, serviceAccount: serviceAccount, kubenamespace: kubenamespace, envVars: [
        envVar(key: 'NAMESPACE', value: kubenamespace)
    ],
    containers: [
        containerTemplate(name: 'java-builder', image: 'openjdk:8-jdk-slim', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'helm', image: 'lachlanevenson/k8s-helm:v2.12.3', ttyEnabled: true, command: 'cat')
  ]) {

    node('ciPod') {
        checkout scm
        env.BRANCH_NAME=env.BRANCH_NAME.toLowerCase()
        container('java-builder') {
            //withCredentials not working with jenkins lts
            stage('Compile') {
                sh """
                        chmod +x ./gradlew
                        ./gradlew assemble
                """
            }
            stage('Unit Test') {
                sh """
                        ./gradlew test
                """
            }
            stage('Build Image') {
                withCredentials([usernamePassword(credentialsId: 'registryCredentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh """
                        ./gradlew jib -PregistryUser=${USERNAME} -PregistryPassword=${PASSWORD}
                        """
                }
            }
        }
        container('helm') {
            stage('Deploy new Docker Image') {
                sh """
                        cd helm/
                        helm init --client-only
                        helm upgrade --install .
                """
            }
        }
    }
}
