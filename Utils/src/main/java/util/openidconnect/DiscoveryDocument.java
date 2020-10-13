package util.openidconnect;

/**
 * The details of configuration Oidc server
 * @author 
 *
 */
public class DiscoveryDocument {
	
	private String issuer;
	private String jwks_uri;
	private String authorization_endpoint;
	private String token_endpoint;
    private String raw;
    
    public String getIssuer() {
		return this.issuer;
	}
    
    public String getAuthorizationEndpoint() {
		return this.authorization_endpoint;
	}
    
    public String getTokenEndpoint() {
		return this.token_endpoint;
	}
    
    public String getJwksUri() {
		return this.jwks_uri;
	}
    
    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
    
}