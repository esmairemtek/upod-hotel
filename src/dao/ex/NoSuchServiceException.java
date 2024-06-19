package dao.ex;

public class NoSuchServiceException extends Exception {
    private static final String MESSAGE = "No such service found with id ";

    public NoSuchServiceException(int id) {
        super(MESSAGE + id);
    }
}
