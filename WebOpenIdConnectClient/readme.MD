A basic demo connect to WiseID by the OpentID Connect with the authorization code flow.

# Dev with

Java JDK 1.8
Spring Tool Suite 4
Build with Maven

# Configurations

Application register a callback uri at http://localhost:8080/sso_callback

The configuration to connect to WiseId in file
```
[folder]/src/main/resources/application.yml
```

The configuration of the verified api in file
```
[folder]/src/main/resources/application.properties
```
Do not forget import CA ssl certificate of WiseID, the verified api and client certificate to java

The client certificates in folder
```
[folder]/certs
```

Need a account register to WiseID