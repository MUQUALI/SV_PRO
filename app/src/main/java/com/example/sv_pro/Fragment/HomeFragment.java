package com.example.sv_pro.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sv_pro.Adapter.FacultyAdapter;
import com.example.sv_pro.Model.faculty;
import com.example.sv_pro.Network.RetrofitClient;
import com.example.sv_pro.Network.ServiceAPI;
import com.example.sv_pro.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    RecyclerView rvFaculty;
    ArrayList<faculty> data = new ArrayList<>();
    FacultyAdapter adapter;

    View vRoot; // Khai báo view root để trỏ về thằng cha chứa fragment này (  là thuenhaActivity)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vRoot = inflater.inflate(R.layout.fragment_home, container, false); // Gán View root

        init();
        prepareData();
        return vRoot;
    }

    void init() {
        rvFaculty = vRoot.findViewById(R.id.rv_faculty);
    }

    void prepareData() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        // Gọi đến class RetrofitClient để lấy dữ liệu từ API ( code trong phần network )
        RetrofitClient.getRetrofitClient().create(ServiceAPI.class).getListFaculty().enqueue(new Callback<ArrayList<faculty>>() {
            @Override
            public void onResponse(Call<ArrayList<faculty>> call, Response<ArrayList<faculty>> response) {
                dialog.dismiss();
                // response ở đây trả về 1 arraylist của các faculty
                if(response.isSuccessful()) {
                    data.clear();
                    data.addAll(response.body()); // gán giá trị cho araylist ở trên
                    configRV(); // gọi hàm cấu hình recycler view
                }
            }

            @Override
            public void onFailure(Call<ArrayList<faculty>> call, Throwable t) {
                dialog.dismiss();
                Log.d("ERR: ", t.getMessage());
            }
        });
    }

    void configRV() {
        // Xác định chiều hiển thị của recycler view - ở đây là VERTICAL ( dọc ), thay this thành getActivity()
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), VERTICAL, false);

        rvFaculty.setLayoutManager(linearLayoutManager); // set layout cho recycler view
        adapter = new FacultyAdapter(data, getActivity()); //gán dữ liệu cho adapter , this thay bằng getActivity()

        rvFaculty.setAdapter(adapter);
    }
}
