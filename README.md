# Gradle-Jib
This project integrates a sample springboot application with [Google jib](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin) and demonstrates how images can be build using jenkins or on localhost.

## Local usage
Please make sure that you have a [dockerhub account](https://hub.docker.com/). This is required to have a target registry available, to publish the image which is built using jib.

To build a docker image without requiring a docker deamon, locally execute:
```bash
./gradlew jib
```
This will use `gradle.properties` to parameterize the JIB configuration inside `build.gradle`.
As there are sensitive information like username + password, please configure them in your local user `~/.gradle/gradle.properties`.
All other actions can be taken from the official [Gradle JIB docs](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin).


## Kubernetes Usage
To build & deploy this project automatically using Jenkins, run the Jenkinsfile.
[Jenkins-helm](https://github.com/gluehbirnenkopf/jenkins-helm) is prepared for this and can be setup on Kubernetes (Bluemix IKS 1.10).
