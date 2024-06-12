package dao;

import model.Customer;

import java.util.List;

public interface CustomerDao {
    void save(Customer customer);
    void update(Customer customer);
    void deleteById(Integer id);
    Customer findById(Integer id);
    List<Customer> findAll();

}
