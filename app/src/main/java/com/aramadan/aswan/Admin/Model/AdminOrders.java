package com.aramadan.aswan.Admin.Model;

public class AdminOrders {

    private String name, phone, address, state, date, time, totalAmount, image, detailsOrder ;

    public AdminOrders() {
    }

    public AdminOrders(String name, String phone, String address, String state, String date, String time, String totalAmount, String image, String detailsOrder) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.state = state;
        this.date = date;
        this.time = time;
        this.totalAmount = totalAmount;
        this.image = image;
        this.detailsOrder = detailsOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetailsOrder() {
        return detailsOrder;
    }

    public void setDetailsOrder(String detailsOrder) {
        this.detailsOrder = detailsOrder;
    }
}
