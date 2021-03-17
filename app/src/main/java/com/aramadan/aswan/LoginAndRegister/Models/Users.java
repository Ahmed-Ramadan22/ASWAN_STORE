package com.aramadan.aswan.LoginAndRegister.Models;

public class Users {

    private String Email, Password, PhoneNumber, userName, image;

    public Users() {
    }

    public Users(String email, String password, String phoneNumber, String userName, String image) {
        Email = email;
        Password = password;
        PhoneNumber = phoneNumber;
        this.userName = userName;
        this.image = image;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
