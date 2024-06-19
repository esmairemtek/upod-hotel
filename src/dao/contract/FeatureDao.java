package dao.contract;

import dao.ex.DaoException;
import dao.ex.NoSuchFeatureException;
import model.Feature;

import java.util.List;

public interface FeatureDao {
    Feature findById(Integer id) throws DaoException, NoSuchFeatureException;
    List<Feature> findAll() throws DaoException;
}
