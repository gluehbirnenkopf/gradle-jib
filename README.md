# gradle-jib
This project integrates a sample springboot application with [Google jib](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin). 

## pre-requisites
Please make sure that you have a [dockerhub account](https://hub.docker.com/). This is required to have a target registry available, to publish the image which is built using jib.

To build a docker image without requiring a docker deamon, execute:
```bash
./gradlew jib
```

This will use `gradle.properties` to parameterize the JIB configuration inside `build.gradle`.
As there are sensitive information like username + password, please configure them in your local user `~/.gradle/gradle.properties`.

All other actions can be taken from the official [Gradle JIB docs](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin).

## optional Continuous Integration with Jenkins
This repository also includes a Jenkinsfile which can be executed on a Jenkins@Kubernetes.
[Jenkins-helm](https://github.com/gluehbirnenkopf/jenkins-helm) is prepared for this and can be setup with a one-liner.
