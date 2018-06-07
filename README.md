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

#### Server starting:

1 . Generate war file
```sh
mvn clean install
```
2 . Move war file to tomcat "webapps" folder

3 . Start tomcat with:
```sh
service tomcat start 
```

Or startup.sh from bin folder
```sh
./startup.sh
```

Then go to http://localhost:8080/ to see results

## Frontend

### Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

#### Prerequisites

What things you need to setup before starting server

<ul>
    <li>Install <a target="_blank" href="https://nodejs.org/en/">Node</a></li>
</ul>

#### Server starting

Navigate to <br/>
_project\_root_

Install node_modules <br/>
_yarn_ or _npm_ _install_

And start with <br/>
_yarn_ _start_ or _npm_ _start_

Then open http://localhost:3000/ to see your app