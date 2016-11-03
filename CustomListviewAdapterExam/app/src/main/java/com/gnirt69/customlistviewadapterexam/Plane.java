package com.gnirt69.customlistviewadapterexam;


public class Plane {
    private int id;
    private String name;
    private int price;
    private String destination;

    //Constructor

    public Plane(int id, String name, int price, String destination) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.destination = destination;
    }

    //Setter, getter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
