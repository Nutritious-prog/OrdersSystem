package nutritious.prog.demo.exceptions;

public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
