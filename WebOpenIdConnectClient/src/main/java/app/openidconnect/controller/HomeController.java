package app.openidconnect.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.RestTemplate;

import app.openidconnect.Model.ApiRequestHeader;
import app.openidconnect.Model.ApiVerifyResponse;
import app.openidconnect.Model.BaseSessionInformation;
import app.openidconnect.Model.HomeModel;
import app.openidconnect.Model.OtherResponseObject;
import util.base.Common;
import util.certificate.Loader;
import util.net.RestWrapper;
import util.openidconnect.OidcUtils;
import util.openidconnect.ResponseTokenInfo;

import java.security.KeyStore;

@Controller
@SessionAttributes("baseSessionInformation")
public class HomeController extends BaseController {
	
	@Value("${verify.uri}")
	private String verifyUri;
	
	@Value("${verify.certifycatePath}")
	private String verifyCertifycatePath;
	
	@Value("${verify.certifycatePass}")
	private String verifyCertifycatePass;
	
	public HomeController(BaseSessionInformation baseSessionInformation,
			ClientRegistrationRepository clientRegistrationRepository
			) {
		
        super(baseSessionInformation, clientRegistrationRepository);
        
    }
    
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public String home(Model model) {
		try {
			String idToken = getIdToken();
			
			if (Common.isStringEmpty(idToken)) {
				return redirectLogin();
			}
			
			return "home";
		} catch (Exception e) {
			return redirectError(e.getMessage(), e.getStackTrace() + "");
		}
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/api" }, method = RequestMethod.GET)
	public String api(Model model) {
		try {
			String idToken = getIdToken();
			String error = "";
			String errorDetails = "";
			String verifyResponse = "";
			
			if (Common.isStringEmpty(idToken)) {
				return redirectLogin();
			}
			
			verifyResponse = callVerifyApi(idToken);
			
			HomeModel data = new HomeModel();
			data.setError(error);
			data.setErrorDetails(errorDetails);
			data.setCode(getAuthenticationCode());
			data.setIdToken(idToken);
			data.setVerifyResult(verifyResponse);
			data.setVerifyUri(verifyUri);
			
			data.setApiRequestHeaders( Common.toJsonPrettyString( new ApiRequestHeader("Bearer " + idToken)));
			
			ApiVerifyResponse dataRes = Common.fromJson(ApiVerifyResponse.class, verifyResponse);
			if (dataRes == null || "".equals(dataRes.getMessage()) || dataRes.getMessage() == null ) {
				OtherResponseObject other = Common.fromJson(OtherResponseObject.class, verifyResponse);
				if (other == null) {
					data.setVerifyMessage("Error");
					data.setApiResponse(verifyResponse);
					data.setResultData("");
					data.setAuthenticatedSubject("");
				} else {
					data.setVerifyMessage("");
					data.setApiResponse(Common.toJsonPrettyString( other ));
					data.setResultData("");
					data.setAuthenticatedSubject("");
				}
			} else {
				data.setVerifyMessage("");
				data.setApiResponse(Common.toJsonPrettyString( dataRes ));
				data.setAuthenticatedSubject(Common.toJsonPrettyString( dataRes.getAuthenticatedSubject()));
				data.setResultData(Common.toJsonPrettyString( dataRes.getResult()));
			}
			
			model.addAttribute("model", data);
			
			return "api";
		} catch (Exception e) {
			return redirectError(e.getMessage(), e.getStackTrace() + "");
		}
		
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/profile" }, method = RequestMethod.GET)
	public String profile(Model model) {
		try {
			String idToken = getIdToken();
			
			if (Common.isStringEmpty(idToken)) {
				return redirectLogin();
			}
			
			HomeModel data = new HomeModel();
			data.setCode(getAuthenticationCode());
			data.setIdToken(idToken);
			data.setApiResponse(getTokenResponse());
			model.addAttribute("model", data);
			
			return "profile";
		} catch (Exception e) {
			return redirectError(e.getMessage(), e.getStackTrace() + "");
		}
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(value = {"/sso_callback" }, method = RequestMethod.GET)
	public String callback(@RequestParam(defaultValue="") String code) {
		String idToken = "";
		
		// Get id token
		if (Common.isStringEmpty(code)) {
			idToken = baseSession.getIdToken();
		} else {
			String sessionCode = baseSession.getAuthenticationCode();
			if (code.equals(sessionCode)) {
				idToken = baseSession.getIdToken();
			} else {
				try {
					OidcUtils rsa = new OidcUtils();
					String tokenUri = getTokenUri();
					String appId = "";
					String appSecret = "";
					String uriCallback = "";
					
					ClientRegistration clientConfig = getAppConfig();
					if (clientConfig != null) {
						appId = clientConfig.getClientId();
						appSecret = clientConfig.getClientSecret();
						uriCallback = clientConfig.getRedirectUriTemplate();
					}
					if (Common.isStringEmpty(tokenUri)) {
						return redirectError("Can not get token uri", "");
					}
					
					ResponseTokenInfo oToken = rsa.getIDToken(code, tokenUri, appId, appSecret,  uriCallback);
					if (oToken != null) {
						idToken = oToken.getIdToken();
						if (!Common.isStringEmpty(idToken)) {
							baseSession.setAuthenticationCode(code);
							baseSession.setIdToken(idToken);
							baseSession.setIdTokenExpireAt(oToken.getExpiresIn());
							baseSession.setRaw(Common.toJsonPrettyString(oToken));
						}
					}
				} catch (Exception e) {
					return redirectError("Can not get id token", e.getMessage());
				}
			}
		}
		
		if (Common.isStringEmpty(idToken)) {
			return redirectError("Can not get id token", "");
		}
		
		return "redirect:/home";
	}
	
	/**
	 * 
	 * @param error
	 * @param details
	 * @return
	 */
	@RequestMapping(value = { "/cacheerror" }, method = RequestMethod.GET)
	public String error(@RequestParam(defaultValue="") String error, 
			@RequestParam(defaultValue="") String details, Model model) {
		
		model.addAttribute("error", error);
		model.addAttribute("details", details);
		return "error";
	}
	
	/**
	 * 
	 * @param idToken
	 * @return
	 */
	private String callVerifyApi(String idToken) {
		try {

			// Call verify api
	    	RestTemplate restTemplate;
		    KeyStore keyStore;
	    	String certPath = verifyCertifycatePath;
	    	String jksPassphare = verifyCertifycatePass;
	    	String serverUri = verifyUri;
	    	
	    	RestWrapper callApi = new RestWrapper();
	    	// Load client certificate
	    	keyStore = Loader.LoadJksResouce(certPath, jksPassphare);
	    	//keyStore = callApi.LoadPKCS12Cert(certPath, jksPassphare);
	    	if (keyStore == null) {
	    		throw new Exception("Can not load client certificate");
	    	}
	    	
	    	// Create template
	    	restTemplate = callApi.createTemplate(keyStore, jksPassphare);
	    	if (restTemplate == null) {
	    		throw new Exception("Can not create request");
	    	}
	    	
	    	// Create request info
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.add("Authorization", "Bearer " + idToken);
	    	HttpEntity<String> entity = new HttpEntity<>(null, headers);
	      
	    	String strBody = callApi.request(restTemplate, serverUri, HttpMethod.GET, entity);
	    	
	    	return strBody;
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
}
