package model;

public class Invoice {
    private int invoiceId;
    private int reservationId;
    private float totalCost;

    public Invoice(int invoiceId, int reservationId, float totalCost) {
        this.invoiceId = invoiceId;
        this.reservationId = reservationId;
        this.totalCost = totalCost;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public float getTotalCost() {
        return totalCost;
    }
}
