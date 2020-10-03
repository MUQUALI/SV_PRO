package com.example.sv_pro.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit; // Lớp lấy dữ liệu từ API

    private RetrofitClient() {

    }

    public static Retrofit getRetrofitClient() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://us-central1-sv-pro-api-7a165.cloudfunctions.net/app/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
