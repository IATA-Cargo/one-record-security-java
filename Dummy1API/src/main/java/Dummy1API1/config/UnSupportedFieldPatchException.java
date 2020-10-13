package Dummy1API1.config;

import java.util.Set;

@SuppressWarnings("serial")
public class UnSupportedFieldPatchException extends RuntimeException {

    public UnSupportedFieldPatchException(Set<String> keys) {
        super("Field " + keys.toString() + " update is not allow.");
    }

}