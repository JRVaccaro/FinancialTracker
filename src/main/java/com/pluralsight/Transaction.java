package com.pluralsight;

import java.time.LocalDate;

public class Transaction {
    //Fields
private LocalDate date;
private String time;
private String description;
String Vendor;
double amount;

    //Constructor
    public Transaction(LocalDate date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        Vendor = vendor;
        this.amount = amount;
    }
    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return Vendor;
    }

    public void setVendor(String vendor) {
        Vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
