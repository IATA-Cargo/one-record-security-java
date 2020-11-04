package Dummy1API1.config;

/**
 * @author  
 */

public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 9089011574944193000L;

    public BadRequestException(String message) {
        super(message);
    }

}