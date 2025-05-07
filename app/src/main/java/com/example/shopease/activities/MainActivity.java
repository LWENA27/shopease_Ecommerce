package com.example.shopease.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import com.example.shopease.R;
import com.example.shopease.databinding.ActivityMainBinding;
import com.example.shopease.ui.home.HomeFragment;
import com.example.shopease.utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefManager = new SharedPrefManager(this);

        // Check if user is logged in
        if (!sharedPrefManager.isLoggedIn()) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
            return;
        }

        // Load HomeFragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

        // Setup toolbar
        setSupportActionBar(binding.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_cart) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        } else if (itemId == R.id.action_wishlist) {
            startActivity(new Intent(this, WishlistActivity.class));
            return true;
        } else if (itemId == R.id.action_logout) {
            sharedPrefManager.clear();
            Intent intent = new Intent(this, AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}