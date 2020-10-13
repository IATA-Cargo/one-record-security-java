# Configuration projects

### Dummy1API Project
Dummy1API Project create a api to verify Client Certificate and IdToken of a oidc
Default config:
  - Server url: https://localhost:8443
  - Api public: https://localhost:8443/api/DummyOneRecord
  - Server certificate P12: /OpenIdConnectClient2.0/Dummy1API/ssl/server/localhostSSL.p12 (passphrase: changeit)
  - Server root certificate: /OpenIdConnectClient2.0/Dummy1API/ssl/server/localhostSSLRoot.pem
  - Server certificate: /OpenIdConnectClient2.0/Dummy1API/ssl/server/localhostSSL.pem
  - Certificate trusted store in: /OpenIdConnectClient2.0/Dummy1API/ssl/server/trustStore.jks (passphrase: changeit)
  - Need config cache folder in "application.properties" file and key "ocspCacheDir"
  
(Maybe developer must import Root certificate to keystore of java or browsers)

### WebOpenIdConnectClient
WebOpenIdConnectClient project create web site to OAuth 2.0 to WiseID and call to Dummy1API to verify IDToken.
Default config:
  - Server url: http://localhost:8080
  - Url call verify api http://localhost:8080/api
  - Client certificate /OpenIdConnectClient2.0/WebOpenIdConnectClient/src/main/resources/clientcert.jks (passphrase: Wisekey@2020)
  - Client root certificate: /OpenIdConnectClient2.0/WebOpenIdConnectClient/src/main/resourcesOISTE WISeKey Global Root GB CA.pem
  - Client CA certificate: /OpenIdConnectClient2.0/WebOpenIdConnectClient/src/main/WISeKey CertifyID Personal GB CA 4.pem
  
(Maybe developer must import Root/CA certificate to keystore of java or browsers)
