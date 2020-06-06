## Resource Server Example

This project is meant to be used as boilerplate for any resource
server using authorization-server for authenticatin and authorization.

## How run

In root directory run

```$xslt
./gradlew bootrun
```

## How to test

First start up the authorization server and retrive a token. 
See the [readme](https://github.com/opencarrentalproject/authorization-service/blob/master/README.md) of the project for more details

There are two endpoints for the resource items

- GET /items/{id}: Here the response should be `secured information call by {name of the principal transfered via token}`

Token must have a read scope

- POST /items/{id} with a text body: Here the response should be `${given text body} submitted by ${principal.name}`

Token must have a write scope and admin authority

If no Bearer token is given endpoint will return access denied.

