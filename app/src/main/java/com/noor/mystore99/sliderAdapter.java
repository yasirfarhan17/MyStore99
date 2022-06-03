package com.noor.mystore99;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.networkmodule.model.SliderModel;

import java.util.List;

public class sliderAdapter extends PagerAdapter {

    private List<SliderModel> sliderModelList;

    public sliderAdapter(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.slider_layout, container, false);
        ConstraintLayout bannerContainer = view.findViewById(R.id.banner_container);
        try {
            bannerContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sliderModelList.get(position).getBackgroundColor())));
        } catch (IllegalArgumentException e) {
            bannerContainer.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));

        }
        ImageView banner = view.findViewById(R.id.banner_slide);
        byte[] decodedString = Base64.decode(sliderModelList.get(position).getBanner(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Glide.with(container.getContext()).load(decodedByte).apply(new RequestOptions().placeholder(R.drawable.ic_home_black_24dp)).into(banner);
        container.addView(view, 0);
        return view;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return sliderModelList.size();
    }

}
