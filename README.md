# Hawaii

## Backend

### Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

#### Prerequisites
What things you need to setup before starting server

- Install [Java 10](https://docs.oracle.com/javase/10/install/overview-jdk-10-and-jre-10-installation.htm#JSJIG-GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A) and set the JAVA_HOME Environment Variable
- Install [Maven](https://maven.apache.org/install.html)
- Install [MySQL](https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/)
- Install [Tomcat 9](https://octopus.com/blog/installing-tomcat-from-scratch)

##### Common
- Install Lombok plugin in IntelliJ
- Install PlantUml plugin in IntelliJ

#### Create MySQL database:
```sh
Database name: hawaii
Port: 3306
Username: root
Password: root
```

#### Building application:

1 . Navigate to <br/>
```
project_root
```

2 . Generate war file <br/>
```
mvn clean install
```

##### Start with maven
```
mvn spring-boot:run
```

Then go to http://localhost:8080 to see results

##### Start with tomcat

1 . Move war file from _target_ folder to tomcat _webapps_ folder

2 . Start tomcat with: <br/>
```
service tomcat start 
```

Or startup.sh from bin folder <br/>
```
./startup.sh
```

Then go to http://localhost:8080/hawaii (*) to see results

(*) _/hawaii_ path depends on tomcat configuration

3 . Stop tomcat with: <br/>
```
./shutdown.sh 
```

## Frontend

### Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

#### Prerequisites

What things you need to setup before starting server

- Install [Node](https://nodejs.org/en/)

#### Server starting

1 . Navigate to <br/>
```
project_root/src/main/frontend
```

2 . Install node_modules <br/>
```
yarn or npm install
```

3 . And start with <br/>
```
yarn start or npm start
```

Then open http://localhost:3000/ to see your app