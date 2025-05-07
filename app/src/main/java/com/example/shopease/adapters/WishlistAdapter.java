package com.example.shopease.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.shopease.R;
import com.example.shopease.database.AppDatabase;
import com.example.shopease.database.entities.WishlistEntity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    private List<WishlistEntity> wishlistItems;
    private OnWishlistClickListener listener;
    private AppDatabase database;
    private ExecutorService executorService;

    public interface OnWishlistClickListener {
        void onWishlistClick(WishlistEntity item);
    }

    public WishlistAdapter(List<WishlistEntity> wishlistItems, OnWishlistClickListener listener) {
        this.wishlistItems = wishlistItems;
        this.listener = listener;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist, parent, false);
        database = AppDatabase.getInstance(parent.getContext());
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        WishlistEntity item = wishlistItems.get(position);
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(String.format("$%.2f", item.getPrice()));
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.placeholder_product)
                .error(R.drawable.error_image)
                .into(holder.ivImage);

        // Remove item
        holder.ivRemove.setOnClickListener(v -> {
            executorService.execute(() -> {
                database.wishlistDao().deleteWishlistItem(item);
                wishlistItems.remove(position);
                holder.itemView.post(() -> {
                    notifyDataSetChanged();
                    listener.onWishlistClick(item);
                });
            });
        });

        // Item click
        holder.itemView.setOnClickListener(v -> listener.onWishlistClick(item));
    }

    @Override
    public int getItemCount() {
        return wishlistItems.size();
    }

    static class WishlistViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage, ivRemove;
        TextView tvName, tvPrice;

        WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_wishlist_image);
            ivRemove = itemView.findViewById(R.id.iv_remove);
            tvName = itemView.findViewById(R.id.tv_wishlist_name);
            tvPrice = itemView.findViewById(R.id.tv_wishlist_price);
        }
    }
}