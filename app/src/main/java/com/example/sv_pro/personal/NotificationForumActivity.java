package com.example.sv_pro.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sv_pro.Adapter.HomeForumAdapter;
import com.example.sv_pro.Adapter.ListCommentAdapter;
import com.example.sv_pro.Adapter.mPostImgAdapter;
import com.example.sv_pro.Model.Comment;
import com.example.sv_pro.Model.Forum;
import com.example.sv_pro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class NotificationForumActivity extends AppCompatActivity {
    TextView tvAuthor, tvCountComment, tvContent;
    ViewPager vpForum;
    RecyclerView rvListComment;
    EditText edtComment;
    ImageView btnSendComment;

    Forum data;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_forum);
        init();
        prepareData();

        vpForum.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationForumActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Download");
                builder.setMessage("Bạn có muốn download về máy?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int pos = vpForum.getCurrentItem();
                                String downloadUrl = data.getPictures().get(pos);
                                Toast.makeText(getApplicationContext(), "ok: downloadUrl", Toast.LENGTH_SHORT).show();
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

    void init() {
        data = new Forum();
        db = FirebaseFirestore.getInstance();
        //view
        tvAuthor = findViewById(R.id.tv_author);
        tvCountComment = findViewById(R.id.tv_count_comment);
        tvContent = findViewById(R.id.tv_content);
        vpForum = findViewById(R.id.vp_forum);
        rvListComment = findViewById(R.id.rv_forum_comment);
        edtComment = findViewById(R.id.edt_comment);
        btnSendComment = findViewById(R.id.send_comment);
    }


    void prepareData() {
        String id = getIntent().getStringExtra("id");
        db.collection("forum_post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        try {
                            Forum item = document.toObject(Forum.class);
                            if(item.getId().equals(id)) {
                                data = item;
                                break;
                            }
                        }catch (Exception e) {
                            String err = e.getMessage();
                        }
                    }
                    fillDataView(id);
                }
                else {
                    Toast.makeText(NotificationForumActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String err = e.getMessage();
            }
        });
    }

    void fillDataView(String postID) {
        ListCommentAdapter commentAdapter = new ListCommentAdapter(data.getComments(), NotificationForumActivity.this, postID);

        tvAuthor.setText(data.getAuthor());
        tvCountComment.setText(data.getComments().size() + "");
        tvContent.setText(data.getContent());
        // viewpager
        if(data.getPictures().size() > 0) {
            mPostImgAdapter adapter = new mPostImgAdapter(NotificationForumActivity.this, data.getPictures());
            vpForum.setAdapter(adapter);
        }
        else {
            vpForum.setVisibility(View.GONE);
        }
        // list comment
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationForumActivity.this, VERTICAL, false);
        rvListComment.setLayoutManager(linearLayoutManager);
        rvListComment.setAdapter(commentAdapter);

        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtComment.getText().toString().isEmpty()) {
                    String author = getAuthor();
                    String content = edtComment.getText().toString();
                    Date time = Calendar.getInstance().getTime();
                    Comment comment = new Comment(author, content, new Timestamp(time));
                    data.getComments().add(comment);
                    commentAdapter.notifyDataSetChanged();
                    // update to database
                    addCommentToDatabase(comment, data);
                    // push notifi
                }
            }
        });
    }

    void addCommentToDatabase(Comment comment, Forum mItem) {
        db.collection("forum_post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Forum item = document.toObject(Forum.class);
                        if(mItem.getAuthor().equals(item.getAuthor()) && mItem.getId().equals(item.getId())) {
                            db.collection("forum_post").document(document.getId()).update("comments", FieldValue.arrayUnion(comment));
                            break;
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String err = e.getMessage();
            }
        });
    }

    String getAuthor() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String author = sharedPreferences.getString("email", "");
        return author;
    }
}
