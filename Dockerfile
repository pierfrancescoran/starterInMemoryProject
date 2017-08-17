FROM openjdk:8

MAINTAINER Peter

ADD keystore.jks /home/infrustructure/

ADD target/test-application-0.1.0.jar /home

WORKDIR /home

EXPOSE 8443 

# RUN java -jar -Dspring.profiles.active=docker /home/search-engine-application-0.1.0.jar