package com.example.sv_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.sv_pro.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    ArrayList<itemRule> spDataItem = new ArrayList<>();
    Spinner spType;
    EditText edtEmail, edtPassword, edtFullName, edtPhone, edtAddress;
    Button btnRegister, btnCancel;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        loadSpinner();
        handleEvent();
    }

    void init() {
        // prepare data spinner
        spDataItem.add(new itemRule("chu-tro", "Chủ trọ"));
        spDataItem.add(new itemRule("sinh-vien", "Sinh viên"));
        // init view
        spType = findViewById(R.id.sp_type);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtFullName = findViewById(R.id.edt_fullname);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);
        btnRegister = findViewById(R.id.btn_register);
        btnCancel = findViewById(R.id.btn_cancel);
        // fire base
        db = FirebaseFirestore.getInstance();
    }

    void loadSpinner() {
        ArrayAdapter<itemRule> spinnerAdapter = new ArrayAdapter<itemRule>(getApplicationContext(), android.R.layout.simple_spinner_item, spDataItem);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(spinnerAdapter);
    }

    void handleEvent() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    boolean checkEmptyInput() {
        if(edtAddress.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(edtEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(edtPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(edtPhone.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập SĐT", Toast.LENGTH_SHORT).show();
            return true;
        }

        else if(edtFullName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Họ tên", Toast.LENGTH_SHORT).show();
            return true;
        }
        return  false;
    }

    User prepareData() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String full_name = edtFullName.getText().toString();
        String address = edtAddress.getText().toString();
        String phone = edtPhone.getText().toString();
        itemRule spItem = (itemRule) spType.getSelectedItem();

        return new User(full_name, email, password, phone, address, spItem.type);
    }

    void register() {
        if(checkEmptyInput())
            return;

        User userRegister = prepareData();

        db.collection("users").add(userRegister).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Có lỗi gì đó", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class itemRule {
        private String type;
        private String name;

        itemRule(String type, String name) {
            this.type = type;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
