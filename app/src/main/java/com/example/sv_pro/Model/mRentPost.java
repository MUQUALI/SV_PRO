package com.example.sv_pro.Model;

import com.google.firebase.Timestamp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class mRentPost {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("faculty")
    @Expose
    private String faculty;
    @SerializedName("facultyID")
    @Expose
    private String facultyID;

    @SerializedName("eletricPrice")
    @Expose
    private double eletricPrice;
    @SerializedName("waterPrice")
    @Expose
    private double waterPrice;
    @SerializedName("rentPrice")
    @Expose
    private double rentPrice;
    @SerializedName("postDate")
    @Expose
    private Timestamp postDate;
    @SerializedName("pictures")
    @Expose
    ArrayList<String> pictures;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getAddress() {
        return address;
    }

    public String getArea() {
        return area;
    }

    public String getContent() {
        return content;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getFacultyID() {
        return facultyID;
    }

    public double getEletricPrice() {
        return eletricPrice;
    }

    public double getWaterPrice() {
        return waterPrice;
    }

    public double getRentPrice() {
        return rentPrice;
    }

    public Timestamp getPostDate() {
        return postDate;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }
}
