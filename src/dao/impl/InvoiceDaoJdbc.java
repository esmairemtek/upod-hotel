package dao.impl;

import dao.contract.InvoiceDao;
import model.Invoice;
import util.DBUtils;

import java.sql.Connection;
import java.util.List;

public class InvoiceDaoJdbc implements InvoiceDao {

    private Connection conn;
    private Connection getConnection() {
        conn = DBUtils.getConnection();
        return conn;
    }

    private static final String SAVE_QUERY = "INSERT INTO INVOICE VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY =  "UPDATE INVOICE SET TOTAL_COST = ? WHERE INVOICE_ID = ?";
    private static final String DELETE_QUERY = "DELETE FROM INVOICE WHERE INVOICE_ID = ?";
    private static final String RETRIEVE_QUERY = "SELECT * FROM INVOICE WHERE INVOICE_ID = "
    @Override
    public void save(Invoice invoice) {

    }

    @Override
    public void update(Invoice invoice) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void findById(Integer id) {

    }

    @Override
    public List<Invoice> findAll() {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
