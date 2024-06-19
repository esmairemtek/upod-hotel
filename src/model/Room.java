package model;

import java.util.List;

public class Room {
    private int id;
    private String name;
    private int capacity;
    private float price;
    private List<Feature> features;

    public Room(int id, String name, int capacity, float price, List<Feature> features) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.price = price;
        this.features = features;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", price=" + price +
                ", features=" + features +
                '}';
    }
}


