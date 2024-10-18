FROM gradle:8.10.2-jdk21-alpine as builder

ARG FOAM_USER
ARG FOAM_TOKEN

ENV FOAM_USER ${FOAM_USER}
ENV FOAM_TOKEN ${FOAM_TOKEN}

COPY c4gh-file-service/gradle /app/c4gh-file-service/
COPY c4gh-file-service/build.gradle /app/c4gh-file-service/
COPY c4gh-file-service/gradlew /app/c4gh-file-service/
COPY c4gh-file-service/src /app/c4gh-file-service/src
COPY shared  /app/shared
COPY settings.gradle /app/

WORKDIR /app/c4gh-file-service

RUN gradle build

RUN mkdir -p build/libs/dependency && (cd build/libs/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache ca-certificates java-cacerts \
    && ln -sf /etc/ssl/certs/java/cacerts $JAVA_HOME/lib/security/cacerts

ARG DEPENDENCY=/app/c4gh-file-service/build/libs/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app

CMD ["java","-cp","app:app/lib/*","pl.lodz.uni.biobank.foam.c4ghfs.C4ghFsApp"]