package com.example.sv_pro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sv_pro.Model.faculty;
import com.example.sv_pro.R;

import java.util.ArrayList;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.FacultyViewHolder> {
    // class này dùng để quản lý dữ liệu đổ vào recycler view
    ArrayList<faculty> data = null;
    Context context;

    public FacultyAdapter(ArrayList<faculty> data, Context context) {
        this.data = data; // chuyền vào dữ liệu để nó quản lý
        this.context = context; //  context này chỉ thể hiện của lớp đối thượng.Hơi khó hiểu nhưng đại khái Lúc nào cũng phải có
    }


    // khai báo class con, thể hiện cho cái item hiển thị bên trong recycler view
    class FacultyViewHolder extends RecyclerView.ViewHolder {
        View vRoot; // view root ở đây là cái bao ngoài cùng 1 item. ở đây là cái Linear layout
        TextView tvFaculty, tvAddress, tvDescription;
        public FacultyViewHolder(@NonNull View itemView) {
            super(itemView);
            vRoot = itemView.findViewById(R.id.v_root);
            tvFaculty = itemView.findViewById(R.id.tv_faculty);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }

    @NonNull
    @Override
    public FacultyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.area_item, parent, false);
        return new FacultyViewHolder(view);
        // Tạo ra 1 ViewHolder chứa 1 item area_item
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyViewHolder holder, int position) {
        // lấy ra từng item cho đến hết
        faculty item = data.get(position);
        //đổ dữ liêu lên view holder
        holder.tvFaculty.setText(item.getFaculty());
        holder.tvAddress.setText(item.getAddress() +" "+ item.getDistrict());
        holder.tvDescription.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    } // trả tổng item


}
