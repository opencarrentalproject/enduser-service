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
