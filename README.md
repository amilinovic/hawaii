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
_project\_root_

2 . Generate war file <br/>
_mvn_ _clean_ _install_

##### Start with maven

1 . _mvn_ _spring-boot:run_

Then go to http://localhost:8080 to see results

##### Start with tomcat

1 . Move war file from _target_ folder to tomcat _webapps_ folder

2 . Start tomcat with: <br/>
_service_ _tomcat_ _start_ 

Or startup.sh from bin folder <br/>
_./startup.sh_

Then go to http://localhost:8080/hawaii (*) to see results

(*) _/hawaii_ path depends on tomcat configuration

3 . Stop tomcat with: <br/>
_./shutdown.sh_ 

## Frontend

### Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

#### Prerequisites

What things you need to setup before starting server

- Install [Node](https://nodejs.org/en/)

#### Server starting

1 . Navigate to <br/>
_project\_root_/_src_/_main_/_frontend_

2 . Install node_modules <br/>
_yarn_ or _npm_ _install_

3 . And start with <br/>
_yarn_ _start_ or _npm_ _start_

Then open http://localhost:3000/ to see your app