package com.example.shopease.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.shopease.R;
import com.example.shopease.activities.CartActivity;
import com.example.shopease.activities.ProductDetailActivity;
import com.example.shopease.adapters.BannerAdapter;
import com.example.shopease.adapters.CategoryAdapter;
import com.example.shopease.adapters.ProductAdapter;
import com.example.shopease.databinding.FragmentHomeBinding;
import com.example.shopease.models.Banner;
import com.example.shopease.models.Category;
import com.example.shopease.models.Product;
import com.example.shopease.network.ApiClient;
import com.example.shopease.network.ApiService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private BannerAdapter bannerAdapter;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter featuredProductAdapter;
    private ProductAdapter newProductAdapter;
    private ApiService apiService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Setup banners
        bannerAdapter = new BannerAdapter(new ArrayList<>());
        binding.vpBanners.setAdapter(bannerAdapter);

        // Setup categories
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), category -> {
            Toast.makeText(getContext(), "Category: " + category.getName(), Toast.LENGTH_SHORT).show();
            // TODO: Navigate to CategoryFragment with category ID
        });
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategories.setAdapter(categoryAdapter);

        // Setup featured products
        featuredProductAdapter = new ProductAdapter(new ArrayList<>(), product -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.getId());
            startActivity(intent);
        });
        binding.rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvProducts.setAdapter(featuredProductAdapter);

        // Setup new products
        newProductAdapter = new ProductAdapter(new ArrayList<>(), product -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.getId());
            startActivity(intent);
        });
        binding.rvNewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvNewProducts.setAdapter(newProductAdapter);

        // Cart button
        binding.btnCart.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CartActivity.class));
        });

        // View All buttons
        binding.tvViewAllFeatured.setOnClickListener(v -> {
            Toast.makeText(getContext(), "View All Featured Products", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to CategoryFragment with featured products
        });

        binding.tvViewAllNew.setOnClickListener(v -> {
            Toast.makeText(getContext(), "View All New Products", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to CategoryFragment with new products
        });

        // Search functionality
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            String query = binding.etSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                Toast.makeText(getContext(), "Search: " + query, Toast.LENGTH_SHORT).show();
                // TODO: Implement search functionality
            }
            return true;
        });

        // Load data
        loadBanners();
        loadCategories();
        loadFeaturedProducts();
        loadNewProducts();

        return root;
    }

    private void loadBanners() {
        apiService.getBanners().enqueue(new Callback<List<Banner>>() {
            @Override
            public void onResponse(Call<List<Banner>> call, Response<List<Banner>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bannerAdapter.updateBanners(response.body());
                } else {
                    Toast.makeText(getContext(), "Failed to load banners", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Banner>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategories() {
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryAdapter.updateCategories(response.body());
                } else {
                    Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFeaturedProducts() {
        apiService.getFeaturedProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    featuredProductAdapter.updateProducts(response.body());
                } else {
                    Toast.makeText(getContext(), "Failed to load featured products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNewProducts() {
        apiService.getNewProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newProductAdapter.updateProducts(response.body());
                } else {
                    Toast.makeText(getContext(), "Failed to load new products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}