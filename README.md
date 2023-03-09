### 1. Project Description

this project is a E-commerce Web Application System. By the time 2023/3/9, I only push the backend service
 into this git repo.

Project is base on the course [Spring Boot构建电商基础秒杀项目](https://www.imooc.com/learn/1079)
### 2. Back-end Technology Stack 
* Java (jdk 8.0)
* Springboot (web applicaition)
* Mysql (database)
* Mybatis (ORM)
* Maven(pakage)
* Docker(deployment)

### 3. How to start the project locally with docker.


Maven pakage and build docker image
```shell
mvn clean package
mvn install docker:build
```
start container
```shell
docker-compose up
```
GOTO http://localhost:8088/user/get?id=6 check.




