FROM gradle:8.10.2-jdk21-alpine as builder

COPY outbox/gradle /app/outbox/
COPY outbox/build.gradle /app/outbox/
COPY outbox/gradlew /app/outbox/
COPY outbox/src /app/outbox/src
COPY shared  /app/shared
COPY settings.gradle /app/

WORKDIR /app/outbox

RUN gradle build

RUN mkdir -p build/libs/dependency && (cd build/libs/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache ca-certificates java-cacerts \
    && ln -sf /etc/ssl/certs/java/cacerts $JAVA_HOME/lib/security/cacerts

ARG DEPENDENCY=/app/outbox/build/libs/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app

CMD ["java","-cp","app:app/lib/*","pl.lodz.uni.biobank.foam.outbox.OutboxApplication"]