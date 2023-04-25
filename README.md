# snowpipe-streaming-api-demo

## Prerequisites:

1. You can deploy this in your own docker client. We will not cover how to install docker, but its pretty simple, you can check those at https://www.docker.com/get-started/

2. Some familiarity with java programming will help to understand the demo better


## How to run this demo

1.	Clone or fork this repo, download to local or where you desire to run this demo

2.	We are using key pair authentication to communicate with Snowflake (default). You can setup key-pair using this link https://docs.snowflake.com/en/user-guide/key-pair-auth#configuring-key-pair-authentication

3.	Update your account specific information in the *snowflake_account_config.properties* file. Paste your PEM private key in this file in a single line, do not include begin and end lines (from above step)

4.	Open the **setup_snowflake.sql** file in text editor to copy and paste its content into a Snowflake worksheet and click **Run All**. This will setup a database and target table. Change it to your specific role if needed.

5.	Go to **snowpipe-streaming-api-demo** (root folder) and run this command (for first time it will take a while)
    `docker compose up`

6.	Once the streaming load is completed you can verify your data in snowflake target table and bring the docker services down by running
    `docker compose down`
