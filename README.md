# Cucumber test generator

[![Build Status](https://travis-ci.org/TAI-EPAM/jdi-cucumber-test-generator.svg?branch=develop)](https://travis-ci.org/TAI-EPAM/jdi-cucumber-test-generator)
[![codecov](https://codecov.io/gh/TAI-EPAM/jdi-cucumber-test-generator/branch/develop/graph/badge.svg)](https://codecov.io/gh/TAI-EPAM/jdi-cucumber-test-generator)
[![Maintainability](https://api.codeclimate.com/v1/badges/aeb3da30a48d444f6477/maintainability)](https://codeclimate.com/github/TAI-EPAM/jdi-cucumber-test-generator/maintainability)

Web application for creating and managing automated UI test scenarios and generating
 .feature files with executable test specifications for Cucumber.
 
 1. [Synopsis](#synopsis)
 2. [Target users](#target-users)
 3. [Download](#download-application)
 4. [Run](#run-web-application)
 5. [What technologies we used](#what-technologies-we-used)
 
## Synopsis
This is a web-application that provides an user interface for testers. One can create and store
 tests in Gherkin language using this application in order to run these tests via [JDI framework](https://github.com/epam/JDI).

Test case consists of steps. Cases form suits. Suits are united into project.
Project is model of application being tested. 
  * First we create project.
  * Then we should add suit.
  * Next step is creating test cases for suit.
  * Finally you need to specify steps that form the test case.
  
A single step is described with `Given`, `When` or `Then` Gherkin clauses.
One should specify what's given initially in the `Given` clause.
`When` clause describes the condition on which `Then` check is activated. Example:

`Given "I open "<pageName>""`\
`When "On pagination "<paginationName>" I go to first"`\
`Then "link "<linkName>" from "<containerName>" match  "<regex>""`

In this three-step example condition of a link on first page matching regex on page `pageName` is tested.

## Target users
Software Testing Engineers who need 
* a quick and easy way for automation of the UI testing
* a platform for storing and organizing large number of test scenarios
* an automated tests reporting and statistics

## Download application
For downloading application you can use console and clone repository:
```text
git clone https://github.com/TAI-EPAM/jdi-cucumber-test-generator.git
```

## Run web application

1) After downloading you can open project in IntelliJ IDEA. 
2) Select "maven projects"->"plugins"->"tomcat7".
3) Execute "run-war" target. Wait for IDEA to build and deploy .war package. 
4) Open browser and navigate to "localhost:8080/cucumber/swagger-ui.html#/"

## What technologies we used

### [Spring](https://spring.io/docs)
* [Spring MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)
Spring Web MVC is the web framework that provides architecture of pattern Model-View-Controller.

Usage in project:

	Application is build on MVC concept. 
    
* [StateMachine](https://projects.spring.io/spring-statemachine/)
Spring StateMachine is a framework for application developers to use state machine concepts with Spring 
applications. 

Usage in project:

	State machine concept is used to manipulate states of test cases (Passes, Failed, Created and etc)

* [Security](https://projects.spring.io/spring-security/)
Spring Security is a framework that focuses on providing both authentication and authorization to Java applications. 
Spring Security easily can be extended to meet custom requirements.

Usage in project:

    Authorization and authentication
    
### [FreeMaker](https://freemarker.apache.org/docs/index.html)
 FreeMarker™ is a template engine: a Java library to generate text output (HTML web pages, e-mails, configuration files, source code, etc.) based on templates and changing data.
 Templates are written in the FreeMarker Template Language (FTL), which is a simple, specialized language (not a full-blown programming language like PHP). Usually, a general-purpose
 programming language (like Java) is used to prepare the data (issue database queries, do business calculations). Then, Apache FreeMarker displays that prepared data using templates.
 In the template you are focusing on how to present the data, and outside the template you are focusing on what data to present.

### [Hibernate](http://hibernate.org/)
Hibernate is an object-relational mapping tool for the Java programming language. Hibernate's primary 
feature is mapping from Java classes to database tables, and mapping from Java data types to SQL data 
types. Hibernate also provides data query and retrieval facilities. It generates SQL calls and relieves 
the developer from the manual handling and object conversion of the result set. Hibernate Validator 
allows to express and validate application constraints.

Usage in project:

    Work with database byORM.
### [H2 Database](http://www.h2database.com/html/main.html)
H2 is a relational database management system written in Java. It can be embedded in Java applications 
or run in the client-server mode.

Usage in project:

    Is used as data storage.

### [Jackson](https://github.com/FasterXML/jackson)
Jackson is JSON library for Java applications. It is a suite of data-processing tools for Java, 
including the flagship streaming JSON parser / generator library, matching data-binding library 
(POJOs to and from JSON) and additional data format modules to process data encoded in CSV, (Java) 
Properties, XML and etc. 

Usage in project:

    Work with Json files

### [Mockito](http://site.mockito.org/)
Mockito is a mocking framework that allows write tests with a clean and simple API. 
Tests are very readable and they produce clean verification errors.

### [JUnit](http://junit.org/junit5/)
JUnit is a test framework which uses annotations to identify methods that specify a test.

### [Log4j](https://logging.apache.org/log4j/2.x/index.html)
Log4j is a reliable, fast and flexible logging framework (APIs) written in Java, 
which is distributed under the Apache Software License. 
Log4j is highly configurable through external configuration files at runtime. 
It views the logging process in terms of levels of priorities and offers mechanisms 
to direct logging information to a great variety of destinations, 
such as a database, file, console, etc.

**Usage in project**

* Add logs in the project.

### [JaVers](https://github.com/javers/javers#guidelines-for-contributors)
JaVers is the lightweight Java library for auditing changes in your data.

Usage in project:

    Organize test cases' history. 
### [Apache Commons Lang](https://commons.apache.org/proper/commons-lang)
Apache Commons Lang, a package of Java utility classes for the classes 
that are in java.lang's hierarchy, or are considered to be so standard as to 
justify existence in java.lang.

**Usage in project**

* String handling.

### [Java JWT](https://github.com/auth0/java-jwt)
A Java implementation of JSON Web Tokens (is a JSON-based open standard for creating access tokens).

Usage in project:

    Generate and work with tokens for autorization.
     
### [Apache Tomcat Maven Plugin](http://tomcat.apache.org/tomcat-9.0-doc/index.html)
The Apache Tomcat Maven Plugin provides goals to manipulate WAR projects within the Apache Tomcat servlet container. 
You can run your War Apache Maven project through Apache Maven without deploying your WAR file to an Apache Tomcat 
instance.

Usage in project:

    More convinient way to run WAR project.
