package util.openidconnect;

/**
 * The information in jwt token
 * @author 
 *
 */
public class JwtTokenInfo {

    /** The "iss" (issuer) claim identifies the principal that issued the JWT */
    private String iss;
    private long exp;
    private long auth_time;
    private String raw;
    private String headersRaw;
    private String claimsRaw;
    private String signatureRaw;

    public String getClaimISS() {
        return this.iss;
    }

    public void setClaimISS(String claimIss) {
        this.iss = claimIss;
    }
    
    public long getExpired() {
        return this.exp;
    }

    public void setExpired(long expire) {
        this.exp = expire;
    }

    public long getAuthenTime() {
        return this.auth_time;
    }

    public void setAuthenTime(long authTime) {
        this.auth_time = authTime;
    }

    public String getRaw() {
        return raw;
    }
    
    public void setRaw(String token) {
        this.raw = token;
    }
    
    public String getHeadersRaw() {
        return headersRaw;
    }
    
    public void setHeadersRaw(String header) {
        this.headersRaw = header;
    }
    
    public String getClaimsRaw() {
        return claimsRaw;
    }
    
    public void setClaimsRaw(String claims) {
        this.claimsRaw = claims;
    }
    
    public String getSignatureRaw() {
        return signatureRaw;
    }
    
    public void setSignatureRaw(String signature) {
        this.signatureRaw = signature;
    }    
}