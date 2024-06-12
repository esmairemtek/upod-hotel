package model;

public class Room {
    private int id;
    private String name;
    private int capacity;
    private int price;
    private String features;

    public Room(int id, String name, int capacity, int price, String features) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.price = price;
        this.features = features;
    }
}
