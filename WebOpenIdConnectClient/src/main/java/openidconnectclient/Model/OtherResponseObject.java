package openidconnectclient.Model;

import com.google.gson.annotations.SerializedName;

public class OtherResponseObject {

    @SerializedName("client.cert.subjectDN")
    protected String subjectDN;
    
    @SerializedName("client.cert.issuer")
    protected String issuer;
    
    @SerializedName("client.cert.notbefore")
    protected String notbefore;
    
    @SerializedName("client.cert.statsdesc")
    protected String statsdesc;
    
    @SerializedName("odic.ski")
    protected String ski;
    
    @SerializedName("client.cert.SAN")
    protected String sans;
    
    @SerializedName("client.cert.stats")
    protected String stats;
    
    @SerializedName("client.cert.notafter")
    protected String notafter;
    
    public String getSubjectDN() {
        return subjectDN;
    }
    
    public String getIssuer() {
        return issuer;
    }
    
    public String getNotbefore() {
        return notbefore;
    }
    
    public String getStatsdesc() {
        return statsdesc;
    }
    
    public String getSki() {
        return ski;
    }
    
    public String getStats() {
        return stats;
    }
    
    public String getNotafter() {
        return notafter;
    }
}

