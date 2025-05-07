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
import com.example.shopease.database.entities.CartEntity;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartViewHolder> {

    private List<CartEntity> cartItems;
    private CartItemListener listener;

    public CartItemAdapter(List<CartEntity> cartItems, CartItemListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartEntity cartItem = cartItems.get(position);
        holder.tvProductName.setText(cartItem.getProductName());
        holder.tvProductPrice.setText(String.format("$%.2f", cartItem.getPrice()));
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
        Glide.with(holder.itemView.getContext())
                .load(cartItem.getProductImage())
                .placeholder(R.drawable.placeholder_product)
                .error(R.drawable.error_image)
                .into(holder.ivProductImage);

        holder.btnIncrement.setOnClickListener(v -> listener.onQuantityChanged(position, cartItem.getQuantity() + 1));
        holder.btnDecrement.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                listener.onQuantityChanged(position, cartItem.getQuantity() - 1);
            }
        });
        holder.btnRemove.setOnClickListener(v -> listener.onItemRemoved(position));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateCartItems(List<CartEntity> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged();
    }

    public interface CartItemListener {
        void onQuantityChanged(int position, int quantity);
        void onItemRemoved(int position);
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage, btnIncrement, btnDecrement, btnRemove;
        TextView tvProductName, tvProductPrice, tvQuantity;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnIncrement = itemView.findViewById(R.id.btn_increment);
            btnDecrement = itemView.findViewById(R.id.btn_decrement);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }
    }
}