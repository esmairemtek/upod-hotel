package dao.contract;

import model.Invoice;

import java.util.List;

public interface InvoiceDao extends AutoCloseable {
    void save(Invoice invoice);
    void update(Invoice invoice);
    void deleteById(Integer id);
    void findByInvoiceId(Integer id);
    void findByCustomerId(Integer id);
    List<Invoice> findAll();
}
