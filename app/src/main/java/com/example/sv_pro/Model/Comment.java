package com.example.sv_pro.Model;

import com.google.firebase.Timestamp;

public class Comment {
    private String author;
    private String comment;
    private Timestamp time;

    public Comment() {
    }

    public Comment(String author, String comment, Timestamp time) {
        this.author = author;
        this.comment = comment;
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
