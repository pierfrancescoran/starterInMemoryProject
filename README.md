Run the application
-------------------
The configurations for different environments such us port in use or file path to the key store are in the src/main/resources/properties folder and they have to be modified.
In partiular modify the path of the application.ssl.filepath to point to the key store into your environment, the file is called keystore.jks and it is the base folder.
The SSL certificate is self signed so when testing the application (for example with Postman) tools might warn you about possible danger if accessing the application.
There is also a Dockerfile that can be used to build an image to deploy on Docker.
There are 3 environments:
* dev
* test
* production

The environment files are used to deploy the application on different environments and to modify the behaviour of the application, for example on production mode the application will return as less information as possible in case of application failure or error.

To run the application
1. Run it with dev configuration:
    * modify the src/main/resources/properties/system-dev.properties in particular the application.ssl.filepath
    * build it using Maven
        $ mvn package
    * run it
        $ java -jar -Dspring.profiles.active=dev target/test-application-0.1.0.jar

2. Run the test or production configuration on Docker
    * modify the src/main/resources/properties/system-test.properties or system-production.properties in particular the application.ssl.filepath
    * build it using Maven
        $ mvn package
    * build the image
        $ docker build -t versionApp .
    * run the image into Docker
        $ docker run -it --rm --publish-all=true -p 8443:8443 versionApp
      or run the image into a Docker in swarm mode
        $ docker service create --publish 8443:8443 --replicas 1 --with-registry-auth --name versionApp versionApp java -jar -Dspring.profiles.active=test /home/test-application-0.1.0.jar --network versionApp


Use the application
-------------------
Call the web service with HTTP tools such as Postman for Chrome or HTTP requester for FireFox (in case you are using Postman first access the web service trhough the browser and authorize the access to the page even if insecure).
There is only one user stored into the internal server memory: "username":"test@testington.com","password":"London". More user can be added from the com.peter.config.SecurityConfig.java file. The latest version of the application is stored into the com.peter.model.httpbody.AbstractMobileFeatures.java file. The class com.peter.bl.MobileBL.java contains all the business logic.
1. Authentication request:
address: https://192.168.0.29:8443/authenticate
method: POST
headers: Content-Type:application/json
body: {"username":"test@testington.com","password":"London","rememberMe":true}
Authentication response:
headers: Authorization:Bearer ... (to use in all the future request to the API)
2. Get something request:
address: https://192.168.0.29:8443/appFired
MONDATORY parameters: appVersion=3
method: GET
MANDATORY header: Authorization:Bearer ...
headers: Content-Type:application/json