package com.example.sv_pro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.sv_pro.Model.faculty;
import com.example.sv_pro.Model.post;
import com.example.sv_pro.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    ArrayList<post> data = null;
    Context context;
    public  PostAdapter (ArrayList<post> data, Context context)
    {
        this.data = data; // truyền vào dữ liệu để nó quản lý
        this.context = context; //  context này chỉ thể hiện của lớp đối thượng.
    }

    // khai báo class con, thể hiện cho cái item hiển thị bên trong recycler view
    class PostViewHolder extends RecyclerView.ViewHolder {
       // private AdapterView.OnItemClickListener itemClickListener;

        View vRoot; // view root ở đây là cái bao ngoài cùng 1 item. ở đây là cái Linear layout
        TextView txtFaculty,txtContent, txtAddress, txtAuthor, txtPrice,txtElectric_price,txtWater_price;
        ViewPager vpfPost;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            vRoot = itemView.findViewById(R.id.v_root_post);
            txtFaculty = itemView.findViewById(R.id.txt_faculty);
            txtContent = itemView.findViewById(R.id.tv_content);
            txtAddress = itemView.findViewById(R.id.txt_address);
            txtAuthor= itemView.findViewById(R.id.tv_author);
            txtPrice = itemView.findViewById(R.id.tv_rent_price);
            txtElectric_price = itemView.findViewById(R.id.tv_electric_price);
            txtWater_price = itemView.findViewById(R.id.tv_water_price);
            // view pager
            vpfPost = itemView.findViewById(R.id.vp_fpost);
        }

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view) ;
        // Tạo ra 1 ViewHolder chứa 1 item area_item
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position)
    {
        post item = data.get(position);
        holder.txtContent.setText(item.getContent());
        holder.txtAddress.setText("Địa Chỉ: " + item.getAddress());
        holder.txtAuthor.setText(item.getAuthor() + " đã đăng vào lúc: " + item.getPostDate().toDate());
        holder.txtPrice.setText("Giá phòng: " + item.getRentPrice() + "/tháng");
        holder.txtElectric_price.setText("Tiền điện: " + item.getElectricPrice() + "/Số");
        holder.txtWater_price.setText("Tiền nước: " + item.getWaterPrice() + "/Khối");
        holder.txtFaculty.setText("Cho thuê nhà gần khu vực " + item.getFaculty());
        // images
        if(item.getPictures() != null && item.getPictures().size() > 0) {
            vpPostAdapter vpPostAdapter = new vpPostAdapter(context, item.getPictures());
            holder.vpfPost.setAdapter(vpPostAdapter);
        }
        else {
            holder.vpfPost.setAdapter(null);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
