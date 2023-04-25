FROM maven:3.3-jdk-8 AS build
WORKDIR /usr/streaming-data-ingestion
COPY streaming-data-ingestion/src ./src
COPY streaming-data-ingestion/snowflake_account_config.properties .
COPY streaming-data-ingestion/pom.xml .
RUN mvn clean package

FROM openjdk:8-jdk-alpine
WORKDIR /streaming-data-ingestion
COPY --from=build /usr/streaming-data-ingestion/target/streaming-data-ingestion-1.0.jar ./streaming-data-ingestion-1.0.jar
COPY --from=build /usr/streaming-data-ingestion/snowflake_account_config.properties ./snowflake_account_config.properties
COPY streaming-data-ingestion/start.sh .
EXPOSE 8080
CMD ["/bin/sh", "start.sh"]
