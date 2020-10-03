package com.example.sv_pro.Network;

import com.example.sv_pro.Model.faculty;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceAPI { // Khai báo các đường dẫn api lấy dữ liệu

    @GET("faculties")
    Call<ArrayList<faculty>> getListFaculty(); //  truyền vào cuối đường dẫn "faculties" sẽ trả về ArrayList<faculty>
    // faculty trong Package model. sau này tạo lớp đối tượng để trong lớp đó hết
}
