package model;

import java.sql.Date;
import java.util.List;

public class Reservation {
    private int reservationId;
    private int roomId;
    private List<Customer> customers;
    private Date checkInDate;
    private Date checkOutDate;
    private Date checkedInDate;
    private Date checkedOutDate;
    private List<Service> services;

    public Reservation(int reservationId, int roomId, List<Customer> customers, Date checkInDate, Date checkOutDate, Date checkedInDate, Date checkedOutDate, List<Service> services) {
        this.reservationId = reservationId;
        this.roomId = roomId;
        this.customers = customers;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.checkedInDate = checkedInDate;
        this.checkedOutDate = checkedOutDate;
        this.services = services;
    }

    public int getReservationId() {
        return reservationId;
    }

    public int getRoomId() {
        return roomId;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public Date getCheckedInDate() {
        return checkedInDate;
    }

    public Date getCheckedOutDate() {
        return checkedOutDate;
    }

    public List<Service> getServices() {
        return services;
    }
}
