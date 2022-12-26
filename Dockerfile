FROM openjdk:8-jdk-alpine
EXPOSE 9090
ADD target/Bithdaybot.jar Bithdaybot.jar
ENTRYPOINT ["java", "-jar", "/Bithdaybot.jar"]