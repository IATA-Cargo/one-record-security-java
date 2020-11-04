package Dummy1API1.config;

import java.util.Set;

public class FieldPatchException extends RuntimeException {

    private static final long serialVersionUID = 4982189741872316595L;

    public FieldPatchException(Set<String> keys) {
        super("Field " + keys.toString() + " update is not allow.");
    }

}