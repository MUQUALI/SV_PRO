package com.example.sv_pro.Fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.sv_pro.Model.RentPost;
import com.example.sv_pro.Model.faculty;
import com.example.sv_pro.Network.RetrofitClient;
import com.example.sv_pro.Network.ServiceAPI;
import com.example.sv_pro.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostFragment extends Fragment {
    private static final int PICK_IMAGES_CODE = 0;
    View vRoot;
    Spinner spFaculty, spDistrict;
    ArrayList<faculty> data = new ArrayList<>();
    ArrayAdapter<faculty> spinnerAdapter;
    Button btnAddImage, btnNext, btnPrevious, btnPost, btnRemoveImage;
    ImageSwitcher imageIs;
    ArrayList<Uri> imageUris;
    StorageReference mStorage;
    FirebaseFirestore db;
    ArrayList<String> listImageUrl;
    Handler handler;
    EditText edtAddress, edtRentPrice, edtElectricPrice, edtWaterPrice, edtContent;

    int position = 0; //  dùng để chuyển mấy cái ảnh trong ImageSwitcher

    public AddPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vRoot =  inflater.inflate(R.layout.fragment_add_post, container, false);
        init();
        //formatToPrice();
        prepareData();
        handlerEvents();
        return vRoot;
    }

    void init() {
        //UI
        spFaculty = vRoot.findViewById(R.id.sp_faculty);
        spDistrict = vRoot.findViewById(R.id.sp_district);
        btnNext = vRoot.findViewById(R.id.btn_next);
        btnPrevious = vRoot.findViewById(R.id.btn_pre);
        btnAddImage = vRoot.findViewById(R.id.btn_add_image);
        btnRemoveImage = vRoot.findViewById(R.id.btn_remove_img);
        btnPost = vRoot.findViewById(R.id.btn_post);
        // Edit text
        edtAddress = vRoot.findViewById(R.id.edt_address);
        edtRentPrice = vRoot.findViewById(R.id.edt_rent_price);
        edtElectricPrice = vRoot.findViewById(R.id.edt_electric_price);
        edtWaterPrice = vRoot.findViewById(R.id.edt_water_price);
        edtContent = vRoot.findViewById(R.id.edt_content);
        // Image
        imageIs = vRoot.findViewById(R.id.images_is);
        imageUris = new ArrayList<>();
        listImageUrl = new ArrayList<>();

        // firebase
        mStorage = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

    }

    void loadSpinner() {
        spinnerAdapter = new ArrayAdapter<faculty>(getActivity(),
                android.R.layout.simple_spinner_item, data);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFaculty.setAdapter(spinnerAdapter);
    }

    void prepareData() {

        RetrofitClient.getRetrofitClient().create(ServiceAPI.class).getListFaculty().enqueue(new Callback<ArrayList<faculty>>() {
            @Override
            public void onResponse(Call<ArrayList<faculty>> call, Response<ArrayList<faculty>> response) {

                if(response.isSuccessful()) {
                    data.clear();
                    data.addAll(response.body()); // gán giá trị cho araylist ở trên
                    loadSpinner(); //
                }
            }

            @Override
            public void onFailure(Call<ArrayList<faculty>> call, Throwable t) {
                Log.d("ERR: ", t.getMessage());
            }
        });
    }

    void handlerEvents() {
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }
                pickImagesIntent();
            }
        });


        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postExecute();
            }
        });

        imageIs.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getActivity());
                return imageView;
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position > 0) {
                    position--;
                    imageIs.setImageURI(imageUris.get(position));
                } else {
                    Toast.makeText(getActivity(), "Không còn ảnh để lùi nữa", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position < imageUris.size() - 1) {
                    position++;
                    imageIs.setImageURI(imageUris.get(position));
                } else {
                    Toast.makeText(getActivity(), "Không còn ảnh để tiến nữa", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUris.size() != 0) {
                    imageUris.remove(position);
                }

                if(position == imageUris.size() && position != 0) {
                    // để = image.size do khi remove đã làm size - 1
                    position--;
                }
                if(imageUris.size() != 0)
                    imageIs.setImageURI(imageUris.get(position));
                else
                    imageIs.setImageURI(null);
            }
        });
    }

//    void formatToPrice() {
//        edtRentPrice.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//
//            }
//
//        });
//    }

    void pickImagesIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image(s)"), PICK_IMAGES_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGES_CODE && resultCode == Activity.RESULT_OK) {
            if(data.getClipData() != null) {
                //picked multiple image
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUris.add(imageUri);
                }

                imageIs.setImageURI(imageUris.get(0));
            }
            else {
                //picked single image
                Uri imageUri = data.getData();
                imageUris.add(imageUri);
                imageIs.setImageURI(imageUris.get(0));
            }
        }
    }

    void uploadImages(String postID) {
        Calendar time = Calendar.getInstance();
        for (int i = 0; i < imageUris.size() ; i++) {
            String path = "images/hoannc_" + time.getTimeInMillis() + i + ".png";
            StorageReference fileToUpload = mStorage.child(path);
            fileToUpload.putFile(imageUris.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Lấy ra đường dẫn để lưu
                    fileToUpload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Làm thế này là bởi vì gọi call back trong 1 vòng for, không thể lấy listImageUrl ra bên ngoài
                            String Uri = String.valueOf(uri);
                            listImageUrl.add(Uri);
                            Map<String, Object> data = new HashMap<>();
                            data.put("pictures", listImageUrl);
                            // giải pháp dùng api của firebase tuy nhiên sẽ hơi chậm nếu triển trai thực tế
                            db.collection("rent_post").document(postID).set(data, SetOptions.merge());
                        }
                    });
                }

            });
        }
    }

    boolean checkEmptyInput() {
        if(edtAddress.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(edtRentPrice.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập giá thuê", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(edtElectricPrice.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập giá điện", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(edtWaterPrice.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập giá nước", Toast.LENGTH_SHORT).show();
            return true;
        }

        else if(edtContent.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập nội dung chính", Toast.LENGTH_SHORT).show();
            return true;
        }
        return  false;
    }

    RentPost preparePostData() {
        // get author
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String author = sharedPreferences.getString("email", "");
                //
        faculty item = (faculty) spFaculty.getSelectedItem();
        //
        String address = edtAddress.getText().toString();
        String faculty = item.getFaculty();
        String facultyID = item.getId();
        String content = edtContent.getText().toString();
        String area = (String)spDistrict.getSelectedItem();
        //
        double rentPrice = Double.valueOf(edtRentPrice.getText().toString());
        double electricPrice = Double.valueOf(edtElectricPrice.getText().toString());
        double waterPrice = Double.valueOf(edtWaterPrice.getText().toString());
        Date postDate = Calendar.getInstance().getTime();
        List<String> pictures = new ArrayList<>();
        return new RentPost(author, address, area, content, faculty, facultyID, electricPrice, waterPrice, rentPrice, postDate, pictures);
    }

    void postExecute() {
        if(checkEmptyInput())
            return;
        RentPost post = preparePostData();

        db.collection("rent_post").add(post)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                uploadImages(documentReference.getId());
                Toast.makeText(getActivity(), "Đăng Bài Thành Công", Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Đăng Bài Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
