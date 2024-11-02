package exceptions;

public class RecordException extends DataAccessException {
    public static final int NUM = 500;
    public RecordException(String message) {
        super(message);
    }
}
