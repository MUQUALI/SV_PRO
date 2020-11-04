package com.example.sv_pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sv_pro.Fragment.HomeFragment;
import com.example.sv_pro.Fragment.NotificationFragment;
import com.example.sv_pro.Fragment.AddPostFragment;
import com.example.sv_pro.personal.ManageRentPost;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ThueNhaActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView navBottom;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thue_nha);
        init();
        configNav();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.my_post:
                Intent i = new Intent(ThueNhaActivity.this, ManageRentPost.class);
                startActivity(i);
                break;
        }
        return true;
    }

    void init() {
        navBottom = findViewById(R.id.nav_bottom);
        toolbar = findViewById(R.id.toolbar_rent);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void configNav() {
        navBottom.setOnNavigationItemSelectedListener(this);
        displayFragment(new HomeFragment());
        navBottom.setSelectedItemId(R.id.navigation_home);
    }

    // hiển thị 1 fragment
    void displayFragment(Fragment fragment) {
        // 1. Khởi tạo fragmentManager quản lý fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 2. khởi tạo 1 fragmentTransaction để chuyển đổi giữa các fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // 3. xác định fragment nào sẽ hiển thị qua biến fragment truyền vào
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        // 4. thực thi
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                displayFragment(new HomeFragment());
                break;
            case R.id.navigation_post:
                displayFragment(new AddPostFragment());
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
