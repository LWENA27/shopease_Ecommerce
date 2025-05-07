package com.example.shopease.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.shopease.R;
import com.example.shopease.adapters.WishlistAdapter;
import com.example.shopease.database.AppDatabase;
import com.example.shopease.database.entities.WishlistEntity;
import com.example.shopease.databinding.ActivityWishlistBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WishlistActivity extends AppCompatActivity {

    private ActivityWishlistBinding binding;
    private AppDatabase database;
    private ExecutorService executorService;
    private WishlistAdapter wishlistAdapter;
    private List<WishlistEntity> wishlistItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize database and executor
        database = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        // Setup RecyclerView
        wishlistItems = new ArrayList<>();
        wishlistAdapter = new WishlistAdapter(wishlistItems, item -> {
            // Navigate to product details (optional)
            Toast.makeText(this, "Selected: " + item.getName(), Toast.LENGTH_SHORT).show();
        });
        binding.rvWishlistItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvWishlistItems.setAdapter(wishlistAdapter);

        // Load wishlist items
        loadWishlistItems();

        // Back button
        binding.ivBack.setOnClickListener(v -> finish());

        // View cart button
        binding.btnViewCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void loadWishlistItems() {
        executorService.execute(() -> {
            List<WishlistEntity> items = database.wishlistDao().getAllWishlistItems();
            runOnUiThread(() -> {
                wishlistItems.clear();
                wishlistItems.addAll(items);
                binding.tvEmptyWishlist.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
                wishlistAdapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWishlistItems(); // Refresh wishlist
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}