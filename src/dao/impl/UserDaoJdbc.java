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
    private static final String SAVE_QUERY = "INSERT INTO USER VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE USER SET EMAIL = ? WHERE ID = ?"; // update email
    private static final String DELETE_QUERY = "DELETE FROM USER WHERE ID = ?";
    private static final String RETRIEVE_QUERY = "SELECT * FROM USER WHERE ID = ?";
    private static final String RETRIEVE_ALL_QUERY = "SELECT * FROM USER";

    @Override
    public void save(User user) throws DaoException, NoSuchAlgorithmException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SAVE_QUERY)) {
            conn.setAutoCommit(false);
            pstmt.setInt(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, SecurityUtils.hashPassword(user.getPassword()));
            pstmt.setString(5, user instanceof Manager ? "manager" : "customer");

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
    public void deleteById(Integer id) {
        //todo
    }

    @Override
    public User findById(Integer id) throws NoSuchUserException, DaoException {
        User user = null;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(RETRIEVE_QUERY)) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int idRetrieved = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                String email = resultSet.getString("EMAIL");
                String password = resultSet.getString("PASSWORD");
                
                String userType = resultSet.getNString("USER_TYPE");
                if ("manager".equals(userType)) {
                    user = new Manager(idRetrieved,name,email,password);
                } else if ("customer".equals(userType)) {
                    user = new Customer(idRetrieved,name,email,password);
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
    public List<User> findByUserType(String userType) throws DaoException {
        List<User> users = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(RETRIEVE_ALL_QUERY)) {
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int idRetrieved = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                String email = resultSet.getString("EMAIL");
                String password = resultSet.getString("PASSWORD");
                User user = null;
                if ("manager".equals(userType)) {
                    user = new Manager(idRetrieved,name,email,password);
                } else if ("customer".equals(userType)) {
                    user = new Customer(idRetrieved,name,email,password);
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
