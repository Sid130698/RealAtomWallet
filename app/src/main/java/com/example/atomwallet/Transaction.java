package com.example.atomwallet;

public class Transaction {
    private String sender,reciever,amount;
    private boolean isPurchase;

    public Transaction(){}

    public Transaction(String sender, String reciever, String amount, boolean isPurchase) {
        this.sender = sender;
        this.reciever = reciever;
        this.amount = amount;
        this.isPurchase=isPurchase;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isPurchase() {
        return isPurchase;
    }

    public void setPurchase(boolean purchase) {
        isPurchase = purchase;
    }
}
