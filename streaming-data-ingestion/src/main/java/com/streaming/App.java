package com.streaming;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.FileInputStream;
import java.io.InputStream;

import net.snowflake.ingest.streaming.InsertValidationResponse;
import net.snowflake.ingest.streaming.OpenChannelRequest;
import net.snowflake.ingest.streaming.SnowflakeStreamingIngestChannel;
import net.snowflake.ingest.streaming.SnowflakeStreamingIngestClient;
import net.snowflake.ingest.streaming.SnowflakeStreamingIngestClientFactory;


public class App {
    // Please follow the example in snowflake_account_config.properties to see the required properties, or
    // you will need the rsa private key to run this demo. You can setup key-pair using this link https://docs.snowflake.com/en/user-guide/key-pair-auth#configuring-key-pair-authentication
    // Paste your private key in the properties file in a single line, do not include begin and end lines
    
    final static String CONFIG_PATH = "snowflake_account_config.properties";
      
    public static void main(String[] args) throws Exception {
      Properties props = new Properties();
      InputStream propStream = new FileInputStream(CONFIG_PATH);
      props.load(propStream);
 
      // Create a streaming ingest client
      try (SnowflakeStreamingIngestClient client =
          SnowflakeStreamingIngestClientFactory.builder(props.getProperty("snowclient")).setProperties(props).build()) {
  
        // Create an open channel request on table MY_TABLE, note that the corresponding
        // db/schema/table needs to be present
        // Example: create or replace table MY_TABLE(c1 number);
        OpenChannelRequest request1 =
            OpenChannelRequest.builder(props.getProperty("channel"))
                .setDBName(props.getProperty("database"))
                .setSchemaName(props.getProperty("schema"))
                .setTableName(props.getProperty("table"))
                .setOnErrorOption(
                    OpenChannelRequest.OnErrorOption.CONTINUE) // Another ON_ERROR option is ABORT
                .build();
  
        // Open a streaming ingest channel from the given client
        SnowflakeStreamingIngestChannel channel1 = client.openChannel(request1);

        // Insert rows into the channel (Using insertRows API)


        String url = "jdbc:mysql://db:3306/mydb";
        String user = "root";
        String password = "mypassword";
        int totalRowsInTable = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        Date date1 = new Date();;

        try  {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            date = new Date();
            System.out.println("Started Retrieving data from mysql: " + dateFormat.format(date)); 
            ResultSet rs = stmt.executeQuery("SELECT * FROM STORE_SALES LIMIT 1000000");
            
            date1 = new Date();
            System.out.println("Started loading data into Snowflake table at: "); 
            System.out.println(dateFormat.format(date1)); 
            while (rs.next()) {
                /*int dt = rs.getInt("SS_SOLD_DATE_SK");
                int price = rs.getInt("SS_EXT_SALES_PRICE");

                System.out.printf("dt: %d, price: %d\n", dt, price);
                */
                Map<String, Object> row = new HashMap<>();

                // c1 corresponds to the column name in table
                
                row.put("SS_SOLD_DATE_SK",rs.getFloat("SS_SOLD_DATE_SK"));
                row.put("SS_SOLD_TIME_SK",rs.getFloat("SS_SOLD_TIME_SK"));
                row.put("SS_ITEM_SK",rs.getFloat("SS_ITEM_SK"));
                row.put("SS_CUSTOMER_SK",rs.getFloat("SS_CUSTOMER_SK"));
                row.put("SS_CDEMO_SK",rs.getFloat("SS_CDEMO_SK"));
                row.put("SS_HDEMO_SK",rs.getFloat("SS_HDEMO_SK"));
                row.put("SS_ADDR_SK",rs.getFloat("SS_ADDR_SK"));
                row.put("SS_STORE_SK",rs.getFloat("SS_STORE_SK"));
                row.put("SS_PROMO_SK",rs.getFloat("SS_PROMO_SK"));
                row.put("SS_TICKET_NUMBER",rs.getFloat("SS_TICKET_NUMBER"));
                row.put("SS_QUANTITY",rs.getFloat("SS_QUANTITY"));
                row.put("SS_WHOLESALE_COST",rs.getFloat("SS_WHOLESALE_COST"));
                row.put("SS_LIST_PRICE",rs.getFloat("SS_LIST_PRICE"));
                row.put("SS_SALES_PRICE",rs.getFloat("SS_SALES_PRICE"));
                row.put("SS_EXT_DISCOUNT_AMT",rs.getFloat("SS_EXT_DISCOUNT_AMT"));
                row.put("SS_EXT_SALES_PRICE",rs.getFloat("SS_EXT_SALES_PRICE"));
                row.put("SS_EXT_WHOLESALE_COST",rs.getFloat("SS_EXT_WHOLESALE_COST"));
                row.put("SS_EXT_LIST_PRICE",rs.getFloat("SS_EXT_LIST_PRICE"));
                row.put("SS_EXT_TAX",rs.getFloat("SS_EXT_TAX"));
                row.put("SS_COUPON_AMT",rs.getFloat("SS_COUPON_AMT"));
                row.put("SS_NET_PAID",rs.getFloat("SS_NET_PAID"));
                row.put("SS_NET_PAID_INC_TAX",rs.getFloat("SS_NET_PAID_INC_TAX"));
                row.put("SS_NET_PROFIT",rs.getFloat("SS_NET_PROFIT"));
        
                // Insert the row with the current offset_token
                InsertValidationResponse response = channel1.insertRow(row, String.valueOf(totalRowsInTable));
                if (response.hasErrors()) {
                  // Simply throw if there is an exception, or you can do whatever you want with the
                  // erroneous row
                  throw response.getInsertErrors().get(0).getException();
                }  
                totalRowsInTable++;                  
            }

        } catch (SQLException ex) {
            System.err.println("An error occurred while connecting to the database: " + ex.getMessage());
        }
    
        Date date2 = new Date();
        System.out.println("\n" + "Completed Streaming data into Snowflake at: " + dateFormat.format(date2)); 
        
        long secondsBetween = (date2.getTime() - date1.getTime()) / 1000;
        System.out.println("Total Streaming time: " + secondsBetween + " Secs for " + totalRowsInTable + " Rows");

        // If needed, you can check the offset_token registered in Snowflake to make sure everything
        // is committed
        final int expectedOffsetTokenInSnowflake = totalRowsInTable - 1; // 0 based offset_token
        final int maxRetries = 100;
        int retryCount = 0;
        Date date3 = new Date();

        do {
          String offsetTokenFromSnowflake = channel1.getLatestCommittedOffsetToken();
          // System.out.println("offset token is " + offsetTokenFromSnowflake);
          if (offsetTokenFromSnowflake != null
              && offsetTokenFromSnowflake.equals(String.valueOf(expectedOffsetTokenInSnowflake))) {
            date3 = new Date();
            System.out.println("SUCCESSFULLY inserted " + totalRowsInTable + " rows at " + dateFormat.format(date3));
            break;
          }
          retryCount++;
        } while (retryCount < maxRetries);
     
        // Close the channel, the function internally will make sure everything is committed (or throw
        // an exception if there is any issue)
        channel1.close().get();

      }
    }
  
}
