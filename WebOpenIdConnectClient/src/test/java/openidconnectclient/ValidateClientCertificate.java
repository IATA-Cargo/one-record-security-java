package openidconnectclient;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import util.certificate.Loader;
import util.net.RestWrapper;

import java.security.KeyStore;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValidateClientCertificate {

    private String verifyCertificatePass = "Wisekey@2020";
    @Test
    public void ClientCertificateProductExpired() {
        KeyStore keyStore = null;
        try{
            keyStore = Loader.LoadJksResource("ExpiredPersonalProduct.jks", verifyCertificatePass);
            RestWrapper callApi = new RestWrapper();
            callApi.createTemplate(keyStore, verifyCertificatePass);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertNotNull(keyStore);
        }
    }

    @Test
    public void ClientCertificateDevExpired() {
        KeyStore keyStore = null;
        try{
            keyStore = Loader.LoadJksResource("ExpiredDev.jks", verifyCertificatePass);
            RestWrapper callApi = new RestWrapper();
            callApi.createTemplate(keyStore, verifyCertificatePass);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertNotNull(keyStore);
        }
    }

    @Test
    public void ClientCertificateDevRevoked() {
        KeyStore keyStore = null;
        try{
            keyStore =  Loader.LoadJksResource("RevokedCertDemo.jks", verifyCertificatePass);
            RestWrapper callApi = new RestWrapper();
            callApi.createTemplate(keyStore, verifyCertificatePass);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertNotNull(keyStore);
        }
    }

    @Test
    public void ClientCertificateDevGood() {
        KeyStore keyStore = null;
        try{
            keyStore = Loader.LoadJksResource("GoodCertDemo.jks", verifyCertificatePass);
            RestWrapper callApi = new RestWrapper();
            callApi.createTemplate(keyStore, verifyCertificatePass);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertNotNull(keyStore);
        }
    }

    @Test
    public void ClientCertificateProductGood() {
        KeyStore keyStore = null;
        try{
            keyStore = Loader.LoadJksResource("GoodCertProduct.jks", verifyCertificatePass);
            RestWrapper callApi = new RestWrapper();
            callApi.createTemplate(keyStore, verifyCertificatePass);
            Assert.assertNotNull(keyStore);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}