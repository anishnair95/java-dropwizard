# Java Dropwizard Application

How to start the test application
---

1. Run `mvn clean install` to build your application
2. To execute the db migration run this cmmand `java -jar target/dropwizard-core-2.1.0-SNAPSHOT.jar db migrate config.yml`
3. Start application with `java -jar target/dropwizard-core-2.1.0-SNAPSHOT.jar server config.yml`
4. To check that your application is running enter url `http://localhost:8080`
5. To check status of db run command `java -jar target/dropwizard-core-2.1.0-SNAPSHOT.jar db status config.yml`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
