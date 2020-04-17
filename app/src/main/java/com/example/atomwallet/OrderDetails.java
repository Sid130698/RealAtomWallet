package com.example.atomwallet;

public class OrderDetails {
    private  String itemID;
    private int quantity;

    public OrderDetails()
    {
        //Empty constructor required
    }

    public OrderDetails(String itemID, int quantity) {
        this.itemID = itemID;
        this.quantity = quantity;
    }
    public OrderDetails(OrderDetails d) {
        this.itemID = d.itemID;
        this.quantity = d.quantity;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
