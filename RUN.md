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
 	- [End-to-end](#end-to-end-tests)


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
2. Select "maven projects", then turn on toggle 'Skip Tests' mode.
3. Then select "jdi-cucumber-test-generator"->"lifecycle"->"install"
4. Select "bdd-generator"->"plugins"->"spring-boot".
5. Execute "run" target. Wait for IDEA to build and deploy .jar package. 
6. Open browser and navigate to [swagger](localhost:8080/swagger-ui.html)
7. In first start use default login

>The application has one user with role Admin by default.
>Please use "admin@mail.com" as login and "admin" as password for login.
>We recommend changing default password for this user.

#### Using console
If you don't have a [Maven](https://maven.apache.org/download.cgi) build tool, install it and set the [environment variables](https://www.mkyong.com/maven/how-to-install-maven-in-windows/).

* To compile, build and run jar file (using java features) execute the following commands:
```bash
mvn clean package -DskipTests=true
java -jar bdd-generator\target\bdd-generator-0.5.0-exec.jar 
```

* Or you also can run the application using spring-boot plugin:
```bash
mvn clean install -DskipTests=true
mvn spring-boot:run -f bdd-generator
```
* Then you can open browser and navigate to [swagger](localhost:8080/swagger-ui.html). In first start use default login.

>The application has one user with role Admin by default.
>Please use "admin@mail.com" as login and "admin" as password for login.
>We recommend changing default password for this user.

### In production environment
If you don't have [JRE](http://www.oracle.com/technetwork/java/javase/downloads/index.html), then to run the application you need to install it.

* To compile, build and run jar file execute the following commands:
```bash
mvn clean package -DskipTests=true
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

**To run all unit tests you need to execute the following command:**

 ```bash
 mvn clean test
 ```
 
**You also can run it using IDE (in our case IntellijIdea).**

`Select "maven projects"->"jdi-cucumber-test-generator"->"lifecycle"->"test".`

### Integration tests
**To run only integration tests you need to perform the following actions:**

* Install the package into the local repository, for use dependencies in other modules
 ```bash
 mvn clean install -DskipTests=true
 ```
* Run integration tests
 ```bash
  mvn failsafe:integration-test
 ```
 
**To run integration and unit tests together you can perform:**
 ```bash
  mvn clean verify
 ```
 
### End-to-end tests

**To run end-to-end tests you need to perform the following actions:**
 
* Install the package into the local repository, for use dependencies in other modules
 ```bash
 mvn clean install -DskipTests=true
 ```
* Run the application with the integration profile
 ```bash
 java -jar -Dspring.profiles.active=integration-tests bdd-generator/target/bdd-generator-0.5.0-exec.jar
 
# or you can run the application using spring-boot plugin
 mvn clean spring-boot:run -f bdd-generator -Dspring.profiles.active=integration-tests
 ```
* Run end-to-end tests
 ```bash
 mvn clean test -f end-to-end-tests -DskipTests=false
 ```
* Run all tests together (unit, integration, end-to-end)
 ```bash
 mvn verify -DskipTests=false
 ```
 
 **You also can run it using IDE (in our case IntellijIdea).**
 1. Select "maven projects", then turn on toggle 'Skip Tests' mode.
 2. Then select "jdi-cucumber-test-generator"->"lifecycle"->"install".
 3. Select "bdd-generator"->"plugins"->"spring-boot"->"run".
 	- create new configuration and add in the command line `-Dspring.profiles.active=integration-tests`
 4. And to the last, select "end-to-end-tests"->"lifecycle"->"test"
    - create new configuration and add in the command line `-DskipTests=false`
  
 **And finally you can run all tests:**
 
 1. Select "maven projects", then turn on toggle 'Skip Tests' mode.
 2. Then select "jdi-cucumber-test-generator"->"lifecycle"->"install"
 3. Select "jdi-cucumber-test-generator"->"lifecycle"->"verify"
    - create new configuration and add in the command line `-DskipTests=false`