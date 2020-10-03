package com.example.sv_pro.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class faculty { // Dùng để hứng dữ liệu trả về từ API chuyển sang kiểu đối tượng

    @SerializedName("id")
    @Expose
    // để nó tìm trường tương ứng trong json trả về, gán vào thuộc tính này
    private String id;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("faculty")
    @Expose
    private String faculty;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("district")
    @Expose
    private String district;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
