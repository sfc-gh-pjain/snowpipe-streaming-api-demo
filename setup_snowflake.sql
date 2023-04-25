/*
-- SETUP SCRIPT TO CREATE TARGET TABLE FOR SNOWPIPE STREAMING API (JDK) DEMO
-- YOU CAN COPY PASTE THIS CODE IN SNOWFLAKE WORKSHEET OR USE SNOWSQL TOOL
 */

USE ROLE ACCOUNTADMIN;
CREATE DATABASE IF NOT EXISTS SNOWPIPE_STREAMING;
USE SCHEMA SNOWPIPE_STREAMING.PUBLIC;

create or replace table 
STREAMING_MYSQL_TO_SNOW as (
    SELECT * FROM SNOWFLAKE_SAMPLE_DATA.TPCDS_SF10TCL.STORE_SALES LIMIT 1
    );
select count(*) from streaming_mysql_to_snow;

