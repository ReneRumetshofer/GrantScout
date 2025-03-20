#FROM eclipse-temurin:21-jdk as builder
#WORKDIR /app
#COPY gradlew build.gradle.kts settings.gradle.kts ./
#COPY src src
#COPY gradle gradle
#RUN chmod +x gradlew
#RUN ./gradlew bootJar

FROM eclipse-temurin:21-jre
WORKDIR /app
#COPY --from=builder /app/build/libs/*.jar app.jar
COPY build/libs/scraping-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
