package com.example.sv_pro.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class PickImgForumAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Uri> dataImages;

    public PickImgForumAdapter(Context mContext, ArrayList<Uri> dataImages) {
        this.mContext = mContext;
        this.dataImages = dataImages;
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
        item.setImageURI(dataImages.get(position));
        container.addView(item, 0);
        return item;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

}
