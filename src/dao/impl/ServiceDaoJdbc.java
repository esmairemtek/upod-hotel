package dao.impl;

import dao.contract.ServiceDao;
import dao.ex.DaoException;
import dao.ex.NoSuchServiceException;
import model.Service;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDaoJdbc implements ServiceDao {

    private Connection conn;

    private Connection getConnection() {
        conn = DBUtils.getConnection();
        return conn;
    }

    private static final String RETRIEVE_BY_ID_QUERY = "SELECT * FROM Service WHERE id = ?";
    private static final String RETRIEVE_ALL_QUERY = "SELECT * FROM Service";

    @Override
    public Service findById(Integer id) throws DaoException, NoSuchServiceException {
        Service service = null;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(RETRIEVE_BY_ID_QUERY)) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int idRetrieved = resultSet.getInt("id");
                String name = resultSet.getString("name");
                float price = resultSet.getFloat("price");
                service = new Service(idRetrieved, name, price);
            }
            if (service == null) {
                throw new NoSuchServiceException(id);
            }

        } catch (SQLException e) {
            throw new DaoException("Error retrieving service in DAO.", e);
        }

        return service;
    }

    @Override
    public List<Service> findAll() throws DaoException {
        List<Service> services = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(RETRIEVE_ALL_QUERY)) {
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int idRetrieved = resultSet.getInt("id");
                String name = resultSet.getString("name");
                float price = resultSet.getFloat("price");
                Service service = new Service(idRetrieved, name, price);
                services.add(service);
            }

        } catch (SQLException e) {
            throw new DaoException("Error retrieving all in DAO.", e);
        }
        return services;
    }

}
