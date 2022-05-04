# COFFEE SHOP

# OBJECTIVE

The objective of this project is to create a functional backend for a coffee shop, which must have the following functionalities:

* Register into the Coffee Shop
* Log in/out of the Coffee Shop
* Subscribe to a plan
  * Personal
  * Family
* Upgrade / Downgrade of a plan if Admin
* Discount of the final price of a plan if a discount is used
* Upload credit card information
* Endpoint use by an external source that will perform the charges to the subscriptions

# MANUAL INSTALLATION

* To execute a new version of the Coffee Shop project:

- Execute "maven clean install" in the Coffee Shop project.


- Doing this will automatically copy the jar file to the target folder in the docker compose project (Dockerfiles/target)

* The application at startup should populate the database and create the tables.

# ENDPOINTS

Standalone: 
```
http://localhost:8080
```
Docker: 
```
http://localhost:9000
```
Swagger api
```
Standalone -> http://localhost:8080/coffee/swagger-ui/
Docker -> http://localhost:9000/coffee/swagger-ui/
```

API:

![2022-05-04 19_44_36-Swagger UI](https://user-images.githubusercontent.com/10815551/166839097-1d9500ef-fb96-44fc-9d2e-49193e929578.png)

# PORTS

Standalone

* 8080 -> Spring Application

Docker

* 9000 -> Spring Application running internally on port 8080

# FUTURE UPGRADES

1- Add the necessary configuration to handle refresh token.

2- Registration with mail verification.

3- Extend error handling system.

4- Extend existing roles system (ADMIN, USER, EXTERNAL), and the permissions of each one.

# CONSIDERATIONS TO KEEP IN MIND IN THE FUTURE

* As Docker is use, it would be necessary to consider a Docker vulnerability scanning tool, to prevent new vulnerabilities and different hacking scenarios on libraries and operating systems.


* Verify the internal quality of a system by performing static analysis of source code automatically, looking for patterns with errors, bad practices or incidents, using a tool such as SonarQube.


* Employ a tool to identify vulnerabilities of dependencies, so that possible threats can be detected early.
  

* Perform an analysis to determine the possible scalability of the application, and thus establish the necessary resources.


* When considering the deployment of the application in the cloud, evaluate the requirements of the application with the existing options (AWS, AZURE, G.CLOUD), taking into account computing resources, security, storage, support, costs.


