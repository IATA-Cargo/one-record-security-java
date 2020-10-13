package util.openidconnect;

public class TokenException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenException(String message) {
        super(message);
    }
	
	public TokenException(String message, Throwable error) {
	    super(message, error);
	}
    
}