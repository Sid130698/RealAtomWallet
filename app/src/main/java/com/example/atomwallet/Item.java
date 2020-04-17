package com.example.atomwallet;

public class Item {
    private String name,id;
    private double price;
    private int quantity;

    public Item(){
        //Empty constructor required
    }

    public Item(String name,String id, double price, int quantity) {
        this.name = name;
        this.id=id;
        this.price = price;
        this.quantity = quantity;
    }

    public Item(Item item)
    {
        this.name=item.name;
        this.id=item.id;
        this.price=item.price;
        this.quantity=item.quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
