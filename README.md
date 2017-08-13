# N26 Code Challenge

## Requirements

We would like to have a restful API for our statistics. The main use case for our API is to
calculate realtime statistic from the last 60 seconds. There will be two APIs, one of them is
called every time a transaction is made. It is also the sole input of this rest API. The other one
returns the statistic based of the transactions of the last 60 seconds.

## Run out-of-the-box

Spring Boot enables you to run the application within your IDE, inside an embedded Tomcat:

 - Clone the project from [Gitlab]
 - Use STS (Spring Toolkit Suite), IntelliJ or Eclipse to import the project
 - Run _Application.java_ as Java Application.
 - Done.

Application logs are written to challenge.log inside your project folder.
logging can be configured in application.properties

## API Documentation

Check generated swagger   http://localhost:8080/swagger-ui.html after deployment






