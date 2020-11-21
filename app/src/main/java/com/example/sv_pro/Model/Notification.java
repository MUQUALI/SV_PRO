package com.example.sv_pro.Model;

import com.google.firebase.Timestamp;

public class Notification {
    private Timestamp time;
    private String author;

    public Notification(Timestamp time, String author) {
        this.time = time;
        this.author = author;
    }

    public Notification() {
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
