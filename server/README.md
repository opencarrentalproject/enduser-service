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