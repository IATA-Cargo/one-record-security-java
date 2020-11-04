package util.base;

import java.util.Arrays;

public class UtilException extends Exception {

    private static final long serialVersionUID = 4982189741872316591L;

    public UtilException(String message, Throwable error) {
        super(message, error);
    }

}
