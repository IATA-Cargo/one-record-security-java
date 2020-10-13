package app.openidconnect.Model;

public class BaseModel {
	private String error;
	private String errorDetails;
	private String successMessage;
	
	public String getError() {
		return this.error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
	
	public String getErrorDetails() {
		return this.errorDetails;
	}
	
	public void setErrorDetails(String details) {
		this.errorDetails = details;
	}
	
	public String getSuccessMessage() {
		return this.successMessage;
	}
	
	public void setSuccessMessage(String message) {
		this.successMessage = message;
	}
}
