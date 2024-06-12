package model;

import java.sql.Date;

public class Customer {
private int id;
private String name;
private String email;
private String phone;
private Date birthDate;

    public Customer(int id, String name, String email, String phone, Date birthDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
    }
}
