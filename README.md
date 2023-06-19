# snowpipe-streaming-api-demo

## Prerequisites:

1. You can deploy this in your own docker client. We will not cover how to install docker, but its very easy, you can check it at https://www.docker.com/get-started/

2. Some familiarity with java programming will help to understand the demo better

3. We are using community version of mysql server for demo purpose only, user and password are simple but in production you should use your own strong authentication guidelines


## How to run this demo

1.	Clone, fork or download this repo to local or where you desire to run this demo

2.	We are using key pair authentication to communicate with Snowflake (default). You can setup key-pair using this link [Snowflake key pair authentication setup](https://docs.snowflake.com/en/user-guide/key-pair-auth#configuring-key-pair-authentication)

3.	Update your account specific information in the **snowflake_account_config.properties** file. Paste your PEM private key in this file in a single line, do not include ""Begin---" and "End--" lines. We are not using passphrase for simplicity as this demo is about streaming not password hardiness.

4.	Open the **setup_snowflake.sql** file in text editor to copy and paste its content into a Snowflake worksheet and click **Run All**. This will setup a database and target table. Change it to your specific role if needed.

5.	Go to **snowpipe-streaming-api-demo** (root folder) and run this command. It will take a while to download all the packages for first time.
    `docker compose up`

6.	Once the streaming load is completed you can verify your data in snowflake target table and bring the docker services down by running
    `docker compose down`


## What you will learn

1. Snowflake ingest SDK has a streaming API that can be used to replicate or ingest delta records from your database or transaction system into Snowflake. 

2. Create custom Java application to build streaming pipeline into Snowflake

3. Create Streaming data ingestion without any message broker like Kafka

## What is not convered in this demo

1. Architecture for a highly available system
2. Fault taulerance with Streaming system
3. Error handling
