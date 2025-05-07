package com.example.shopease.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.shopease.databinding.ActivityOrderSuccessBinding;

public class OrderSuccessActivity extends AppCompatActivity {

    private ActivityOrderSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get order ID from intent
        int orderId = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderId != -1) {
            binding.tvOrderId.setText(String.format("Order ID: %d", orderId));
        }

        // Back button click listener
        binding.ivBack.setOnClickListener(v -> finish());

        // Continue shopping button click listener
        binding.btnContinueShopping.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}