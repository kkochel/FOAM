FROM gradle:8.10.2-jdk21-alpine as builder

COPY download-manager-app/gradle /app/download-manager-app/
COPY download-manager-app/build.gradle /app/download-manager-app/
COPY download-manager-app/gradlew /app/download-manager-app/
COPY download-manager-app/src /app/download-manager-app/src
COPY shared  /app/shared
COPY settings.gradle /app/

WORKDIR /app/download-manager-app

RUN gradle build -x test

RUN mkdir -p build/libs/dependency && (cd build/libs/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache ca-certificates java-cacerts \
    && ln -sf /etc/ssl/certs/java/cacerts $JAVA_HOME/lib/security/cacerts

ARG DEPENDENCY=/app/download-manager-app/build/libs/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app

CMD ["java","-cp","app:app/lib/*","pl.lodz.uni.biobank.foam.app.DownloadManagerApp"]