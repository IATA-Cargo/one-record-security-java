package openidconnectclient.Model;

public class ApiRequestHeader {

    public ApiRequestHeader(String Authorization) {
        this.authorization = Authorization;
    }

    private String authorization;

    public String getAuthorization() {
        return this.authorization;
    }

    public void setAuthorization(String value) {
        this.authorization = value;
    }

}