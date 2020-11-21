package com.example.sv_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.sv_pro.Fragment.AddPostForum;
import com.example.sv_pro.Fragment.AddPostFragment;
import com.example.sv_pro.Fragment.HomeForumFragment;
import com.example.sv_pro.Fragment.HomeFragment;
import com.example.sv_pro.Fragment.NotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HocTapActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView navBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoc_tap);
        init();
        configNav();
    }

    void init() {
        navBottom = findViewById(R.id.nav_bottom);
        //toolbar = findViewById(R.id.toolbar_rent);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void configNav() {
        navBottom.setOnNavigationItemSelectedListener(this);

        displayFragment(new HomeForumFragment());
        navBottom.setSelectedItemId(R.id.navigation_home);
    }

    void displayFragment(Fragment fragment) {
        // 1. Khởi tạo fragmentManager quản lý fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 2. khởi tạo 1 fragmentTransaction để chuyển đổi giữa các fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // 3. xác định fragment nào sẽ hiển thị qua biến fragment truyền vào
        fragmentTransaction.replace(R.id.view_container, fragment);
        // 4. thực thi
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                displayFragment(new HomeForumFragment());
                break;
            case R.id.navigation_post:
                displayFragment(new AddPostForum());
                break;
            case R.id.navigation_notifications:
                displayFragment(new NotificationFragment());
                break;
            default:
                //displayFragment(new HomeFragment());
                break;
        }
        return true;
    }

}
