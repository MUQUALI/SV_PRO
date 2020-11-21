package com.example.sv_pro.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sv_pro.Adapter.FacultyAdapter;
import com.example.sv_pro.Adapter.NotificationAdapter;
import com.example.sv_pro.Model.Forum;
import com.example.sv_pro.Model.ListNotifi;
import com.example.sv_pro.Model.Notification;
import com.example.sv_pro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    View vRoot;
    ArrayList<Notification> data;
    RecyclerView rvNotification;

    FirebaseFirestore db;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vRoot = inflater.inflate(R.layout.fragment_notification, container, false);
        init();
        prepareData();
        return vRoot;
    }


    void init() {
        data = new ArrayList<>();
        rvNotification = vRoot.findViewById(R.id.rv_notification);
        db = FirebaseFirestore.getInstance();
    }

    void prepareData() {
        db.collection("notifications").document("1605846222974").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    ListNotifi item = task.getResult().toObject(ListNotifi.class);
                    data = item.getNotifis();
                    configRv();
                }
            }
        });

    }

    void configRv() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), VERTICAL, false);

        rvNotification.setLayoutManager(linearLayoutManager); // set layout cho recycler view
        NotificationAdapter adapter = new NotificationAdapter(data, getActivity()); //gán dữ liệu cho adapter , this thay bằng getActivity()

        rvNotification.setAdapter(adapter);
    }
}
