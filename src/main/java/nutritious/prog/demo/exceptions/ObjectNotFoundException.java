package nutritious.prog.demo.exceptions;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
