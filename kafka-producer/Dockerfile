FROM openjdk:17-jdk-oracle
COPY ./target/kafka-producer-1.0-SNAPSHOT.jar kafka-producer-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "kafka-producer-1.0-SNAPSHOT.jar"]