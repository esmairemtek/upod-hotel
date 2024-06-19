import services.UserService;
import util.DBInitializer;

public class Main {
    public static void main(String[] args) {
        DBInitializer dbInitializer = new DBInitializer();
        dbInitializer.initializeTables();

        UserService.start();
    }
}