package com.example.sv_pro;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sv_pro.Fragment.HomeFragment;
import com.example.sv_pro.Fragment.NotificationFragment;
import com.example.sv_pro.Fragment.PostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class ThueNhaActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView navBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thue_nha);
        init();
        configNav();

    }

    void init() {
        navBottom = findViewById(R.id.nav_bottom);
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
                displayFragment(new PostFragment());
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
