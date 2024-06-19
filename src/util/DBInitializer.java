package util;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class DBInitializer {
    private static final Properties properties = new Properties();

    static {
        FileReader in = null;
        String filePath = "src/resources/data.properties";
        try {
            in = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            System.out.println("File is not found: " + filePath);
        }
        try {
            properties.load(in);
        } catch (IOException e) {
            System.out.println("Problem with IO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void initializeTables() {
        createUserTable();
        createFeatureTable();
        createRoomTable();
        createReservationTable();
        createInvoiceTable();
        createServiceTable();
        createReservationServiceLinksTable();
        createRoomFeatureLinksTable();
    }

    private void createUserTable() {
        String sql = "CREATE TABLE User (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) NOT NULL UNIQUE," +
                "password VARCHAR(255) NOT NULL," +
                "user_type ENUM('manager', 'customer') NOT NULL" +
                ");";

        executeUpdate(sql, "User table created successfully.");
        insertInitialManager();
    }

    private void insertInitialManager() {
        String sql = "INSERT INTO User (name, email, password, user_type) VALUES (?,?,?,?)";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, properties.getProperty("managerName"));
            pstmt.setString(2, properties.getProperty("managerMail"));
            pstmt.setString(3, SecurityUtils.hashPassword(properties.getProperty("managerPassword")));
            pstmt.setString(4, "manager");

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Initial manager inserted successfully.");
            } else {
                System.out.println("Failed to insert initial manager.");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting initial manager: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void createServiceTable() {
        String sql = "CREATE TABLE Service (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) UNIQUE NOT NULL," +
                "price DECIMAL(10,2)" +
                ");";

        executeUpdate(sql, "Service table created successfully.");
        insertInitialServices();
    }

    private void insertInitialServices() {
        String sql = "INSERT INTO Service (name, price) VALUES (?, ?)";
        String[][] services = {
                {"Spa", "100.00"},
                {"In-Room Dining", "30.00"},
                {"Fitness Center", "25.00"},
                {"Pool Access", "15.00"},
                {"Airport Shuttle", "50.00"},
                {"Valet Parking", "20.00"},
                {"Laundry Service", "20.00"},
                {"Buffet Breakfast", "20.00"}
        };
        executeBatchInsert(sql, services);
    }

    private void createRoomTable() {
        String sql = "CREATE TABLE Room (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL UNIQUE," +
                "capacity INT NOT NULL," +
                "price DECIMAL(10,2)" +
                ");";

        executeUpdate(sql, "Room table created successfully.");
        insertInitialRooms();
    }

    private void insertInitialRooms() {
        String sql = "INSERT INTO Room (name, capacity, price) VALUES (?,?,?)";
        String[][] rooms = {
                {"Single Eco 1", "1", "75.00"},
                {"Single Eco 2", "1", "75.00"},
                {"Double Eco 1", "2", "120.00"},
                {"Double Eco 2", "2", "120.00"},
                {"Double Eco 3", "2", "120.00"},
                {"Double Deluxe 1", "2", "150.00"},
                {"Double Deluxe 2", "2", "150.00"},
                {"Triple Eco 1", "3", "165.00"},
                {"Triple Eco 2", "3", "165.00"},
                {"Triple Eco 3", "3", "165.00"},
                {"Triple Deluxe 1", "3", "180.00"},
                {"Triple Deluxe 2", "3", "180.00"},
                {"Family Suite", "4", "200.00"}
        };
        executeBatchInsert(sql, rooms);
    }

    private void createReservationTable() {
        String sql = "CREATE TABLE Reservation (" +
                "reservation_id INT AUTO_INCREMENT PRIMARY KEY," +
                "room_id INT," +
                "user_id INT," +
                "check_in_date DATE," +
                "check_out_date DATE," +
                "checked_in_date DATE," +
                "checked_out_date DATE," +
                "FOREIGN KEY (room_id) REFERENCES Room(id) ON DELETE SET NULL," +
                "FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE SET NULL" +
                ");";

        executeUpdate(sql, "Reservation table created successfully.");
    }

    private void createInvoiceTable() {
        String sql = "CREATE TABLE Invoice (" +
                "invoice_id INT AUTO_INCREMENT PRIMARY KEY," +
                "reservation_id INT," +
                "total_cost DECIMAL(10,2)," +
                "FOREIGN KEY (reservation_id) REFERENCES Reservation(reservation_id) ON DELETE CASCADE" +
                ");";

        executeUpdate(sql, "Invoice table created successfully.");
    }

    private void createFeatureTable() {
        String sql = "CREATE TABLE Feature (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL UNIQUE" +
                ");";

        executeUpdate(sql, "Feature table created successfully.");
        insertInitialFeatures();
    }

    private void insertInitialFeatures() {
        String[] features = {"Jacuzzi", "Safe Deposit", "Sea View", "Forest View", "Balcony", "Minibar"};

        try (Connection conn = DBUtils.getConnection();
             Statement stmt = conn.createStatement()) {
            for (String feature : features) {
                String sql = "INSERT INTO Feature (name) VALUES ('" + feature + "');";
                stmt.execute(sql);
            }
            System.out.println("Initial features inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting initial features: " + e.getMessage());
        }
    }

    private void createRoomFeatureLinksTable() {
        String sql = "CREATE TABLE Room_Feature_Links (" +
                "room_id INT," +
                "feature_id INT," +
                "PRIMARY KEY (room_id, feature_id)," +
                "FOREIGN KEY (room_id) REFERENCES Room(id) ON DELETE CASCADE," +
                "FOREIGN KEY (feature_id) REFERENCES Feature(id) ON DELETE RESTRICT" +
                ");";

        executeUpdate(sql, "Room_Feature_Links table created successfully.");
        insertInitialRoomFeatureLinks();
    }

    private void insertInitialRoomFeatureLinks() {
        String sql = "INSERT INTO Room_Feature_Links (room_id, feature_id) VALUES (?, ?)";
        String[][] roomFeatureLinks = {
                {"1", "2"},  // Single Eco 1 with Safe Deposit
                {"2", "2"},  // Single Eco 2 with Safe Deposit
                {"3", "3"},  // Double Eco 1 with Sea View
                {"4", "3"},  // Double Eco 2 with Sea View
                {"5", "4"},  // Double Eco 3 with Forest View
                {"6", "1"},  // Double Deluxe 1 with Jacuzzi
                {"6", "6"},  // Double Deluxe 1 with Minibar
                {"7", "1"},  // Double Deluxe 2 with Jacuzzi
                {"7", "6"},  // Double Deluxe 2 with Minibar
                {"8", "5"},  // Triple Eco 1 with Balcony
                {"9", "5"},  // Triple Eco 2 with Balcony
                {"10", "5"}, // Triple Eco 3 with Balcony
                {"11", "1"}, // Triple Deluxe 1 with Jacuzzi
                {"11", "6"}, // Triple Deluxe 1 with Minibar
                {"12", "1"}, // Triple Deluxe 2 with Jacuzzi
                {"12", "6"}, // Triple Deluxe 2 with Minibar
                {"13", "1"}, // Family Suite with Jacuzzi
                {"13", "6"}, // Family Suite with Minibar
                {"13", "5"}  // Family Suite with Balcony
        };
        executeBatchInsert(sql, roomFeatureLinks);
    }

    private void createReservationServiceLinksTable() {
        String sql = "CREATE TABLE Reservation_Service_Links (" +
                "reservation_id INT," +
                "service_id INT," +
                "PRIMARY KEY (reservation_id, service_id)," +
                "FOREIGN KEY (reservation_id) REFERENCES Reservation(reservation_id) ON DELETE CASCADE," +
                "FOREIGN KEY (service_id) REFERENCES Service(id) ON DELETE RESTRICT" +
                ");";
        executeUpdate(sql, "Reservation_Service_Links table created successfully.");
    }
    private void executeUpdate(String sql, String successMessage) {
        try (Connection conn = DBUtils.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println(successMessage);
        } catch (SQLException e) {
            System.out.println("Error executing SQL: " + e.getMessage());
        }
    }

    private void executeBatchInsert(String sql, String[][] values) {
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] value : values) {
                for (int i = 0; i < value.length; i++) {
                    pstmt.setString(i+1, value[i]);
                }
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Batch insert completed successfully.");
        } catch (SQLException e) {
            System.out.println("Error executing batch insert: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DBInitializer dbInitializer = new DBInitializer();
        dbInitializer.initializeTables();
    }
}
