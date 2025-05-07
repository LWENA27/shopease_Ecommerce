package com.example.shopease.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.shopease.R;
import com.example.shopease.database.AppDatabase;
import com.example.shopease.database.entities.CartEntity;
import com.example.shopease.database.entities.WishlistEntity;
import com.example.shopease.databinding.ActivityProductDetailsBinding;
import com.example.shopease.models.Product;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailsBinding binding;
    private AppDatabase database;
    private ExecutorService executorService;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize database and executor
        database = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        // Get product from intent
        product = (Product) getIntent().getSerializableExtra("PRODUCT");
        if (product == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Populate UI
        binding.tvProductName.setText(product.getName());
        binding.tvProductDescription.setText(product.getDescription());
        double discountedPrice = product.getPrice() * (1 - product.getDiscount() / 100.0);
        binding.tvProductPrice.setText(String.format("$%.2f", discountedPrice));
        binding.tvProductRating.setText(String.format("Rating: %.1f", product.getRating()));
        binding.tvProductStock.setText(product.isInStock() ? "In Stock" : "Out of Stock");
        Glide.with(this)
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder_product)
                .error(R.drawable.error_image)
                .into(binding.ivProductImage);

        // Back button
        binding.ivBack.setOnClickListener(v -> finish());

        // Add to cart
        binding.btnAddToCart.setOnClickListener(v -> {
            if (!product.isInStock()) {
                Toast.makeText(this, "Product is out of stock", Toast.LENGTH_SHORT).show();
                return;
            }
            executorService.execute(() -> {
                CartEntity existingItem = database.cartDao().getCartItemByProductId(product.getId());
                if (existingItem != null) {
                    existingItem.setQuantity(existingItem.getQuantity() + 1);
                    database.cartDao().updateCartItem(existingItem);
                } else {
                    CartEntity cartItem = new CartEntity(
                            product.getId(),
                            product.getName(),
                            product.getImageUrl(),
                            product.getPrice(),
                            1
                    );
                    database.cartDao().insertCartItem(cartItem);
                }
                runOnUiThread(() -> Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show());
            });
        });

        // Add to wishlist
        binding.btnAddToWishlist.setOnClickListener(v -> {
            executorService.execute(() -> {
                WishlistEntity existingItem = database.wishlistDao().getWishlistItemByProductId(product.getId());
                if (existingItem == null) {
                    WishlistEntity wishlistItem = new WishlistEntity(
                            product.getId(),
                            product.getName(),
                            product.getImageUrl(),
                            product.getPrice()
                    );
                    database.wishlistDao().insertWishlistItem(wishlistItem);
                    runOnUiThread(() -> Toast.makeText(this, "Added to wishlist", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Already in wishlist", Toast.LENGTH_SHORT).show());
                }
            });
        });

        // View cart
        binding.btnViewCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        // View wishlist
        binding.btnViewWishlist.setOnClickListener(v -> {
            Intent intent = new Intent(this, WishlistActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}