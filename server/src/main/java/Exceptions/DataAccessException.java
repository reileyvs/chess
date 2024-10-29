package Exceptions;

/**
 * Indicates there was an error connecting to the database (specifically 500 exceptions)
 */
public class DataAccessException extends Exception {
    public DataAccessException(String message) {
        super(message);
    }
}
