## This file describes how to download and run application, tests in various cases
 1. [Download application](#download-application)
 2. [Run application](#run-web-application)
 	- [In development environment](#in-development-environment)
 		- [Using IDE](#using-ide)
 		- [Using console](#using-console)	
 	- [In production environment](#in-production-environment)
 3. [Run tests](#run-tests)
 	- [Unit](#unit-tests)
 	- [Integration](#integration-tests) 


## Download application
For downloading application you can use console and clone repository:
```bash
git clone https://github.com/TAI-EPAM/jdi-cucumber-test-generator.git
```

## Run web application
### In development environment
#### Using IDE
Firstly, if you don't have a [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html), you should install it.

1. After downloading you can open project using IDE (e.g. we  use [IntelliJ IDEA](https://www.jetbrains.com/idea/download/#section=windows)). 
2. Select "maven projects"->"bdd-generator"->"plugins"->"spring-boot".
3. Execute "run" target. Wait for IDEA to build and deploy .jar package. 
4. Open browser and navigate to "localhost:8080/swagger-ui.html"
5. In first start use default login

#### Using console
If you don't have a [Maven](https://maven.apache.org/download.cgi) build tool, install it and set the [environment variables](https://www.mkyong.com/maven/how-to-install-maven-in-windows/).
* To compile and build jar file execute the following commands:
```bash
mvn clean install -DskipTests=true
```
* To run the application:
```bash
mvn clean spring-boot:run -f bdd-generator

# or you can run the application using java features
java -jar bdd-generator\target\bdd-generator-0.5.0-exec.jar 
```

>The application has one user with role Admin by default.
>Please use "admin@mail.com" as login and "admin" as password for login.
>We recommend changing default password for this user.

### In production environment
If you don't have [JRE](http://www.oracle.com/technetwork/java/javase/downloads/index.html), then to run the application you need to install it.

* To run the application, you need to execute the following command:
```bash
java -jar deploy/bdd-generator-0.5.0-exec.jar
# Where deploy is home directory of jar file
```
* If you want to change some configuration values and don't want to rebuild all project, you can
create `application.yml` file and put it in the root folder.
```yaml
# jwt_secret is a key word to config the right and secure JWT token file
jwt_secret: iteaky
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:file:~/h2/app_db
    username: sa
    password: sa
  jenkins:
    url: http://ecse00100b3d.epam.com:8888
    login: admin
    password: admin
  mail:
  	default-encoding: UTF-8
    host: smtp.gmail.com
    port: 587
    username: cucumbervarificator@gmail.com
    password: cucumberadmin
    properties:
      mail:
        smtp:
          auth: true
          starttls:
          enable: true
    protocol: smtp
``` 

## Run tests
### Unit tests
**To run all unit tests you need to execute the following command in the console:**
 
 ```text
 mvn clean test -f bdd-generator
 ```
**You also can run it using IDE (in our case IntellijIdea).**

`Select "maven projects"->"bdd-generator"->"lifecycle"->"test".`
 
### Integration tests
**To run integration tests you need to perform the following actions in the console:**
 
* Install the package into the local repository, for use as a dependency in other projects locally
 ```text
 mvn clean install -DskipTests=true
 ```
* Run the application with the integration profile
 ```text
 java -jar -Dspring.profiles.active=integration-tests deploy/bdd-generator-0.5.0-exec.jar
 # Where deploy is home directory of jar file
 
# or you can run the application using maven plugin
 mvn clean spring-boot:run -f bdd-generator -Dspring.profiles.active=integration-tests
 ```
* Run all integration tests
 ```text
 mvn clean test -f jdi-cucumber-test-generator-integration-tests -DskipTests=false
 ```
* Run all tests
```text
 mvn clean test -DskipTests=false
 ```
 
 **You also can run it using IDE (in our case IntellijIdea).**
 1. Turn on toggle 'Skip Tests' mode
 2. Select "maven projects"->"jdi-cucumber-test-generator"->"lifecycle"->"install".
 3. Then select "maven projects"->"bdd-generator"->"plugins"->"spring-boot"->"run".
 	- create new configuration and add in the command line `-Dspring.profiles.active=integration-tests`
 4. And to the last select "maven projects"->"jdi-cucumber-test-generator-integration-tests"->"lifecycle"->"test"
    - create new configuration and add in the command line `-DskipTests=false`
  
 **And finally you can of course run all tests:**
 
  Select "maven projects"->"jdi-cucumber-test-generator"->"lifecycle"->"test"
  - create new configuration and add in the command line `-DskipTests=false`