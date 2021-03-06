package com.example.sv_pro.Network;

import com.example.sv_pro.Model.faculty;
import com.example.sv_pro.Model.mRentPost;
import com.example.sv_pro.Model.post;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServiceAPI { // Khai báo các đường dẫn api lấy dữ liệu

    @GET("faculties")
    Call<ArrayList<faculty>> getListFaculty(); //  truyền vào cuối đường dẫn "faculties" sẽ trả về ArrayList<faculty>
    // faculty trong Package model. sau này tạo lớp đối tượng để trong lớp đó hết

    @GET("mypost")
    Call<ArrayList<mRentPost>> getMyRentPost(@Query("email") String email);

    @GET("post")
    Call<ArrayList<post>> getListPost(@Query("fid") String fid);
}
