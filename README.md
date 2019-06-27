# quarkus-reactive-pg [![Build Status](https://travis-ci.org/daggerok/quarkus-reactive-pg.svg?branch=master)](https://travis-ci.org/daggerok/quarkus-reactive-pg)
Quarkus micro-profile starter using Gradle / Maven build tools.

_getting started_

```bash
./mvnw docker:start

#./mvnw process-resources flyway:migrate
./mvnw compile quarkus:dev

./mvnw docker:stop
```

_maven dev mode_

```bash
./mvnw compile quarkus:dev
http :8080/api/v1/hello
```

_maven build_

```bash
./mvnw compile jar:jar quarkus:build
java -cp target/lib -jar target/*-runner.jar
http :8080/api/v1/hello/max
```

_fat jar_

```bash
./mvnw package -PuberJar
java -jar target/*-runner.jar
http :8080/api/v1/hello/max
```

_maven docker-compose plugin_

```bash
./mvnw -P docker-compose compile jar:jar quarkus:build docker-compose:up
#
./mvnw -P docker-compose docker-compose:down
```

_docker-compose_

```bash
./mvnw
docker-compose -f ./src/main/docker/docker-compose-maven.yaml up
# ...
docker-compose -f ./src/main/docker/docker-compose-maven.yaml down
```

_docker jvm_

```bash
./mvnw clean compile jar:jar quarkus:build
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/quarkus-example-jvm .
docker run -i --rm --name app -p 8080:8080 quarkus/quarkus-example-jvm &
#...
docker rm -f -v app
```

_docker native_

```bash
./mvnw package -Pnative -Dnative-image.docker-build=true
docker build -f src/main/docker/Dockerfile.native -t quarkus/quarkus-example-native .
docker run -i --rm --name app -p 8080:8080 quarkus/quarkus-example-native
# ...
docker rm -f -v app
```

_project sources archive_

```bash
./mvnw assemble:single
```

find archive with all project sources in target folder too: 

```bash
./mvnw assembly:single -Dassembly.ignoreMissingDescriptor
unzip -d target/sources target/*-sources.zip
unzip -d target/default target/*-src.zip
```

_maven archetype generator_

```bash
mvn io.quarkus:quarkus-maven-plugin:0.17.0:create \
  -DprojectGroupId=com.github.daggerok \
  -DprojectArtifactId=rest-api \
  -DprojectVersion=1.0-SNAPSHOT \
  -DclassName="com.github.daggerok.hello.RestResourcesource" \
  -Dpath="/"
```

NOTE: _This project has been based on [GitHub: daggerok/main-starter](https://github.com/daggerok/main-starter)_