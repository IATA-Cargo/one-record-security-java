/**
 * File BaseController.java
 * 
 */
package app.openidconnect.controller;

import java.util.Map;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import app.openidconnect.Model.BaseSessionInformation;
import util.base.Common;

/**
 * 
 * @author 
 *
 */
public class BaseController {
	
	private static final String ISSUER_URI = "issuer";
	private static final String JWKS_URI = "jwks_uri";
	private static final String TOKEN_URI = "token_endpoint";
	private static final String OIDC_APP_KEY = "wiseid";
	
	protected BaseSessionInformation baseSession;
	protected ClientRegistrationRepository clientRegistration;
	
	public BaseController(BaseSessionInformation baseSessionInformation, 
			ClientRegistrationRepository clientRegistrationRepository) {
        this.baseSession = baseSessionInformation;
        this.clientRegistration = clientRegistrationRepository;
    }
	
	/** Get token id */
	protected String getIdToken() {
		
		if (baseSession == null) {
			return "";
		}
		
		return baseSession.getIdToken();
	}
	
	/** Get authentication code */
	protected String getAuthenticationCode() {
		
		if (baseSession == null) {
			return "";
		}
		
		return baseSession.getAuthenticationCode();
	}
	
	/** Get token response from  token uri */
	protected String getTokenResponse() {
		
		if (baseSession == null) {
			return "";
		}
		
		return baseSession.getRaw();
	}
	
	/** Check is login */
	protected boolean isLogin() {
		String idToken = getIdToken();
		
		return !Common.isStringEmpty(idToken);
	}
	
	/** Redirect to signout then login */
	protected String redirectLogin() {
		return "redirect:/signout";
	}
	
	/** Redirect to error page */
	protected String redirectError(String error, String details) {
		return "redirect:/cacheerror?error=" + error + "&details=" + details;
	}
	
	/** Get application configuration */
	protected ClientRegistration getAppConfig() {
		if (clientRegistration == null) {
			return null;
		}
		
		return clientRegistration.findByRegistrationId(OIDC_APP_KEY);
	}
	
	/** Get jwks uri from configuration */
	protected String getJwksUri() {
		
		Map<String, Object> metaData = getConfigMetadata();
		if (metaData == null) {
			return "";
		}
		
		if (!metaData.containsKey(JWKS_URI)) {
			return "";
		}
		
		return (String) metaData.getOrDefault(JWKS_URI, "");
	}

	/** Get token uri from configuration */
	protected String getTokenUri() {
		
		Map<String, Object> metaData = getConfigMetadata();
		if (metaData == null) {
			return "";
		}
		
		if (!metaData.containsKey(TOKEN_URI)) {
			return "";
		}
		
		return (String) metaData.getOrDefault(TOKEN_URI, "");
	}
	
	/** Get issuer uri from configuration */
	protected String getIssuerUri() {
		
		Map<String, Object> metaData = getConfigMetadata();
		if (metaData == null) {
			return "";
		}
		
		if (!metaData.containsKey(ISSUER_URI)) {
			return "";
		}
		
		return (String) metaData.getOrDefault(ISSUER_URI, "");
	}
	
	/**  */
	private Map<String, Object> getConfigMetadata(){
		ClientRegistration config = getAppConfig();
		if (config == null) {
			return null;
		}
		
		ProviderDetails details = config.getProviderDetails();
		
		if (details == null) {
			return null;
		}
		
		return details.getConfigurationMetadata();
	}
	
}
