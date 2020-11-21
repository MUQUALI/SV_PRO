package com.example.sv_pro.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sv_pro.Model.Comment;
import com.example.sv_pro.Model.Forum;
import com.example.sv_pro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListCommentAdapter extends RecyclerView.Adapter<ListCommentAdapter.CommentViewHolder> {
    ArrayList<Comment> mData;
    Context mContext;
    Comment commentSelect = new Comment();
    String postID;
    FirebaseFirestore db;

    public ListCommentAdapter(ArrayList<Comment> mData, Context mContext, String postID) {
        this.mData = mData;
        this.mContext = mContext;
        this.db = FirebaseFirestore.getInstance();
        this.postID = postID;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = mData.get(position);

        holder.tvTime.setText(comment.getTime().toDate().toString());
        holder.tvAuthor.setText(comment.getAuthor() + ": ");
        holder.tvComment.setText(comment.getComment());

        if(position % 2 != 0) {
            holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.colorDark2));
            holder.tvAuthor.setTextColor(mContext.getResources().getColor(R.color.colorDark2));
            holder.tvComment.setTextColor(mContext.getResources().getColor(R.color.colorDark2));
        }

        //Activity myActivity = (Activity) mContext;

        holder.vRoot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                commentSelect = comment;
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.option_delete: {
                                mData.remove(commentSelect);
                                notifyDataSetChanged();
                                db.collection("forum_post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {
                                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                                Forum item = doc.toObject(Forum.class);
                                                if(item.getId().equals(postID)) {
                                                    db.collection("forum_post").document(doc.getId()).update("comments", FieldValue.arrayRemove(commentSelect));
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                });
                                break;
                            }
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.long_click_menu);
                popupMenu.show();
                return false;
            }
        });
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }


    class CommentViewHolder extends RecyclerView.ViewHolder {
        View vRoot;
        TextView tvTime, tvComment, tvAuthor;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            vRoot = itemView.findViewById(R.id.view_comment_root);
            tvTime = itemView.findViewById(R.id.comment_time);
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvAuthor = itemView.findViewById(R.id.tv_author);
        }
    }
}
