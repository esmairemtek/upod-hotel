package dao.contract;

import dao.ex.DaoException;
import dao.ex.NoSuchUserException;
import model.User;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserDao extends AutoCloseable {
    void save(User user) throws DaoException, NoSuchAlgorithmException;

    void update(User user) throws DaoException;

    void deleteById(Integer id);

    User findById(Integer id) throws NoSuchUserException, DaoException;

    List<User> findByUserType(String userType) throws DaoException;
}
