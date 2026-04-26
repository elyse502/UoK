package com.userapp.models;

public class User {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String country;

    public User() {}

    public User(int id, String fullName, String email, String phone, String country) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.country = country;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}