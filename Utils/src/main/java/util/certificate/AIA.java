package util.certificate;

public class AIA {

    private String issuer;
    private String ocsp;

    public AIA(String issuer, String ocsp) {
        this.issuer = issuer;
        this.ocsp = ocsp;
    }

    public AIA() {
        this.issuer = "";
        this.ocsp = "";
    }

    public void setIssuer(String value) {
        this.issuer = value;
    }

    public String getIssuer() {
        return this.issuer;
    }

    public void setOcsp(String value) {
        this.ocsp = value;
    }

    public String getOcsp() {
        return this.ocsp;
    }
}
