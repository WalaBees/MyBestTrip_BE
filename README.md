application.yml:

spring:
application:
name: BE

datasource:
driver-class-name: com.mysql.cj.jdbc.Driver
url: jdbc:mysql://localhost:3306/[DB 이름]?serverTimezone=UTC&characterEncoding=UTF-8
username: [root]
password: [DB비번]

web:
cors:
allowed-origins: http://localhost:3000

jpa:
show-sql: true
database-platform: org.hibernate.dialect.MySQL8Dialect
database: mysql
hibernate:
ddl-auto: update
generate-ddl: false
properties:
hibernate:
format_sql: true
enable_lazy_load_no_trans: true
