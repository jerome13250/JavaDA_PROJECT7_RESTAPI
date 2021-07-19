# Poseiden / Transform your backend to an API
Openclassrooms project number 7

<!-- ABOUT THE PROJECT -->
## About The Project

This is a Spring Boot application for project number 7 of [Openclassrooms](https://openclassrooms.com/) java back-end formation.

## Project goals:

### Implement a Feature
1. Create mapping domain class and place in package com.nnk.springboot.domain
2. Create repository class and place in package com.nnk.springboot.repositories
3. Create controller class and place in package com.nnk.springboot.controllers
4. Create view files and place in src/main/resource/templates

### Write Unit Test
1. Create unit test and place in package com.nnk.springboot in folder test > java

### Security
1. Create user service to load user from  database and place in package com.nnk.springboot.config.security.userdetails
2. Implement Oauth2 login using GitHub.
3. Add configuration class and place in package com.nnk.springboot.security


## Built With

1. Framework: Spring Boot v2.4.5
2. Java 8
3. Thymeleaf
4. Bootstrap v.4.3.1
5. [Maven 3.8.1](https://maven.apache.org/download.cgi#downloading-apache-maven-3-8-1)

<!-- GETTING STARTED -->
## Getting Started

This is how to set up the project locally.
To get a local copy up and running follow these simple example steps:

### Prerequisites

Check that you have : 
* Java 8 installed
  ```sh
  java -version
  ```
* Maven 3.8.1 installed
  ```sh
  mvn -v
  ```

### Installation

1. Choose a directory for the java project
   ```sh
   cd /path/to/directory/project
   ```
2. Clone the repo
   ```sh
   git clone https://github.com/jerome13250/JavaDA_PROJECT7_RESTAPI.git
   ```
3. Select the poseiden directory
   ```sh
   cd JavaDA_PROJECT7_RESTAPI\Poseiden-skeleton
   ```
4. Create the 2 databases in MySQL using the .sql files in JavaDA_PROJECT7_RESTAPI\Poseiden-skeleton\doc.
	* data.sql will create the "demo" database.
	* data_integration_test.sql will create the "demo_test" database that will be used for integration tests.
5. Package the application (fat jar file) using maven.
   ```sh
   mvn package
   ```
6. Execute the jar file
   ```JS
   java -jar ./target/spring-boot-skeleton-0.0.1-SNAPSHOT.jar
   ```
7. To access the application, open your browser, go to [http://localhost:8080/user/add](http://localhost:8080/user/add) to create a new user with login/password to be able to access all the pages of the application.

