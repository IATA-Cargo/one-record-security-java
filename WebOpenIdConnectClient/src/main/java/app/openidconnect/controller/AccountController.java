/**
 * File AccountController.java
 * 
 */
package app.openidconnect.controller;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import app.openidconnect.Model.BaseSessionInformation;


@Controller
@SessionAttributes("baseSessionInformation")
public class AccountController extends BaseController  {
	
	public AccountController(BaseSessionInformation baseSessionInformation,
			ClientRegistrationRepository clientRegistrationRepository) {
		super(baseSessionInformation, clientRegistrationRepository);
		
		
	}

	
	@RequestMapping(value = { "/signout" }, method = RequestMethod.GET)
	public String signout() {
		
		if (baseSession != null) {
			baseSession.clear();
		}
		return "redirect:/login";
	}
	
}
