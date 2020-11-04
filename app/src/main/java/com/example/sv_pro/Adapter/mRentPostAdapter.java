package com.example.sv_pro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.sv_pro.Model.mRentPost;
import com.example.sv_pro.R;

import java.util.ArrayList;

public class mRentPostAdapter extends RecyclerView.Adapter<mRentPostAdapter.mRentPostViewHolder> {

    ArrayList<mRentPost> mData = null;
    Context mContext;

    public mRentPostAdapter(ArrayList<mRentPost> data, Context context) {
        this.mData = data;
        this.mContext = context;
    }

    class mRentPostViewHolder extends RecyclerView.ViewHolder {
        View vRoot;
        TextView txtFaculty, txtAuthor, txtAddress, txtRentPrice, txtEletricPrice, txtWaterPrice, txtContent;
        ViewPager vpMpost;
        public mRentPostViewHolder(@NonNull View itemView) {
            super(itemView);
            vRoot = itemView.findViewById(R.id.v_root_mpost);
            txtFaculty = itemView.findViewById(R.id.txt_faculty);
            txtAuthor = itemView.findViewById(R.id.txt_author);
            txtAddress = itemView.findViewById(R.id.txt_address);
            txtRentPrice = itemView.findViewById(R.id.txt_rent_price);
            txtEletricPrice = itemView.findViewById(R.id.txt_electric_price);
            txtWaterPrice = itemView.findViewById(R.id.txt_water_price);
            txtContent = itemView.findViewById(R.id.txt_content);
            //
            vpMpost = itemView.findViewById(R.id.vp_mpost);
        }
    }

    @NonNull
    @Override
    public mRentPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mpost_item, parent, false);
        return new mRentPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mRentPostViewHolder holder, int position) {
        mRentPost item = mData.get(position);
        //
        holder.txtFaculty.setText("Cho thuê nhà gần " + item.getFaculty());
        holder.txtAuthor.setText(item.getAuthor() + "đã đăng vào lúc: " + item.getPostDate().toDate());
        holder.txtAddress.setText("Địa Chỉ: " + item.getAddress());
        holder.txtRentPrice.setText("Giá phòng: " + item.getRentPrice() + " 1 Tháng");
        holder.txtEletricPrice.setText("Giá điện: " + item.getEletricPrice() + "/số");
        holder.txtWaterPrice.setText("Giá nước: " + item.getWaterPrice() + "/khối");
        holder.txtContent.setText(item.getContent());
        // prepare images
        mPostImgAdapter mPostImgAdapter = new mPostImgAdapter(mContext, item.getPictures());
        holder.vpMpost.setAdapter(mPostImgAdapter);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


}
