package com.example.sv_pro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.sv_pro.Model.User;
import com.example.sv_pro.Model.faculty;
import com.example.sv_pro.Network.RetrofitClient;
import com.example.sv_pro.Network.ServiceAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    LinearLayout homeItem, learnItem;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        chose();

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
            case R.id.logout:
                sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
        }
        return true;
    }

    void init() {
        homeItem = findViewById(R.id.home_menu);
        learnItem = findViewById(R.id.learn_menu);

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    void chose() {
        homeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ThueNhaActivity.class);
                startActivity(intent);
            }
        });

        learnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HocTapActivity.class);
                startActivity(intent);
            }
        });
    }


}
