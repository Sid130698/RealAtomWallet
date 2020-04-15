package com.example.atomwallet;

public class Transaction {
    public String sender,reciever,amount;

    public Transaction(){}

    public Transaction(String sender, String reciever, String amount) {
        this.sender = sender;
        this.reciever = reciever;
        this.amount = amount;
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
}
