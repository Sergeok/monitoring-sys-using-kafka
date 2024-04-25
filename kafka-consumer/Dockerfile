FROM openjdk:17-jdk-oracle
COPY ./target/kafka-consumer-1.0-SNAPSHOT.jar kafka-consumer-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "kafka-consumer-1.0-SNAPSHOT.jar"]