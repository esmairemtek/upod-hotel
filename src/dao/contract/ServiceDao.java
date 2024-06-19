package dao.contract;

import dao.ex.DaoException;
import dao.ex.NoSuchServiceException;
import model.Service;

import java.util.List;

public interface ServiceDao {
    public Service findById(Integer id) throws DaoException, NoSuchServiceException;
    public List<Service> findAll() throws DaoException;
}
