FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . /app
RUN ./gradlew build
EXPOSE 5353
CMD ["java", "-jar", "build/libs/OnlineDeliveryService-0.0.1-SNAPSHOT.war"]