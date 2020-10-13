package util.openidconnect;

import java.util.List;

public class ResponseJwks {
    private List<ResponseJwksItem> keys;
    private String raw;
    
    public List<ResponseJwksItem> getKeys() {
        return this.keys;
    }
    
    public String getRaw() {
        return this.raw;
    }
    
    public void setRaw(String raw) {
        this.raw = raw;
    }
    
    public ResponseJwksItem getLastSignKey() {
        if (this.keys == null) return null;
        if (this.keys.isEmpty()) return null;
        ResponseJwksItem result = null;
        ResponseJwksItem temp = null;
        
        for (int i = 0; i < this.keys.size(); i++) {
            temp = this.keys.get(i);
            if (temp == null) continue;
            if ("sig".equals(temp.getUse())) {
                result = temp;
            }
        }
        return result;
    }  
}