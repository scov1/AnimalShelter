package org.seda.animalshelter.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.seda.animalshelter.R;

import java.util.List;

public class ServiceImagesAdapter extends PagerAdapter {

    private List<String> serviceImages;

    public ServiceImagesAdapter(List<String> serviceImages) {
        this.serviceImages = serviceImages;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView serviceImage = new ImageView(container.getContext());
        Glide.with(container.getContext()).load(serviceImages.get(position)).apply(new RequestOptions().placeholder(R.drawable.home_hor_icon)).into(serviceImage);
        container.addView(serviceImage,0);
        return serviceImage;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }

    @Override
    public int getCount() {
        return serviceImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
