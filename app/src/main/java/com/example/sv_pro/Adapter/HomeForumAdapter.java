package com.example.sv_pro.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.sv_pro.Model.Comment;
import com.example.sv_pro.Model.Forum;
import com.example.sv_pro.Model.Notification;
import com.example.sv_pro.R;
import com.example.sv_pro.personal.NotificationForumActivity;
import com.example.sv_pro.personal.SaveImage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class HomeForumAdapter extends RecyclerView.Adapter<HomeForumAdapter.ForumViewHolder> implements Filterable {
    ArrayList<Forum> mData;
    ArrayList<Forum> mDataFull;
    Context mContext;
    FirebaseFirestore mDb;

    public HomeForumAdapter(ArrayList<Forum> mData, Context mContext, FirebaseFirestore db) {
        this.mData = mData;
        this.mContext = mContext;
        this.mDb = db;
        this.mDataFull = new ArrayList<>(mData);
    }


    class ForumViewHolder extends RecyclerView.ViewHolder {
        View vRoot;
        TextView tvAuthor, tvCountComment, tvContent, tvComment;
        ViewPager vpForum;
        RecyclerView rvListComment;
        EditText edtComment;
        ImageView btnSendComment;
        public ForumViewHolder(@NonNull View itemView) {
            super(itemView);
            vRoot = itemView.findViewById(R.id.forum_item_root);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvCountComment = itemView.findViewById(R.id.tv_count_comment);
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvContent = itemView.findViewById(R.id.tv_content);
            vpForum = itemView.findViewById(R.id.vp_forum);
            rvListComment = itemView.findViewById(R.id.rv_forum_comment);
            edtComment = itemView.findViewById(R.id.edt_comment);
            btnSendComment = itemView.findViewById(R.id.send_comment);
        }
    }

    @NonNull
    @Override
    public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.forum_item, parent, false);
        return new ForumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
        try {
            Forum forum = mData.get(position);
            //ListCommentAdapter commentAdapter;
            ListCommentAdapter commentAdapter = new ListCommentAdapter(forum.getComments(), mContext, forum.getId());

            holder.tvAuthor.setText(forum.getAuthor());
            holder.tvCountComment.setText(forum.getComments().size() + "");
            holder.tvContent.setText(forum.getContent());
            // viewpager
            if(forum.getPictures().size() > 0) {
                mPostImgAdapter adapter = new mPostImgAdapter(mContext, forum.getPictures());
                holder.vpForum.setAdapter(adapter);
            }
            else {
                holder.vpForum.setVisibility(View.GONE);
            }
            // list comment
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, VERTICAL, false);
            holder.rvListComment.setLayoutManager(linearLayoutManager);
            holder.rvListComment.setAdapter(commentAdapter);

            // download image

            // send comment
            holder.btnSendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!holder.edtComment.getText().toString().isEmpty()) {
                        String author = getAuthor();
                        String content = holder.edtComment.getText().toString();
                        Date time = Calendar.getInstance().getTime();
                        Comment comment = new Comment(author, content, new Timestamp(time));
                        forum.getComments().add(comment);
                        commentAdapter.notifyDataSetChanged();
                        // update to database
                        addCommentToDatabase(comment, forum);
                        // push notifi
                        pushNotification(forum.getId());
                        // sace notifi
                        saveNotificationToDb(forum.getId());
                    }
                }

            });
            // go to forum detail
            holder.tvComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, NotificationForumActivity.class);
                    i.putExtra("id", forum.getId());
                    mContext.startActivity(i);
                }
            });
        }
        catch (Exception e) {
            String err = e.getMessage();
        }
    }

    void addCommentToDatabase(Comment comment, Forum mItem) {
        mDb.collection("forum_post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Forum item = document.toObject(Forum.class);
                        if(mItem.getAuthor().equals(item.getAuthor()) && mItem.getId().equals(item.getId())) {
                            mDb.collection("forum_post").document(document.getId()).update("comments", FieldValue.arrayUnion(comment));
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
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String author = sharedPreferences.getString("email", "");
        return author;
    }

    void pushNotification(String id) {
        String message = getAuthor() + " đã bình luận vào bài viết cảu bạn";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_notifications_blue_24dp)
                .setContentTitle("Bạn có thông báo mới")
                .setContentText(message)
                .setAutoCancel(true);

        Intent i = new Intent(mContext, NotificationForumActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    void saveNotificationToDb(String postID) {
        Date time = Calendar.getInstance().getTime();
        Notification item = new Notification(new Timestamp(time), getAuthor());
        mDb.collection("notifications").document(postID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                try {
                    if(task.isSuccessful()) {
                        ArrayList<Notification> data = (ArrayList<Notification> )task.getResult().get("notifis");
                        if(data == null) {
                            ArrayList<Notification> list = new ArrayList<>();
                            list.add(item);
                            HashMap map = new HashMap();
                            map.put("notifis", list);
                            mDb.collection("notifications").document(postID).set(map);
                        }
                        else {
                            mDb.collection("notifications").document(postID).update("notifis", FieldValue.arrayUnion(item));
                        }
                    }
                }
                catch (Exception e) {
                    String err = e.getMessage();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return forumFilter;
    }

    private Filter forumFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Forum> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(mDataFull);
            }
            else {
                String key = charSequence.toString().toLowerCase().trim();
                for (Forum item: mDataFull) {
                    if(item.getContent().toLowerCase().contains(key)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return  filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mData.clear();
            mData.addAll((ArrayList<Forum>) filterResults.values);
            notifyDataSetChanged();
        }
    };

}
