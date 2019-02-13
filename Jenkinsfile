def appname = "test"

// Pod Template
def cloud = env.CLOUD ?: "kubernetes"
def registryCredsID = env.REGISTRY_CREDENTIALS ?: "registry-credentials-id"
def serviceAccount = env.SERVICE_ACCOUNT ?: "jenkins"

podTemplate(label: 'mypod', cloud: cloud, serviceAccount: serviceAccount, kubenamespace: kubenamespace, envVars: [
        envVar(key: 'NAMESPACE', value: kubenamespace),
        envVar(key: 'REGNAMESPACE', value: regnamespace),
        envVar(key: 'APPNAME', value: appname),
        envVar(key: 'REGISTRY', value: registry)
    ],
    containers: [
        containerTemplate(name: 'java-builder', image: 'openjdk:8-jre-alpine', ttyEnabled: true, command: 'cat')
  ]) {

    node('mypod') {
        checkout scm
        env.BRANCH_NAME=env.BRANCH_NAME.toLowerCase()
        container('java-builder') {
            //withCredentials not working with jenkins lts
            stage('Build Docker Image') {
                withCredentials([usernamePassword(credentialsId: registryCredsID, 
                                               usernameVariable: 'USERNAME', 
                                               passwordVariable: 'PASSWORD')]) {
                        sh """
                        #!/bin/bash
                        docker login -u token -p ${PASSWORD} ${env.REGISTRY}
                        
                        #Build Gradle Build Container Image from Dockerfile.build 
                        docker build . -t ${env.REGISTRY}/${env.REGNAMESPACE}/${env.APPNAME}:build-${env.BUILD_NUMBER} -f Dockerfile.build
                        docker container create --name ${env.APPNAME}-build-${env.BUILD_NUMBER} ${env.REGISTRY}/${env.REGNAMESPACE}/${env.APPNAME}:build-${env.BUILD_NUMBER}
                        
                        #Copy .jar file from Build Container and remove Build Container
                        mkdir -p ./target
                        docker container cp ${env.APPNAME}-build-${env.BUILD_NUMBER}:/usr/src/app/build/libs/TestService.jar ./target/app.jar
                        docker container rm -f ${env.APPNAME}-build-${env.BUILD_NUMBER}
                        
                        #Build Final Container Image from Dockerfile and delete build artifacts in workspace.
                        docker build . -t ${env.REGISTRY}/${env.REGNAMESPACE}/${env.APPNAME}:${env.BRANCH_NAME}
                        rm -rf target/ bin/ src/
                        """
                }
            }
            stage('Push Docker Image to Registry') {
                withCredentials([usernamePassword(credentialsId: registryCredsID, 
                                               usernameVariable: 'USERNAME', 
                                               passwordVariable: 'PASSWORD')]) {
                    sh """
                    #!/bin/bash
                    docker login -u token -p ${PASSWORD} ${env.REGISTRY}
                    docker push ${env.REGISTRY}/${env.REGNAMESPACE}/${env.APPNAME}:${env.BRANCH_NAME}
                    """
                    }
            }
        }
        container('helm') {
            stage('Deploy new Docker Image') {
                sh """
                cd helm/
                helm init --client-only
                #helm install --name=${env.APPNAME}-${env.BRANCH_NAME} --set name=${env.APPNAME}-${env.BRANCH_NAME} --set image=registry.eu-de.bluemix.net/blw-msa/${env.APPNAME}:${env.BRANCH_NAME} --set ingressSubdomain=helmtest . 
                helm upgrade --install ${env.APPNAME}-${env.BRANCH_NAME} --set appname=${env.APPNAME}-${env.BRANCH_NAME} --set image=registry.eu-de.bluemix.net/blw-msa/${env.APPNAME}:${env.BRANCH_NAME} --set ingressSubdomain=helmtest .
                """
            }
        }
    }
}
