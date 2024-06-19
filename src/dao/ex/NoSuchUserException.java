package dao.ex;

public class NoSuchUserException extends Exception {
    private static final String MESSAGE = "No such user found with id ";

    public NoSuchUserException(int id) {
        super(MESSAGE + id);
    }

    public NoSuchUserException(String email) {
        super(MESSAGE + email);
    }
}
