package util;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {

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
            pstmt.setString(1, "Lilith Iyapo");
            pstmt.setString(2, "admin@upodhotel.com");
            pstmt.setString(3, SecurityUtils.hashPassword("Admin123"));
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
    }

    private void createRoomTable() {
        String sql = "CREATE TABLE Room (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "capacity INT NOT NULL," +
                "price DECIMAL(10,2)" +
                ");";

        executeUpdate(sql, "Room table created successfully.");
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
        String[][] roomFeatures = {

        };
        executeBatchInsert(sql, roomFeatures);
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
