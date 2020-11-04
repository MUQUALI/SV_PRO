package com.example.sv_pro.Model;

import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RentPost {
    private String id;
    private String author;
    private String address;
    private String area;
    private String content;
    private String faculty;
    private String facultyID;

    private double eletricPrice;
    private double waterPrice;
    private double rentPrice;
    private Date postDate;

    List<String> pictures;

    public RentPost(String author, String address, String area, String content, String faculty, String facultyID,
                    double eletricPrice, double waterPrice, double rentPrice, Date postDate, List<String> pictures) {
        this.author = author;
        this.address = address;
        this.area = area;
        this.content = content;
        this.faculty = faculty;
        this.facultyID = facultyID;
        this.eletricPrice = eletricPrice;
        this.waterPrice = waterPrice;
        this.rentPrice = rentPrice;
        this.postDate = postDate;
        this.pictures = pictures;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getFacultyID() {
        return facultyID;
    }

    public void setFacultyID(String facultyID) {
        this.facultyID = facultyID;
    }

    public double getEletricPrice() {
        return eletricPrice;
    }

    public void setEletricPrice(double eletricPrice) {
        this.eletricPrice = eletricPrice;
    }

    public double getWaterPrice() {
        return waterPrice;
    }

    public void setWaterPrice(double waterPrice) {
        this.waterPrice = waterPrice;
    }

    public double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(double rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Date getPostDate() {
        return postDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
}
