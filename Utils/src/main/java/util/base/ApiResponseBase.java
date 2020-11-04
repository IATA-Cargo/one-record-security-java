package util.base;

public class ApiResponseBase {

    private int code = 0;
    private String message = "OK";
    private String details;
    private long timestamp = Common.getLongTimeStamp();

    public int getCode() {
        return this.code;
    }
    public void setCode(int value) {
        this.code = value;
    }

    public String getMessage() {
        return this.message;
    }
    public void setMessage(String value) {
        this.message = value;
    }

    public String getDetails() {
        return this.details;
    }
    public void setDetails(String value) {
        this.details = value;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
    public void setTimestamp(long value) {
        this.timestamp = value;
    }
}
