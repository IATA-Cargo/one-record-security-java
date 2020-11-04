package openidconnectclient.Model;

public class HomeModel extends BaseModel {

    private String code;
    private String idToken;
    private String verifyResult;
    private String verifyUri;
    private String apiRequestHeaders;
    private String apiResponse;
    private String verifyMessage;
    private String resultData;
    private String authenticatedSubject;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIdToken() {
        return this.idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getVerifyResult() {
        return this.verifyResult;
    }

    public void setVerifyResult(String verifyResult) {
        this.verifyResult = verifyResult;
    }

    public String getVerifyUri() {
        return this.verifyUri;
    }

    public void setVerifyUri(String verifyUri) {
        this.verifyUri = verifyUri;
    }

    public String getVerifyMessage() {
        return this.verifyMessage;
    }

    public void setVerifyMessage(String verifyMessage) {
        this.verifyMessage = verifyMessage;
    }

    public String getApiRequestHeaders() {
        return this.apiRequestHeaders;
    }

    public void setApiRequestHeaders(String apiRequestHeaders) {
        this.apiRequestHeaders = apiRequestHeaders;
    }

    public String getApiResponse() {
        return this.apiResponse;
    }

    public void setApiResponse(String apiResponse) {
        this.apiResponse = apiResponse;
    }

    public String getResultData() {
        return this.resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public String getAuthenticatedSubject() {
        return this.authenticatedSubject;
    }

    public void setAuthenticatedSubject(String authenticatedSubject) {
        this.authenticatedSubject = authenticatedSubject;
    }

}
