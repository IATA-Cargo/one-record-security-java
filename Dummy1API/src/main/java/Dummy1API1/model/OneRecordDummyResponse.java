package Dummy1API1.model;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import util.base.ApiResponseBase;
import util.base.Common;

public class OneRecordDummyResponse extends ApiResponseBase {
	
	private OneRecordDummyData result;
	private OneRecordTLSID authenticatedSubject;
    
	public OneRecordDummyResponse() {
		this.result = new OneRecordDummyData();
		this.authenticatedSubject = new OneRecordTLSID();
	}
	
	public OneRecordDummyResponse(X509Certificate cert) {
		
		this.result = new OneRecordDummyData();
		
		this.authenticatedSubject = new OneRecordTLSID();
		
		if (cert == null) return;
		
		this.authenticatedSubject.type = "User Certificate";
		this.authenticatedSubject.subjectDN = cert.getSubjectDN() + "";
		this.authenticatedSubject.validFrom = "" + cert.getNotBefore();
		this.authenticatedSubject.validTo = "" + cert.getNotAfter();
		this.authenticatedSubject.issuerDN = cert.getIssuerDN() + "";
		this.authenticatedSubject.lastAuthenticatedAt = Common.getDatetimeString();
		//this.oneRecordIDList = CertValidator.ParseOneRecordIDs(cert);
		this.authenticatedSubject.oneRecordIDList = new ArrayList<String>();
	}
	
	public OneRecordDummyData getResult() {
		return this.result;
	}
	public void setResult(OneRecordDummyData value) {
		this.result = value;
	}
	
	public OneRecordTLSID getAuthenticatedSubject() {
		return this.authenticatedSubject;
	}
	public void setAuthenticatedSubject(OneRecordTLSID value) {
		this.authenticatedSubject = value;
	}
	
	class OneRecordDummyData
    {
		public OneRecordDummyData() {
			this.productID = 1;
			this.price = 1000;
			this.quantity = 10;
		}
		
		public int productID;
		public int price;
		public int quantity;
		
		public int getProductID() {
			return this.productID;
		}
		public void setProductID(int value) {
			this.productID = value;
		}
		
		public int getPrice() {
			return this.price;
		}
		public void setPrice(int value) {
			this.price = value;
		}
		
		public int getQuantity() {
			return this.quantity;
		}
		public void setQuantity(int value) {
			this.quantity = value;
		}
    }
	
	class OneRecordTLSID
    {
		public OneRecordTLSID() {
			this.validationService = "IATA Trust Platform";
		}
		
		public String type;
		public String subjectDN;
		public String validFrom;
		public String validTo;
		public String issuerDN;
		public String lastAuthenticatedAt;
		public String validationService;
		public List<String> oneRecordIDList;
		
		public String getType() {
			return this.type;
		}
		public void setType(String value) {
			this.type = value;
		}
		
		public String getSubjectDN() {
			return this.subjectDN;
		}
		public void setSubjectDN(String value) {
			this.subjectDN = value;
		}
		
		public String getValidFrom() {
			return this.validFrom;
		}
		public void setValidFrom(String value) {
			this.validFrom = value;
		}
		
		public String getValidTo() {
			return this.validTo;
		}
		public void setValidTo(String value) {
			this.validTo = value;
		}
		
		public String getIssuerDN() {
			return this.issuerDN;
		}
		public void setIssuerDN(String value) {
			this.issuerDN = value;
		}
		
		public String getLastAuthenticatedAt() {
			return this.lastAuthenticatedAt;
		}
		public void setLastAuthenticatedAt(String value) {
			this.lastAuthenticatedAt = value;
		}
		
		public String getValidationService() {
			return this.validationService;
		}
		public void setValidationService(String value) {
			this.validationService = value;
		}
		
		public List<String> getOneRecordIDList() {
			return this.oneRecordIDList;
		}
		public void setOneRecordIDList(List<String> value) {
			this.oneRecordIDList = value;
		}
    }
}
