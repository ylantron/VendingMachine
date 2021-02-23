package com.example.distributoreautomatico.business;

public class Drink {
    private int id;
    private String name;
    private int quantity;
    private float cost;

    public Drink(String name, int quantity, float cost) {
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
    }

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

    public int getQuantity() {
        return quantity;
    }

    @SuppressWarnings("unused")
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getCost() {
        return cost;
    }

    @SuppressWarnings("unused")
    public void setCost(float cost) {
        this.cost = cost;
    }
}
