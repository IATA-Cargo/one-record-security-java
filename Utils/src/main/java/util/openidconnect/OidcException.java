package util.openidconnect;

public class OidcException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public OidcException(String message) {
        super(message);
    }

    public OidcException(String message, Throwable error) {
        super(message, error);
    }
}