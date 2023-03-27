package com.example.internshiptask;

public class User {
    private String name;
    private String phoneNumber;
    private String emailAdd;
    private String birthDate;
    private String imageUri;

    public User() {}

    public User(String name, String phoneNumber, String emailAdd, String birthDate, String imageUri) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAdd = emailAdd;
        this.birthDate = birthDate;
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAdd() {
        return emailAdd;
    }

    public void setEmailAdd(String emailAdd) {
        this.emailAdd = emailAdd;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
