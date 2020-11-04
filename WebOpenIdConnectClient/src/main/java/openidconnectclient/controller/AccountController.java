/**
 * File AccountController.java
 */
package openidconnectclient.controller;

import openidconnectclient.Model.BaseSessionInformation;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@SessionAttributes("baseSessionInformation")
public class AccountController {

    private BaseSessionInformation baseSession;
    private ClientRegistrationRepository clientRegistration;

    public AccountController(BaseSessionInformation baseSessionInformation,
                             ClientRegistrationRepository clientRegistrationRepository) {
        this.baseSession = baseSessionInformation;
        this.clientRegistration = clientRegistrationRepository;
    }

    /**
     * Sign out page, process user sign out and show page
     * @return
     */
    @RequestMapping(value = {"/signout"}, method = RequestMethod.GET)
    public String signOut() {

        if (baseSession != null) {
            baseSession.clear();
        }
        return "redirect:/login";
    }

}
