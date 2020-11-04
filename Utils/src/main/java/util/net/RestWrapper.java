package util.net;

import java.security.KeyStore;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import util.openidconnect.OidcException;

/**
 * The wrapper class to call api 
 */
public class RestWrapper {

    /**
     * Create restTemplate to call a uri
     * @param clientCertificate The client certificate send to server, pass null if not
     * @param password The password of client certificate
     * @return return a RestTemplate, other throws OidcException
     */
    public RestTemplate createTemplate(KeyStore clientCertificate, String password) throws OidcException {

        try {

            RestTemplate restTemplate = new RestTemplate();

            if (clientCertificate != null) {

                HttpComponentsClientHttpRequestFactory requestFactory = null;
                SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(new SSLContextBuilder()
                        .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                        .loadKeyMaterial(clientCertificate, password.toCharArray()).build(),
                            NoopHostnameVerifier.INSTANCE);

                HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
                        .setMaxConnTotal(5)
                        .setMaxConnPerRoute(5)
                        .build();

                requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
                requestFactory.setReadTimeout(10000);
                requestFactory.setConnectTimeout(10000);

                restTemplate.setRequestFactory(requestFactory);
            }

            return restTemplate;
        } catch (Exception e) {
            if (clientCertificate != null){
                throw new OidcException("Client certificate invalid", e);
            }
            throw new OidcException("Can not create rest template", e);
        }
    }
    
    /**
     * Get data from uri
     * @param restTemplate The restTemplate to call
     * @param uri The uri to fetching data
     * @param method The method of request
     * @param entity The information to send to server (Header, body)
     * @return Content of response body
     */
    public String request(RestTemplate restTemplate, String uri, HttpMethod method,  HttpEntity<?> entity) {

        ResponseEntity<String> response;
        response = restTemplate.exchange(uri, method, entity, String.class);

        return response.getBody();
    }
    
}