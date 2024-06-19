package model;

public class Feature {
    private int id;
    private String name;

    public Feature(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Feature(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Feature{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
