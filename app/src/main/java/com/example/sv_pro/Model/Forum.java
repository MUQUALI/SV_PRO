package com.example.sv_pro.Model;

import java.util.ArrayList;

public class Forum {
    private String id;
    private String author;
    private String content;
    private ArrayList<String> pictures;
    private ArrayList<Comment> comments;

    public Forum() {

    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
