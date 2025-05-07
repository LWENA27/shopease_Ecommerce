package com.example.shopease.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.shopease.R;
import com.example.shopease.databinding.ActivityOrderHistoryBinding;

public class OrderHistoryActivity extends AppCompatActivity {

    private ActivityOrderHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TODO: Load user orders via getUserOrders API and display in RecyclerView
    }
}