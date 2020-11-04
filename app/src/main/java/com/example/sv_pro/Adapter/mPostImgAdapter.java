package com.example.sv_pro.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class mPostImgAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> dataImages;

    mPostImgAdapter(Context context, ArrayList<String> imageUrl) {
        mContext = context;
        dataImages = imageUrl;
    }

    @Override
    public int getCount() {
        return dataImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView item = new ImageView(mContext);
        item.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.get().load(dataImages.get(position)).into(item);
        container.addView(item, 0);
        return item;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
