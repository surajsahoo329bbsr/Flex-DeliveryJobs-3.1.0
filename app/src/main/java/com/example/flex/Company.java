package com.example.flex;

import androidx.annotation.NonNull;

public class Company {

    private int imageId;
    private String company;
    private String timings;
    private String address;
    private String paymentStatus, transactionDate, transactionMoney;

    public Company(int imageId, String company, String timings, String address, String paymentStatus, String transactionDate, String transactionMoney) {
        this.imageId = imageId;
        this.company = company;
        this.timings = timings;
        this.address = address;
        this.paymentStatus = paymentStatus;
        this.transactionDate = transactionDate;
        this.transactionMoney = transactionMoney;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionMoney() {
        return transactionMoney;
    }

    public void setTransactionMoney(String transactionMoney) {
        this.transactionMoney = transactionMoney;
    }


    @NonNull
    @Override
    public String toString() {
        return company+ "\n"+timings+"\n" + address;
    }
}