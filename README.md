# gradle-jib
Repository which integrates spring boot hello world with [google jib](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin).

To build a docker image without requiring a docker deamon, execute:
```bash
./gradlew jib
```

This will use `gradle.properties` to parameterize the JIB configuration inside `build.gradle`.
As there are sensitive information like username + password, please configure them in your local user `~/.gradle/gradle.properties`.
