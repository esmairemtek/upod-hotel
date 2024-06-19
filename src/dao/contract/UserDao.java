package dao.contract;

import dao.ex.DaoException;
import dao.ex.NoSuchUserException;
import model.User;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserDao extends AutoCloseable {
    void save(User user) throws DaoException, NoSuchAlgorithmException;

    void update(User user) throws DaoException;

    boolean deleteById(Integer id) throws DaoException;

    User findById(Integer id) throws NoSuchUserException, DaoException;

    User findByEmail(String email) throws NoSuchUserException, DaoException;

    User findByAuth(String email, String password) throws NoSuchAlgorithmException, DaoException;

    List<User> findByUserType(String userType) throws DaoException;
}
