package dao.ex;

public class NoSuchFeatureException extends Exception{
    private static final String MESSAGE = "No such service found with id ";

    public NoSuchFeatureException(int id) {
        super(MESSAGE + id);
    }
}
