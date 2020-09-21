FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

COPY build/libs/sales-importer-*.jar /app/sales-importer.jar

EXPOSE 8080

CMD ["java", "-jar", "-Xmx512M", "-Xms512M", "sales-importer.jar"]
