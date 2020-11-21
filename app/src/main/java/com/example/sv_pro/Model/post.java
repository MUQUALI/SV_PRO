package com.example.sv_pro.Model;

import com.google.firebase.Timestamp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class post
{
    @SerializedName("id")
    @Expose
    private  String id;

    @SerializedName("author")
    @Expose
    private  String author;

    @SerializedName("faculty")
    @Expose
    private String faculty;

    @SerializedName("facultyID")
    @Expose
    private String facultyID;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("rentPrice")
    @Expose
    private int rentPrice;

    @SerializedName("eletricPrice")
    @Expose
    private int eletricPrice;

    @SerializedName("waterPrice")
    @Expose
    private int waterPrice;

    @SerializedName("postDate")
    @Expose
    private Timestamp postDate;

    public Timestamp getPostDate() {
        return postDate;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    @SerializedName("pictures")
    @Expose
    private ArrayList<String> pictures;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getFacultyID() {
        return facultyID;
    }

    public String getContent() {
        return content;
    }

    public String getAddress() {
        return address;
    }

    public int getRentPrice() {
        return rentPrice;
    }

    public int getElectricPrice() {
        return eletricPrice;
    }

    public int getWaterPrice() {
        return waterPrice;
    }
}
