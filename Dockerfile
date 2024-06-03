FROM openjdk:17

WORKDIR /StudyMate2

COPY StudyMate-Apis/target/StudyMate-Apis-1.0.jar ./StudyMate-Apis-1.0.jar

EXPOSE 8080

CMD ["java", "-jar", "StudyMate-Apis-1.0.jar"]