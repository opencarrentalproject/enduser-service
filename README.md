![CI](https://github.com/opencarrentalproject/authorization-service/workflows/CI/badge.svg?branch=master)

# Authorization Service
Component responsible for managing users, providing registration and login. Supports token based authentication

## Authentication Flow

![Alt](images/AuthorizationServiceFlow.svg)


## Authorization Code Flow with Proof Key for Code Exchange

Public client such as browser or mobile need to use authorization code flow with code challenge.

A simplfied flow can be describe like following:

 - First request to authorization server for authorization code

```
http://localhost:8090/oauth/authorize?response_type=code&client_id=public&redirect_uri=http://localhost:8080&scope=read&code_challenge=4cc9b165-1230-4607-873b-3a78afcf60c5
```

- If user is not logged a login page will appear for the user to enter his credentials. After succesful login user is redicted with authorization code in the query string.

```
http://localhost:8080/?code=S0ONMC
```

- Client needs to request with authorization code and code from the previous code challenge as verification to retrieve the access token
```
curl localhost:8090/oauth/token -d client_id=public -d grant_type=authorization_code -d redirect_uri=http://localhost:8080 -d code=bDtkDx -d code_verifier=4cc9b165-1230-4607-873b-3a78afcf60c5
```


## How to use Authorization Server to Secure Own Resource Server

Steps:

1- Use the docker-compose.yml from infrastructure directory 

```
version: "3"
services:
  mongodb:
    image: mongo:bionic
    container_name: "mongodb"
    ports:
      - 27017
  auth-server:
    image: docker.pkg.github.com/opencarrentalproject/authorization-service/authorization-service:latest
    container_name: auth-server
    ports:
      - 8090:8090
    links:
      - mongodb
  auth-ui:
    image: docker.pkg.github.com/opencarrentalproject/authorization-service/authorization-ui:latest
    container_name: auth-ui
    ports:
      - 80:80
    links:
      - auth-server

```

**IMPORTANT**: You need access to this organisation docker repository to access docker.pkg.github.com/opencarrentalproject. If you are a member of the organisation you need to retrieve personal access token with read rights.
See the project [wiki](https://github.com/opencarrentalproject/project-wiki/wiki) on how to create a access token to login to docker registry.

After successfuly authenticating to docker registry run 
```docker-composee up```

In the end authorization-ui, authorization-service and database should be app.

2- To test the setup go to http://localhost

You should see a login page. Enter **admin** for both username and password. Now you are able
to see users table. You can add/update/delete users and roles.

3- Create a user and a role for your service.
Example: **test@example.com** with a role of **service-admin**

