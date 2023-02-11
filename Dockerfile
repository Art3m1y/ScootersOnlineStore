FROM openjdk:19
EXPOSE 8080
ADD backend/target/spring-boot-shop.jar spring-boot-shop.jar
ENTRYPOINT ["java", "-jar", "/spring-boot-shop.jar"]