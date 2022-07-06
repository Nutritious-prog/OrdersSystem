package nutritious.prog.demo.exceptions;

public class ObjectAlreadyExistsException extends RuntimeException {
    public ObjectAlreadyExistsException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
