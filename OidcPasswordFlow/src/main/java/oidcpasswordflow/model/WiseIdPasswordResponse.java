package oidcpasswordflow.model;

public class WiseIdPasswordResponse {

    private String access_token;
    private String token_type;
    private int expires_in;
    private String id_token;

    public String getAccessToken(){
        return this.access_token;
    }
    public String getIdToken(){
        return this.id_token;
    }
}
