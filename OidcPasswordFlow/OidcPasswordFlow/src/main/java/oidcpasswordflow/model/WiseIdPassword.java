package oidcpasswordflow.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import javax.crypto.Cipher;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class WiseIdPassword {

	private static final String ALGORITHM = "RSA";
	private static final String STRING_EMPTY = "";

	private String grant_type = "password";
	private String scope = "openid profile email";

	private String username = STRING_EMPTY;
	private String thumbprint = STRING_EMPTY;
	private String password = STRING_EMPTY;
	private String client_id = STRING_EMPTY;
	private String client_secret = STRING_EMPTY;

	public String getThumbprint(){
		return this.thumbprint;
	}

	public String getPassword(){
		return this.password;
	}

	public WiseIdPassword(String userId, String pass, String appId, String appKey,  String pathCrt){

		this.username = userId;
		this.client_id = appId;
		this.client_secret = appKey;

		X509Certificate cert = getCertificate(pathCrt);
		if (cert == null) return;

		this.thumbprint = getThumbprint(cert);

		byte[] passBytes = encrypt(cert, pass.getBytes());
		this.password = getHexString(passBytes, true);
	}

	public MultiValueMap<String, String> buildFields(){
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();

		map.add("grant_type", this.grant_type);
		map.add("username", this.username + "_" + this.thumbprint);
		map.add("password", this.password);
		map.add("scope", this.scope);
		map.add("client_id", this.client_id);
		map.add("client_secret", this.client_secret);
		return map;
	}

	public String toJson(){
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			return gson.toJson(this);
		} catch (Exception e) {
			return STRING_EMPTY;
		}
	}

	private String getHexString(byte[] data, boolean isLower) {
		try {
			StringBuilder sb = new StringBuilder();
			for (byte b : data) {
				sb.append(String.format("%02X", b));
			}
			if (isLower){
				return sb.toString().toLowerCase();
			} else {
				return sb.toString().toUpperCase();
			}

		} catch (Exception ex){
			return STRING_EMPTY;
		}
	}

	private X509Certificate getCertificate(String resourceFilePath) {
		try {
			InputStream inputCert = WiseIdPassword.class.getClassLoader().getResourceAsStream(resourceFilePath);
			CertificateFactory f = CertificateFactory.getInstance("X.509");
			return (X509Certificate)f.generateCertificate(inputCert);
		} catch (Exception e){
			return null;
		}
	}

	private String getThumbprint(X509Certificate cert) {
		try {

			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] der = cert.getEncoded();
			md.update(der);
			byte[] digest = md.digest();
			return getHexString(digest, false);
		} catch (Exception e){
			return STRING_EMPTY;
		}
	}

	private byte[] encrypt(X509Certificate certificate, byte[] inputData) {
		try {
			PublicKey key = certificate.getPublicKey();

			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);

			return cipher.doFinal(inputData);
		} catch (Exception e){
			logException(e);
		}
		return null;

	}

	public static void logException(Exception ex){
		String stackTrace = Arrays.toString(ex.getStackTrace());
		System.out.println("Error: " +  ex.getMessage() + "\n Stack trace: " + stackTrace);
	}

	public static void logMessage(String message){
		System.out.println(message);
	}

}
