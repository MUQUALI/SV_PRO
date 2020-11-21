package com.example.sv_pro.Fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sv_pro.Adapter.PickImgForumAdapter;
import com.example.sv_pro.Model.Comment;
import com.example.sv_pro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostForum extends Fragment {
    private static final int PICK_IMAGES_CODE = 0;
    View vRoot;
    //
    ViewPager vpPostForum;
    Button btnAddImage, btnRemoveImage, btnPost;
    EditText edtContent;
    //data
    ArrayList<Uri> imageUris;
    ArrayList<String> downloadUrl;
    PickImgForumAdapter adapter;
    String pathSaveImage = "";
    //counter image upload
    int counterImage = 0;
    // firebase
    StorageReference mStorage;
    StorageReference mStoragePost;
    FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vRoot = inflater.inflate(R.layout.fragment_add_post_forum, container, false);
        init();
        handlerEvent();
        return vRoot;
    }

    void init() {
        vpPostForum = vRoot.findViewById(R.id.vp_add_image_forum);
        btnAddImage = vRoot.findViewById(R.id.btn_add_image);
        btnRemoveImage = vRoot.findViewById(R.id.btn_remove_img);
        btnPost = vRoot.findViewById(R.id.btn_post);
        edtContent = vRoot.findViewById(R.id.edt_content);
        // data
        imageUris = new ArrayList<>();
        downloadUrl = new ArrayList<>();
        //firebase
        mStorage = FirebaseStorage.getInstance().getReference();
        mStoragePost = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
    }

    void handlerEvent() {
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkPermission()) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }
                pickImagesIntent();
            }
        });

        btnRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUris.size() != 0) {
                    int pos = vpPostForum.getCurrentItem();
                    imageUris.remove(pos);
                    adapter.notifyDataSetChanged();
                    vpPostForum.setAdapter(adapter);
                    vpPostForum.setCurrentItem(pos);
                }
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Xác Nhận");
                builder.setMessage("Bạn chắc muốn đăng chứ ?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(imageUris.size() > 0) {
                                    saveImageUrl();
                                }
                                else {
                                    postExecute(new ArrayList<String>());
                                }
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

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
            }
            else {
                //picked single image
                Uri imageUri = data.getData();
                imageUris.add(imageUri);
            }
            adapter = new PickImgForumAdapter(getContext(), imageUris);
            vpPostForum.setAdapter(adapter);
        }
    }

    boolean checkPermission() {
        if(ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            return false;
        }
        else {
            return true;
        }
    }

    String getEmail() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        return email;
    }

    void saveImageUrl() {
        String email = getEmail();
        Calendar time = Calendar.getInstance();
        pathSaveImage = "post_forum/" + email + "_" + time.getTimeInMillis();
        for (int i = 0; i < imageUris.size() ; i++) {
            String path = pathSaveImage + "/" + email + time.getTimeInMillis() + i + ".png";
            StorageReference fileToUpload = mStorage.child(path);
            fileToUpload.putFile(imageUris.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    getDownloadUrl(fileToUpload);
                    // phải đặt trong này nếu ko sẽ lỗi hàm này chạy trc khi storage đc tạo ra
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Lỗi Lưu ảnh", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    void getDownloadUrl(StorageReference fileToUpload ) {
        try {
            fileToUpload.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()) {
                        counterImage++;
                        downloadUrl.add(task.getResult().toString());
                        if(counterImage == imageUris.size()) {
                            postExecute(downloadUrl);
                        }
                    }
                    else {
                        fileToUpload.delete();
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String err = e.getMessage();
                }
            });
        }
        catch (Exception ex) {
            String err = ex.getMessage();
        }
    }

    void postExecute(ArrayList<String> pictures) {
        if(edtContent.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Bạn phải nhập nội dung", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            Calendar time = Calendar.getInstance();
            HashMap document = new HashMap();
            document.put("id", time.getTimeInMillis() + "");
            document.put("author", getEmail());
            document.put("content", edtContent.getText().toString());
            document.put("pictures", pictures);
            document.put("comments", new ArrayList<Comment>());

            db.collection("forum_post").add(document).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getActivity(), "Đăng Bài Thành Công", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String err = e.getMessage();
                    Toast.makeText(getContext(), "Có lỗi gì đó: " + err, Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception ex) {
            String err = ex.getMessage();
            Toast.makeText(getContext(), "Có lỗi gì đó: " + err, Toast.LENGTH_SHORT).show();
        }
    }

}
