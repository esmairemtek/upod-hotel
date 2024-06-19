package dao.impl;

import dao.contract.UserDao;
import dao.ex.DaoException;
import dao.ex.NoSuchUserException;
import model.Customer;
import model.Manager;
import model.User;
import util.DBUtils;
import util.SecurityUtils;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJdbc implements UserDao {
    private Connection conn;

    private Connection getConnection() {
        conn = DBUtils.getConnection();
        return conn;
    }

    private static final String SAVE_QUERY = "INSERT INTO User (name, email, password, user_type) VALUES (?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE User SET email = ? WHERE id = ?"; // update email
    private static final String DELETE_QUERY = "DELETE FROM User WHERE id = ?";
    private static final String RETRIEVE_QUERY_BY_ID = "SELECT * FROM User WHERE id = ?";
    private static final String RETRIEVE_QUERY_BY_EMAIL = "SELECT * FROM User WHERE email = ?";
    private static final String RETRIEVE_QUERY_BY_USERTYPE = "SELECT * FROM User WHERE user_type = ?";
    private static final String RETRIEVE_QUERY_BY_AUTH = "SELECT * FROM User WHERE email = ? AND password = ?";

    @Override
    public void save(User user) throws DaoException, NoSuchAlgorithmException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SAVE_QUERY)) {
            conn.setAutoCommit(false);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, SecurityUtils.hashPassword(user.getPassword()));
            pstmt.setString(4, user instanceof Manager ? "manager" : "customer");

            if (pstmt.executeUpdate() != 1) {
                throw new DaoException("Error in saving user.");
            }
            conn.commit();
        } catch (SQLException e) {
            throw new DaoException("Error adding user in DAO.", e);
        }
    }

    @Override
    public void update(User user) throws DaoException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_QUERY)) {
            conn.setAutoCommit(false);
            pstmt.setString(1, user.getEmail());
            pstmt.setInt(3, user.getId());

            if (pstmt.executeUpdate() != 1) {
                throw new DaoException("Error in updating user.");
            }
            conn.commit();

        } catch (SQLException e) {
            throw new DaoException("Error updating user in DAO.", e);
        }
    }

    @Override
    public boolean deleteById(Integer id) throws DaoException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_QUERY)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DaoException("Error deleting user: " + e.getMessage());
        }
    }

    @Override
    public User findById(Integer id) throws NoSuchUserException, DaoException {
        User user = null;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(RETRIEVE_QUERY_BY_ID)) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int idRetrieved = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                String userType = resultSet.getNString("user_type");
                if ("manager".equals(userType)) {
                    user = new Manager(idRetrieved, name, email, password);
                } else if ("customer".equals(userType)) {
                    user = new Customer(idRetrieved, name, email, password);
                }
            }
            if (user == null) {
                throw new NoSuchUserException(id);
            }
        } catch (SQLException e) {
            throw new DaoException("Error retrieving user in DAO.", e);
        }
        return user;
    }

    @Override
    public User findByEmail(String email) throws NoSuchUserException, DaoException {
        User user = null;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(RETRIEVE_QUERY_BY_EMAIL)) {
            pstmt.setString(1, email);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String emailRetrieved = resultSet.getString("email");
                String password = resultSet.getString("password");

                String userType = resultSet.getNString("user_type");
                if ("manager".equals(userType)) {
                    user = new Manager(id, name, emailRetrieved, password);
                } else if ("customer".equals(userType)) {
                    user = new Customer(id, name, emailRetrieved, password);
                }
            }
            if (user == null) {
                throw new NoSuchUserException(email);
            }
        } catch (SQLException e) {
            throw new DaoException("Error retrieving user in DAO.", e);
        }
        return user;
    }

    @Override
    public User findByAuth(String email, String password) throws NoSuchAlgorithmException, DaoException {
        User user = null;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(RETRIEVE_QUERY_BY_AUTH)) {
            pstmt.setString(1, email);
            pstmt.setString(2, SecurityUtils.hashPassword(password));
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String emailRetrieved = resultSet.getString("email");
                String passwordRetrieved = resultSet.getString("password");
                String userType = resultSet.getNString("user_type");
                if ("manager".equals(userType)) {
                    user = new Manager(id, name, emailRetrieved, passwordRetrieved);
                } else if ("customer".equals(userType)) {
                    user = new Customer(id, name, emailRetrieved, passwordRetrieved);
                }
            }

            if (user == null) {
                throw new NoSuchUserException(email);
            }
        } catch (SQLException e) {
            throw new DaoException("Error retrieving user in DAO.", e);
        } catch (NoSuchUserException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public List<User> findByUserType(String userType) throws DaoException {
        List<User> users = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(RETRIEVE_QUERY_BY_USERTYPE)) {
            pstmt.setString(1, userType);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int idRetrieved = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                String email = resultSet.getString("EMAIL");
                String password = resultSet.getString("PASSWORD");
                User user = null;
                if ("manager".equals(userType)) {
                    user = new Manager(idRetrieved, name, email, password);
                    users.add(user);
                } else if ("customer".equals(userType)) {
                    user = new Customer(idRetrieved, name, email, password);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error retrieving all in DAO.", e);
        }
        return users;
    }

    @Override
    public void close() throws Exception {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Exception closing connection: " + e);
        }

    }
}
