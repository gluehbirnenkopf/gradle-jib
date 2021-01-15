
![Travis](https://api.travis-ci.org/gluehbirnenkopf/gradle-jib.svg?branch=master)

# Gradle-Jib
This project integrates a sample springboot application with [Google jib](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin) and demonstrates how images can be build manually as well as automatically.

## Local usage
Please make sure that you have a [dockerhub account](https://hub.docker.com/). This is required to publish images.
To build a docker image without requiring a docker deamon, locally execute:
```bash
./gradlew jib
```
This will use `gradle.properties` to parameterize the JIB configuration inside `build.gradle`.
As there are sensitive information like username + password, please configure them in your local user `~/.gradle/gradle.properties`.
All other actions can be taken from the official [Gradle JIB docs](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin).

```bash
registryUser=<gets populated by gradle.properties in the user home>
registryPassword=<gets populated by gradle.properties in the user home>
registryName=<your registryname (username) at dockerhub>
```

Alternatively, the credentials for the registry can be also passed via gradle project parameters
```bash
./gradlew jib -PregistryUser=<YourUser> -PregistryPassword=<YourPassword>
```

## Kubernetes Usage
To build & deploy this project automatically using Jenkins, run the Jenkinsfile.
[Jenkins-helm](https://github.com/gluehbirnenkopf/jenkins-helm) is prepared for this and can be setup on Kubernetes (Bluemix IKS 1.10).

Jenkins needs to have the docker credentials in place, in order to connect to your specific registry. Therefore please create an global credential called `registryCredentials` with username and password.

### kubectl setup (non-declarative)
```bash
# Create Config Map for application properties
kubectl create configmap helloworld --from-literal="message.text=Hello from Gluehbirnenkopf in the cloud"

#Create RBAC
kubectl create serviceaccount helloworld
kubectl create role spring-cloud-config --verb=get --verb=watch --verb=list --resource=configmaps --resource=pods --resource=secrets
kubectl create rolebinding helloworld --role=spring-cloud-config --serviceaccount=default:helloworld

#Create Deployment of your image
kubectl create deployment helloworld --image=gluehbirnenkopf/helloworld:1.1
kubectl patch deployment helloworld -p '{"spec": {"template": {"spec": {"serviceAccountName": "helloworld"}}}}'

#Expose the Service
kubectl expose deployment helloworld --port=8080
```
//dummy
