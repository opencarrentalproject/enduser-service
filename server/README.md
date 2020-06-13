## Authorization Service

Authorization Service has two main responsibilities:

- Management of End Users
- Authorizing clients accessing resource relevant to users

### Developer Notes

#### How to build and package the project

From root directory run

```$xslt
./gradlew assemble
```    

#### How to run project on local environment

After building and packaging the project run following command in docker directory

```$xslt
docker-compose up
```

#### Road Map

Currently, this project uses the deprecated spring-security-oauth module for
authorization server. There is a spring-authorization-server project in experimental
phase. If this project will come out of experimental stage this repo will be migrated to
use the new authorization server
