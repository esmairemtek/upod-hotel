package dao.impl;

import dao.contract.FeatureDao;
import dao.ex.DaoException;
import dao.ex.NoSuchFeatureException;
import model.Feature;
import util.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeatureDaoJdbc implements FeatureDao {
    private Connection conn;
    private static final String RETRIEVE_BY_ID_QUERY = "SELECT * FROM Feature WHERE id = ?";
    private static final String RETRIEVE_ALL_QUERY = "SELECT * FROM Feature";
    private Connection getConnection() {
        conn = DBUtils.getConnection();
        return conn;
    }

    @Override
    public Feature findById(Integer id) throws DaoException, NoSuchFeatureException {
        Feature feature = null;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(RETRIEVE_BY_ID_QUERY)) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int idRetrieved = resultSet.getInt("id");
                String name = resultSet.getString("name");
                feature = new Feature(idRetrieved, name);
            }
            if (feature == null) {
                throw new NoSuchFeatureException(id);
            }

        } catch (SQLException e) {
            throw new DaoException("Error retrieving feature in DAO.", e);
        }

        return feature;
    }

    @Override
    public List<Feature> findAll() throws DaoException {
        List<Feature> features = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(RETRIEVE_ALL_QUERY)) {
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int idRetrieved = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Feature feature = new Feature(idRetrieved, name);
                features.add(feature);
            }

        } catch (SQLException e) {
            throw new DaoException("Error retrieving all in DAO.", e);
        }
        return features;
    }
}
