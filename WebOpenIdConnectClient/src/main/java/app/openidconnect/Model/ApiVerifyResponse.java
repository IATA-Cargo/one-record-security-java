package app.openidconnect.Model;

import java.util.List;
import util.base.ApiResponseBase;

public class ApiVerifyResponse extends ApiResponseBase {
	
	private ApiAuthenticatedSubject authenticatedSubject;
	private ApiResult result;
	
	public ApiAuthenticatedSubject getAuthenticatedSubject() {
		return authenticatedSubject;
	}
	
	public void setAuthenticatedSubject(ApiAuthenticatedSubject data) {
		this.authenticatedSubject = data;
	}
	
	public ApiResult getResult() {
		return result;
	}
	
	public void setResult(ApiResult data) {
		this.result = data;
	}
	
	class ApiResult {
		
		private int productID;
		private long price;
		private int quantity;
		
		public int getProductID() {
			return this.productID;
		}
		
		public void setProductID(int data) {
			this.productID = data;
		}
		
		public long getPrice() {
			return this.price;
		}
		
		public void setPrice(long data) {
			this.price = data;
		}
		
		public int getQuantity() {
			return this.quantity;
		}
		
		public void setQuantity(int data) {
			this.quantity = data;
		}
		
	}
	
	class ApiAuthenticatedSubject {
		
		private String type;
		private String subjectDN;
		private String validFrom;
		private String validTo;
		private String issuerDN;
		private String lastAuthenticatedAt;
		private String validationService;
		private List<String> sans;
		
		public String getType() {
			return this.type;
		}
		
		public void setType(String data) {
			this.type = data;
		}
		
		public String getSubjectDN() {
			return this.subjectDN;
		}
		
		public void setSubjectDN(String data) {
			this.subjectDN = data;
		}
		
		public String getValidFrom() {
			return this.validFrom;
		}
		
		public void setValidFrom(String data) {
			this.validFrom = data;
		}
		
		public String getValidTo() {
			return this.validTo;
		}
		
		public void setValidTo(String data) {
			this.validTo = data;
		}
		
		public String getIssuerDN() {
			return this.issuerDN;
		}
		
		public void setIssuerDN(String data) {
			this.issuerDN = data;
		}
		
		public String getLastAuthenticatedAt() {
			return this.lastAuthenticatedAt;
		}
		
		public void setLastAuthenticatedAt(String data) {
			this.lastAuthenticatedAt = data;
		}
		
		public String getValidationService() {
			return this.validationService;
		}
		
		public void setValidationService(String data) {
			this.validationService = data;
		}
		
		public List<String> getSans() {
			return this.sans;
		}
		
		public void setSans(List<String> data) {
			this.sans = data;
		}
	}
}
