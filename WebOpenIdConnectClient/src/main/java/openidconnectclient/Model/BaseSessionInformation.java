package openidconnectclient.Model;

/**
 * Very basic section storage, only keep information of id token
 * @author 
 *
 */
public class BaseSessionInformation {

    private String authenticationCode;
    
    private String idToken;

    private long idTokenExpireAt;

    private String raw;

    private String clientName;

    public void setAuthenticationCode(String code) {
        this.authenticationCode = code;
    }

    public String getAuthenticationCode() {
        return this.authenticationCode;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return this.idToken;
    }

    public void setIdTokenExpireAt(long expiredAt) {
        this.idTokenExpireAt = expiredAt;
    }

    public long getIdTokenExpireAt() {
        return this.idTokenExpireAt;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getRaw() {
        return this.raw;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void clear() {
        this.setAuthenticationCode(null);
        this.setIdTokenExpireAt(0);
        this.setIdToken(null);
    }
}
