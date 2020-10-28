package oidcpasswordflow.controller;

import com.google.gson.Gson;
import oidcpasswordflow.model.WiseIdPasswordResponse;
import java.util.Arrays;

public class BaseController {

	public BaseController() {
	}

	/** Redirect to error page */
	protected String redirectError(String error, String details) {
		return "redirect:/appError?error=" + error + "&details=" + details;
	}


	WiseIdPasswordResponse parseToken(String result){
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

	void logException(Exception ex){
		String stackTrace = Arrays.toString(ex.getStackTrace());
		System.out.println("Error: " +  ex.getMessage() + "\n Stack trace: " + stackTrace);
	}
	/*
	void logMessage(String message){
		System.out.println(message);
	}
	*/
}
