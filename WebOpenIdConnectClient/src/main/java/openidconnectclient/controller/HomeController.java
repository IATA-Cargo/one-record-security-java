package openidconnectclient.controller;

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

import openidconnectclient.Model.*;
import util.base.Common;
import util.certificate.Loader;
import util.net.RestWrapper;
import util.openidconnect.OidcUtils;
import util.openidconnect.ResponseTokenInfo;

import java.security.KeyStore;
import java.util.Map;

@Controller
@SessionAttributes("baseSessionInformation")
public class HomeController {

    private static final String ISSUER_URI = "issuer";
    private static final String JWKS_URI = "jwks_uri";
    private static final String TOKEN_URI = "token_endpoint";
    private static final String OIDC_APP_KEY = "wiseid";

    @Value("${verify.uri}")
    private String verifyUri;

    @Value("${verify.certifycatePath}")
    private String verifyCertificatePath;

    @Value("${verify.certifycatePass}")
    private String verifyCertificatePass;

    protected BaseSessionInformation baseSession;
    protected ClientRegistrationRepository clientRegistration;

    public HomeController(BaseSessionInformation baseSessionInformation,
                          ClientRegistrationRepository clientRegistrationRepository) {
        this.baseSession = baseSessionInformation;
        this.clientRegistration = clientRegistrationRepository;
    }

    /**
     * Home page
     *
     * @return
     */
    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
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
     * Api page, this page call 1R api to validate client with id token and certificate
     *
     * @return
     */
    @RequestMapping(value = {"/api"}, method = RequestMethod.GET)
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

            data.setApiRequestHeaders(Common.toJsonPrettyString(new ApiRequestHeader("Bearer " + idToken)));

            ApiVerifyResponse dataRes = Common.fromJson(ApiVerifyResponse.class, verifyResponse);
            if (dataRes == null || "".equals(dataRes.getMessage()) || dataRes.getMessage() == null) {
                OtherResponseObject other = Common.fromJson(OtherResponseObject.class, verifyResponse);
                if (other == null) {
                    data.setVerifyMessage("Error");
                    data.setApiResponse(verifyResponse);
                    data.setResultData("");
                    data.setAuthenticatedSubject("");
                } else {
                    data.setVerifyMessage("");
                    data.setApiResponse(Common.toJsonPrettyString(other));
                    data.setResultData("");
                    data.setAuthenticatedSubject("");
                }
            } else {
                data.setVerifyMessage("");
                data.setApiResponse(Common.toJsonPrettyString(dataRes));
                data.setAuthenticatedSubject(Common.toJsonPrettyString(dataRes.getAuthenticatedSubject()));
                data.setResultData(Common.toJsonPrettyString(dataRes.getResult()));
            }

            model.addAttribute("model", data);

            return "api";
        } catch (Exception e) {
            return redirectError(e.getMessage(), e.getStackTrace() + "");
        }

    }

    /**
     * Profile page, this show current information (status)
     *
     * @return
     */
    @RequestMapping(value = {"/profile"}, method = RequestMethod.GET)
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
     * Process when Wiseid callback after login with the code flow
     *
     * @param code
     * @return
     */
    @RequestMapping(value = {"/sso_callback"}, method = RequestMethod.GET)
    public String callback(@RequestParam(defaultValue = "") String code) {
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

                    ResponseTokenInfo oToken = rsa.getIDToken(code, tokenUri, appId, appSecret, uriCallback);
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
     * Catch and show error page
     *
     * @param error
     * @param details
     * @return
     */
    @RequestMapping(value = {"/cacheerror"}, method = RequestMethod.GET)
    public String error(@RequestParam(defaultValue = "") String error,
                        @RequestParam(defaultValue = "") String details, Model model) {

        model.addAttribute("error", error);
        model.addAttribute("details", details);
        return "error";
    }

    /**
     * Call 1R Api to verify token
     *
     * @param idToken The id token
     * @return
     */
    private String callVerifyApi(String idToken) {
        try {

            // Call verify api
            RestTemplate restTemplate;
            KeyStore keyStore;
            RestWrapper callApi = new RestWrapper();

            // Load client certificate
            keyStore = Loader.LoadJksResource(verifyCertificatePath, verifyCertificatePass);

            if (keyStore == null) {
                throw new Exception("Can not load client certificate");
            }

            // Create template
            // RestTemplate checked certificate status before send client certificate to server
            // RestTemplate also checked ssl certificate to https url
            restTemplate = callApi.createTemplate(keyStore, verifyCertificatePass);

            if (restTemplate == null) {
                throw new Exception("Can not create request");
            }

            // Create request info
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + idToken);
            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            return callApi.request(restTemplate, verifyUri, HttpMethod.GET, entity);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Get token id in session (this is base for demo)
     * Maybe verify token before using
     *
     * @return
     */
    private String getIdToken() {

        if (baseSession == null) {
            return "";
        }

        return baseSession.getIdToken();
    }

    /**
     * Get authentication code
     *
     * @return
     */
    private String getAuthenticationCode() {

        if (baseSession == null) {
            return "";
        }

        return baseSession.getAuthenticationCode();
    }

    /**
     * Get token response from  token url
     *
     * @return
     */
    private String getTokenResponse() {

        if (baseSession == null) {
            return "";
        }

        return baseSession.getRaw();
    }

    /**
     * Redirect to signout then login
     *
     * @return
     */
    private String redirectLogin() {
        return "redirect:/signout";
    }

    /**
     * Redirect to error page
     */
    private String redirectError(String error, String details) {
        return "redirect:/cacheerror?error=" + error + "&details=" + details;
    }

    /**
     * Get token uri from configuration
     *
     * @return
     */
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

    /**
     * Get metadata configuration
     *
     * @return
     */
    private Map<String, Object> getConfigMetadata() {
        ClientRegistration config = getAppConfig();
        if (config == null) {
            return null;
        }
        ClientRegistration.ProviderDetails details = config.getProviderDetails();
        if (details == null) {
            return null;
        }
        return details.getConfigurationMetadata();
    }

    /**
     * Get application configuration
     *
     * @return
     */
    private ClientRegistration getAppConfig() {
        if (clientRegistration == null) {
            return null;
        }
        return clientRegistration.findByRegistrationId(OIDC_APP_KEY);
    }

}
