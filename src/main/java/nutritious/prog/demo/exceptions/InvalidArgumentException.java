package nutritious.prog.demo.exceptions;

public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String errorMessage) {
        super(errorMessage);
    }
}
