package com.example.sv_pro.Model;

public class User {
    private String full_name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String type;

    public User(String full_name, String email, String password, String phone, String address, String type) {
        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.type = type;
    }


    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
