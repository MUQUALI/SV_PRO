package com.example.sv_pro.personal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.sv_pro.Adapter.mRentPostAdapter;
import com.example.sv_pro.Model.mRentPost;
import com.example.sv_pro.Network.RetrofitClient;
import com.example.sv_pro.Network.ServiceAPI;
import com.example.sv_pro.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageRentPost extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvMpost;
    mRentPostAdapter adapter;
    ArrayList<mRentPost> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_rent_post);
        init();
        prepareData();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    void init() {
        // setup toolbar
        toolbar = findViewById(R.id.toolbar_mpost);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // UI
        rvMpost = findViewById(R.id.rv_manage_rpost);
    }

    void prepareData() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        // get data from api
        RetrofitClient.getRetrofitClient().create(ServiceAPI.class).getMyRentPost(email).enqueue(new Callback<ArrayList<mRentPost>>() {
            @Override
            public void onResponse(Call<ArrayList<mRentPost>> call, Response<ArrayList<mRentPost>> response) {
                if(response.isSuccessful()) {
                    data.clear();
                    data.addAll(response.body()); // gán giá trị cho araylist ở trên
                    configRV(); // gọi hàm cấu hình recycler view
                }
            }

            @Override
            public void onFailure(Call<ArrayList<mRentPost>> call, Throwable t) {
                Log.d("ERR: ", t.getMessage());
            }
        });
    }

    void configRV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvMpost.setLayoutManager(linearLayoutManager);
        adapter = new mRentPostAdapter(data, this);
        rvMpost.setAdapter(adapter);
    }
}
