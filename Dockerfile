
# Build
FROM maven:3.9.6-eclipse-temurin-8 AS build
WORKDIR /app
COPY . ./
RUN --mount=type=cache,target=/root/.m2 \
    mvn -Dmaven.repo.local=/root/.m2/repository clean package -DskipTests

# Default

FROM eclipse-temurin:8-jre-alpine
WORKDIR /app

RUN apk --no-cache add curl && \
    adduser -D user

COPY --from=build /app/target/*.jar ./app.jar

USER user
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]


