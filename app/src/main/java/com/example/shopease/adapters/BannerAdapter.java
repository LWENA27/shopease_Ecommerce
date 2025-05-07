package com.example.shopease.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.shopease.R;
import com.example.shopease.models.Banner;
import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private List<Banner> banners;

    public BannerAdapter(List<Banner> banners) {
        this.banners = banners;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Banner banner = banners.get(position);
        Glide.with(holder.itemView.getContext())
                .load(banner.getImageUrl())
                .placeholder(R.drawable.placeholder_product)
                .error(R.drawable.error_image)
                .into(holder.ivBanner);
    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    public void updateBanners(List<Banner> newBanners) {
        this.banners.clear();
        this.banners.addAll(newBanners);
        notifyDataSetChanged();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBanner;

        BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBanner = itemView.findViewById(R.id.iv_banner);
        }
    }
}