package com.example.sv_pro.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sv_pro.Adapter.HomeForumAdapter;
import com.example.sv_pro.Model.Forum;
import com.example.sv_pro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeForumFragment extends Fragment {
    View vRoot;
    RecyclerView rvHomeForum;
    ArrayList<Forum> data;
    HomeForumAdapter adapter;
    Toolbar toolbar;

    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vRoot = inflater.inflate(R.layout.fragment_home_forum, container, false);
        init();
        prepareData();
        return vRoot;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.top_forum_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        try {
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
        catch (Exception e) {
            String err = e.getMessage();
        }

    }


    void init() {
        data = new ArrayList<>();
        rvHomeForum = vRoot.findViewById(R.id.rv_home_forum);
        toolbar = vRoot.findViewById(R.id.toolbar_forum);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        db = FirebaseFirestore.getInstance();
    }

    void prepareData() {
        db.collection("forum_post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        try {
                            Forum item = document.toObject(Forum.class);
                            data.add(item);
                        }catch (Exception e) {
                            String err = e.getMessage();
                        }
                    }
                    configRv();
                }
                else {
                    Toast.makeText(getActivity(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String err = e.getMessage();
            }
        });
    }

    void configRv() {
        adapter = new HomeForumAdapter(data, getActivity(), db);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rvHomeForum.setLayoutManager(linearLayoutManager);
        rvHomeForum.setAdapter(adapter);
    }

}
