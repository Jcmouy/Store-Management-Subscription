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

## Brief explanation of endpoints

* Auth Controller

```
/coffee/api/auth/signin -> Login to the application
/coffee/api/auth/signup -> Register in the application
```

* Auth External Controller

```
/coffee/api/ex/credential -> Obtain the credentials to make the charges in the subscriptions.
```

* Credit Card Controller

```
/coffee/user/cc/add -> Add credit card
/coffee/user/cc/get -> Get credit card using tokern
/coffee/user/cc/remove -> Remove credit card using tokern
```

* Payment Controller

```
/coffee/api/ex/charge -> Make charges to the subscriptions
/coffee/api/ex/renew -> Renew subscriptions if they are still in effect
```

* Subscribe Controller

```
/coffee/subscription/subscribe/user -> Add user to an existing subscription
/coffee/subscription/subscribe/removeUser/{username} -> Remove user from a subscription
 ```

* Subscription Controller

```
/coffee/subscription/add -> Create a new subscription
/coffee/subscription/add/{discountCode} -> Create a new subscription using a discount code
/coffee/subscription/downgrade/{username} -> Downgrade the plan of a subscription
/coffee/subscription/upgrade/{username} -> Upgrade the plan of a subscription
/coffee/subscription/remove/{username} -> Delete a subscription
 ```

* User Controller

```
/coffee/user/getInvites/{username} -> Retrieve subscription invitations using the username 
/coffee/user/acceptInvite -> Accept the invitation
/coffee/user/rejectInvite -> Reejct the invitation
 ```

Note that for all endpoints, except those belonging to Auth Controller and Auth External Controller, a Bearer Token must be passed.


Examples of calls to these endpoints can be found in the folder "captures"


The possible Roles that we have are:

```
ROLE_USER -> User when created
ROLE_ADMIN -> User with a Personal or Family plan
ROLE_EXTERNAL -> User with credentials to perform the charges or renewal of the subscriptions.
```

# TECHNOLOGY STACK

* Spring Boot
* Maven
* Swagger2
* Spring AOP(logging, event logging)
* H2 Database
* Docker

# DATABASE

H2 Database:


# PORTS

Standalone

* 8080 -> Spring Application

Docker

* 9000 -> Spring Application running internally on port 8080

# FUTURE UPGRADES

1- Add the necessary configuration to handle refresh token,  for this we should validate the time of validity of the token, and in case it has expired force the user to log in again.

2- Registration with mail verification, a second step to make sure that the e-mail entered by the user is real and belongs to the user. The account would be created but it would be deactivated until the user clicks on the activation button that would arrive to his email.

3- Extend error handling system, the one currently in use is very basic, we could create an ExceptionHelper and encapsulate all possible exceptions there.

4- Extend existing roles system (ADMIN, USER, EXTERNAL), and the permissions of each one. This would come along with a privilege system that would really differentiate between users and what they can do and see.

5- Replace the current database (H2) with one that provides the necessary strength to handle real scenarios.

# CONSIDERATIONS TO KEEP IN MIND IN THE FUTURE

* It was not required, but I thought it would be a good idea to use Docker. The disadvantage of using it is that in the future it would be advisable to consider a Docker vulnerability scanning tool, to prevent new vulnerabilities and different hacking scenarios on libraries and operating systems.


* It may be possible to verify the internal quality of a system by performing static analysis of source code automatically, looking for patterns with errors, bad practices or incidents, using a tool such as SonarQube.


* Considering the recent problems with dependencies, it would be advisable to use a tool to identify possible vulnerabilities, so that threats can be detected early.


* In the case of deploying this application in the cloud, it would be necessary to perform an analysis to determine the possible scalability of the application, and start taking the first steps to define the requirements and establish the necessary resources. At this point, it would be necessary to take into account the requirements with the existing options (AWS, AZURE, G.CLOUD), considering computing resources, security, storage, support and costs.



