package com.example.shopease.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.shopease.adapters.CartItemAdapter;
import com.example.shopease.database.AppDatabase;
import com.example.shopease.database.dao.CartDao;
import com.example.shopease.database.entities.CartEntity;
import com.example.shopease.databinding.ActivityCartBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.CartItemListener {

    private ActivityCartBinding binding;
    private AppDatabase database;
    private ExecutorService executorService;
    private CartItemAdapter cartItemAdapter;
    private List<CartEntity> cartItems;
    private double subtotal = 0;
    private final double SHIPPING_FEE = 5.99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize database and executor
        database = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        // Setup RecyclerView
        cartItems = new ArrayList<>();
        cartItemAdapter = new CartItemAdapter(cartItems, this);
        binding.rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCartItems.setAdapter(cartItemAdapter);

        // Load cart items
        loadCartItems();

        // Back button click listener
        binding.ivBack.setOnClickListener(v -> finish());

        // Shop Now button in empty cart
        binding.btnShopNow.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Checkout button click listener
        binding.btnCheckout.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, CheckoutActivity.class);
                intent.putExtra("SUBTOTAL", subtotal);
                intent.putExtra("SHIPPING", SHIPPING_FEE);
                intent.putExtra("TOTAL", subtotal + SHIPPING_FEE);
                startActivity(intent);
            }
        });
    }

    private void loadCartItems() {
        executorService.execute(() -> {
            List<CartEntity> items = database.cartDao().getAllCartItems();
            runOnUiThread(() -> {
                cartItems.clear();
                cartItems.addAll(items);
                binding.layoutEmptyCart.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
                binding.layoutCartContent.setVisibility(items.isEmpty() ? View.GONE : View.VISIBLE);
                cartItemAdapter.notifyDataSetChanged();
                calculatePrices();
            });
        });
    }

    private void calculatePrices() {
        subtotal = 0;
        for (CartEntity item : cartItems) {
            subtotal += item.getPrice() * item.getQuantity();
        }
        binding.tvSubtotal.setText(String.format("$%.2f", subtotal));
        binding.tvShipping.setText(String.format("$%.2f", SHIPPING_FEE));
        binding.tvTotal.setText(String.format("$%.2f", subtotal + SHIPPING_FEE));
    }

    @Override
    public void onQuantityChanged(int position, int quantity) {
        CartEntity cartItem = cartItems.get(position);
        cartItem.setQuantity(quantity);
        executorService.execute(() -> {
            database.cartDao().updateCartItem(cartItem);
            runOnUiThread(() -> {
                cartItemAdapter.notifyItemChanged(position);
                calculatePrices();
            });
        });
    }

    @Override
    public void onItemRemoved(int position) {
        CartEntity cartItem = cartItems.get(position);
        executorService.execute(() -> {
            database.cartDao().deleteCartItem(cartItem.getProductId());
            runOnUiThread(() -> {
                cartItems.remove(position);
                cartItemAdapter.notifyItemRemoved(position);
                if (cartItems.isEmpty()) {
                    binding.layoutEmptyCart.setVisibility(View.VISIBLE);
                    binding.layoutCartContent.setVisibility(View.GONE);
                }
                calculatePrices();
            });
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