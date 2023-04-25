FROM mysql:latest

ENV MYSQL_ROOT_PASSWORD mypassword
ENV MYSQL_DATABASE mydb
ENV MYSQL_USER myuser
ENV MYSQL_PASSWORD myuserpassword

COPY mysql_db/stores_sales_1m_1.csv.gz /var/lib/mysql-files/
COPY mysql_db/stores_sales_1m_2.csv.gz /var/lib/mysql-files/
COPY mysql_db/stores_sales_1m_3.csv.gz /var/lib/mysql-files/
COPY mysql_db/stores_sales_1m_4.csv.gz /var/lib/mysql-files/
COPY mysql_db/stores_sales_1m_5.csv.gz /var/lib/mysql-files/
COPY mysql_db/stores_sales_1m_6.csv.gz /var/lib/mysql-files/
COPY mysql_db/stores_sales_1m_7.csv.gz /var/lib/mysql-files/
COPY mysql_db/stores_sales_1m_8.csv.gz /var/lib/mysql-files/

RUN gzip -d /var/lib/mysql-files/stores_sales_1m_1.csv.gz
RUN gzip -d /var/lib/mysql-files/stores_sales_1m_2.csv.gz
RUN gzip -d /var/lib/mysql-files/stores_sales_1m_3.csv.gz
RUN gzip -d /var/lib/mysql-files/stores_sales_1m_4.csv.gz
RUN gzip -d /var/lib/mysql-files/stores_sales_1m_5.csv.gz
RUN gzip -d /var/lib/mysql-files/stores_sales_1m_6.csv.gz
RUN gzip -d /var/lib/mysql-files/stores_sales_1m_7.csv.gz
RUN gzip -d /var/lib/mysql-files/stores_sales_1m_8.csv.gz


COPY mysql_db/init_mysql.sql /docker-entrypoint-initdb.d/

EXPOSE 3306