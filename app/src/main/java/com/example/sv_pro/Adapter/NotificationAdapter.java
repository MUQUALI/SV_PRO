package com.example.sv_pro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sv_pro.Model.Notification;
import com.example.sv_pro.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{
    ArrayList<Notification> mData;
    Context mContext;

    public NotificationAdapter(ArrayList<Notification> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification item = mData.get(position);

        holder.tvTime.setText(item.getTime().toDate().toString());
        holder.tvContent.setText(item.getAuthor() + " đã bình luận về bài của bạn");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        View vRoot;
        TextView tvTime, tvContent;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            vRoot = itemView.findViewById(R.id.notifi_view_root);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }
}
