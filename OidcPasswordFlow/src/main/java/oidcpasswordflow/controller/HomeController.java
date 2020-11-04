package oidcpasswordflow.controller;

import com.google.gson.Gson;
import oidcpasswordflow.model.WiseIdPassword;
import oidcpasswordflow.model.WiseIdPasswordResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller

public class HomeController {

    @Value("${oidc.token.url}")
    String serverUri = "";
    @Value("${oidc.token.username}")
    String username = "";
    @Value("${oidc.token.password}")
    String password = "";
    @Value("${oidc.token.appid}")
    String appId = "";
    @Value("${oidc.token.appkey}")
    String appKey = "";
    @Value("${oidc.token.certificatepath}")
    String certPath = "";

    public HomeController() {

    }

    /**
     * Home page, user was Authentication
     *
     * @return
     */
    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public String home() {
        try {
            WiseIdPasswordResponse authn = getToken();
            if (authn == null) {
                throw new Exception("");
            }

            if (authn.getAccessToken() == null || "".equals(authn.getAccessToken())) {
                throw new Exception("Access Token is empty");
            }

            if (authn.getIdToken() == null || "".equals(authn.getIdToken())) {
                throw new Exception("Id Token is empty");
            }

            return "home";
        } catch (Exception e) {
            return redirectError("Unauthorized", e.getMessage());
        }
    }

    @RequestMapping(value = {"/appError"}, method = RequestMethod.GET)
    public String error(@RequestParam(defaultValue = "") String error,
                        @RequestParam(defaultValue = "") String details, Model model) {

        model.addAttribute("error", error);
        model.addAttribute("details", details);

        return "error";
    }

    private WiseIdPasswordResponse getToken() throws Exception {

        WiseIdPassword data = new WiseIdPassword(username, password, appId, appKey, certPath);
        if ("".equals(data.getThumbprint()) || "".equals(data.getPassword())) {
            throw new Exception("Config data error");
        }
        String strBody;
        try {
            RestTemplate callApi = new RestTemplate();

            // Create request information
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(data.buildFields(), headers);

            ResponseEntity<String> response;
            response = callApi.exchange(serverUri, HttpMethod.POST, entity, String.class);
            strBody = response.getBody();
        } catch (Exception e) {
            logException(e);
            throw new Exception("Can not connect to token server");
        }

        try {
            return parseToken(strBody);
        } catch (Exception e) {
            logException(e);
            throw new Exception("Can not retrieve token");
        }
    }

    /**
     * Redirect to error page
     *
     * @param error   The error content
     * @param details The details or description of error
     * @return
     */
    private String redirectError(String error, String details) {
        return "redirect:/appError?error=" + error + "&details=" + details;
    }

    /**
     * Parse token form data response
     *
     * @param result the response data
     * @return
     */
    private WiseIdPasswordResponse parseToken(String result) {
        try {
            if (result == null || "".equals(result)) {
                return null;
            }
            Gson converter = new Gson();
            return converter.fromJson(result, WiseIdPasswordResponse.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * The base function to log a exception
     *
     * @param ex The exception object
     */
    private void logException(Exception ex) {
        String stackTrace = Arrays.toString(ex.getStackTrace());
        System.out.println("Error: " + ex.getMessage() + "\n Stack trace: " + stackTrace);
    }

}
