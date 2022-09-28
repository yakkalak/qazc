# user-management-api
An API hosting a couple of endpoints using data from Trafiklab’s open API (https://www.trafiklab.se).

The API offers the following features:
- Return the details of top 10 bus lines in Stockholm area with most bus stops on their route.
- Return a list of every bus stop of the bus line provided.

## Key Technologies Used
- Java 11
- Spring Boot
    - embedded tomcat
    - cache
    - scheduler
    - rest template
- Junit 5
- Mockito
- AssertJ
- slf4j
- Maven
- commons-lang3
- H2 Embedded Database
## Instructions to run the application

- Download the source code from [[https://github.com/yakkalak/qazc](https://github.com/yakkalak/qazc)].
- Open the project using a Java IDE of your choice (IntelliJ or Eclipse).
- Locate a file called '***UserManagementApplication***', right-click and run as a java application.
- Since it is a spring boot application, no additional build steps are needed. The application listens on port '***9001***' once it is started successfully.
## API Responses
The API responds with two formats based on the status returned.
- Success response with a 200 http response code to the client - format as shown below
     { 
        "status":"Success", // A string representing the status of the operation
        "timestamp":"2020-01-18T18:45:00", //A date-time without a time-zone in the ISO-8601 calendar system
        "data":
          [ 
             { 
               /* resultant data from endpoints */
             }
          ]
       }

  - Failure response with corresponding http status code to the client - format as shown below
      {
        "timestamp": "2022-09-26T15:38:01.739+00:00",
        "message": "User not found :: 5",
        "details": "uri=/user-management/users/5"
      }

## API Endpoints
The API exposes following endpoints for use. They shall be invoked as follows.
- ***List all user***

Use the below URL on a web browser or rest client -
[http://localhost:9001/users](http://localhost:9001/users)

If successful, the endpoint responds back with a sample response as below.

[
    {
        "id": 2,
        "name": "two",
        "password": "two",
        "emailId": "two@test.com",
        "lastLoginTime": "2022-09-26T15:39:51.456320Z"
    },
    {
        "id": 3,
        "name": "one",
        "password": "one",
        "emailId": "one@test.com",
        "lastLoginTime": "2022-09-26T15:43:45.335912Z"
    }
]

- ***List details of user based on id***

Use the below URL on a web browser or rest client -
[http://localhost:9001/users/2](http://localhost:9001/users/2)

If successful, the endpoint responds back with a sample response as below.

[
    {
        "id": 2,
        "name": "two",
        "password": "two",
        "emailId": "two@test.com",
        "lastLoginTime": "2022-09-26T15:39:51.456320Z"
    }
]


- ***delete a user***

Use the below URL on a web browser or rest client -
[http://localhost:9001/users/2](http://localhost:9001/users/2)

If successful, the endpoint responds back with a status code 200

- ***update user***

Use the below URL on a web browser or rest client -
[http://localhost:9001/users/3](http://localhost:9001/users/3)

If successful, the endpoint responds back with a status code 200

{
"id": 3,
"name": "updated",
"password": "Testlast",
"emailId": "one@test.com",
"lastLoginTime": "2022-09-26T15:48:48.335757200Z"
}
## Assumptions/Considerations
- Tokens can be used for authentication for each request except login and create user

## Good to have
- standard time format 
- API specification (documentation using annotations) spring utility
