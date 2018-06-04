# Hawaii

### Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

#### Prerequisites
What things you need to setup before starting server

- Install [Java](https://docs.oracle.com/javase/10/install/overview-jdk-10-and-jre-10-installation.htm#JSJIG-GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A) and set the JAVA_HOME Environment Variable
- Install [Maven](https://www.mkyong.com/maven/how-to-install-maven-in-ubuntu/)
- Install [MySQL](https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-16-04)
- Install [Tomcat9](https://tecadmin.net/install-tomcat-9-on-ubuntu/)
- Install Lombok plugin in IntelliJ
- Install PlantUml plugin in IntelliJ

**Create MySQL database:**
```sh
Database name: hawaii
Port: 3306
Username: root
Password: root
```

**Server starting:**

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

**Then go to http://localhost:8080/ to see results**