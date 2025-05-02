//Book.java
package com.example.group3textbookapp;

public class Book {
    final private int id;
    final private String title;
    final private String seller;
    final private int copies;
    final private double price;
    final private String bankInfo;

    public Book(int id, String title, String seller, int copies, double price, String bankInfo) {
        this.id = id;
        this.title = title;
        this.seller = seller;
        this.copies = copies;
        this.price = price;
        this.bankInfo = bankInfo;
    }

    public Book(String title, String seller, int copies, double price, String bankInfo) {
        this(-1, title, seller, copies, price, bankInfo);
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getSeller() { return seller; }
    public int getCopies() { return copies; }
    public double getPrice() { return price; }
    public String getBankInfo() { return bankInfo; }

    public String getSellerName() {
        return seller;
    }
}



