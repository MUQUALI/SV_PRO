package com.example.sv_pro.personal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.sv_pro.Adapter.PostAdapter;
import com.example.sv_pro.Model.post;
import com.example.sv_pro.Network.RetrofitClient;
import com.example.sv_pro.Network.ServiceAPI;
import com.example.sv_pro.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class ViewPostActivity extends AppCompatActivity {
    RecyclerView rvPost;
    ArrayList<post> data = new ArrayList<>();
    PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        init();
        prepareData();
    }

    void init() {
        rvPost = findViewById(R.id.rv_post);
    }

    void prepareData() {
        Intent intent = getIntent();
        String fid = intent.getStringExtra("fid");
        RetrofitClient.getRetrofitClient().create(ServiceAPI.class).getListPost(fid).enqueue(new Callback<ArrayList<post>>() {
            @Override
            public void onResponse(Call<ArrayList<post>> call, Response<ArrayList<post>> response) {
                // response ở đây trả về 1 arraylist của các post
                if(response.isSuccessful()) {
                    data.clear();
                    data.addAll(response.body()); // gán giá trị cho araylist ở trên
                    configRV(); // gọi hàm cấu hình recycler view
                }
            }

            @Override
            public void onFailure(Call<ArrayList<post>> call, Throwable t) {
                Log.d("ERR: ", t.getMessage());
            }
        });
    }

    void configRV () {
        // Xác định chiều hiển thị của recycler view - ở đây là VERTICAL ( dọc ), thay this thành getActivity()
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, VERTICAL, false);

        rvPost.setLayoutManager(linearLayoutManager); // set layout cho recycler view
        adapter = new PostAdapter(data, this); //gán dữ liệu cho adapter , this thay bằng getActivity()

        rvPost.setAdapter(adapter);
    }
}