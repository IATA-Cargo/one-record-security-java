package util.openidconnect;

import util.base.Common;

public class ResponseJwksItem {
    private String n;
    private String e;
    private String use;
    
    public String getN() {
        return this.n;
    }
    
    public void setN(String n) {
        this.n = n;
    }
    
    public String getE() {
        return this.e;
    }
    
    public void setE(String e) {
        this.e = e;
    }
    

    private String raw;
    public String getRaw() {
        return raw;
    }

    public static ResponseJwksItem FromJson(String jsonString) {
            
        if (jsonString == null || "".equals(jsonString)) return null;
        
        return Common.fromJson(ResponseJwksItem.class,jsonString);
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }
}