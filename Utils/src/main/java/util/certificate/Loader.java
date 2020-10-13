package util.certificate;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import util.base.UtilException;

public class Loader {
	
	private final static String KEYSTORE_JKS = "jks";
	private final static String KEYSTORE_PKCS12 = "pkcs12";
	private final static String MSG_LOAD_FALSE = "Can not load certificate";
	
	/**
     * Load the jks certificate from file 
     * @param path The absolute path to jks file
     * @param password The password of jks file
     * @return return Certificate, other throw a OidcException
     * @throws UtilException
     */
    public static KeyStore LoadJksFile(String path, String password) throws UtilException {
        try {
            
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_JKS);
            
            FileInputStream inputCert = new FileInputStream(path);
            
            keyStore.load(inputCert, password.toCharArray());
            
            return keyStore;
            
		} catch (Exception e) {
			throw new UtilException(MSG_LOAD_FALSE, e);
		}
    }
    
    /**
     * Load the jks certificate from file 
     * @param path The path to jks file in resouce
     * @param password The password of jks file
     * @return return Certificate, other throw a OidcException
     * @throws UtilException
     */
    public static KeyStore LoadJksResouce(String path, String password) throws UtilException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_JKS);
            
            InputStream inputCert = Loader.class.getClassLoader().getResourceAsStream(path);
            
            keyStore.load(inputCert, password.toCharArray());
            
            return keyStore;
		} catch (Exception e) {
			throw new UtilException(MSG_LOAD_FALSE, e);
		}
    }
    
    /**
     * Load the pkcs12 certificate from file 
     * @param path The path to p12 file in resouce
     * @param password The password of p12 file
     * @return return Certificate, other throw a OidcException
     * @throws UtilException
     */
    public static KeyStore LoadPKCS12Resource(String path, String password) throws UtilException {
        try {
            
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_PKCS12);
            
            InputStream inputCert = Loader.class.getClassLoader().getResourceAsStream(path);
            
            keyStore.load(inputCert, password.toCharArray());
            return keyStore;
            
		} catch (Exception e) {
			throw new UtilException(MSG_LOAD_FALSE, e);
		}
    }
    
    /**
     * Load the pkcs12 certificate from file 
     * @param path The absolute path to p12 file
     * @param password The password of p12 file
     * @return return Certificate, other throw a OidcException
     * @throws UtilException
     */
    public static KeyStore LoadPKCS12File(String path, String password) throws UtilException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_PKCS12);
            
            FileInputStream inputCert = new FileInputStream(path);
            
            keyStore.load(inputCert, password.toCharArray());
            
            return keyStore;
		} catch (Exception e) {
			throw new UtilException(MSG_LOAD_FALSE, e);
		}
    }
    
}
