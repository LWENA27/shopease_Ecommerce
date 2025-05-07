package com.example.shopease.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shopease.R;
import com.example.shopease.activities.ProductDetailActivity;
import com.example.shopease.adapters.ProductAdapter;
import com.example.shopease.models.Product;
import com.example.shopease.network.ApiClient;
import com.example.shopease.network.ApiService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {

    private TextView tvCategoryTitle;
    private RecyclerView rvProducts;
    private ApiService apiService;
    private String categoryName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        tvCategoryTitle = view.findViewById(R.id.tv_category_title);
        rvProducts = view.findViewById(R.id.rv_products);

        // Get category name
        if (getArguments() != null) {
            categoryName = getArguments().getString("CATEGORY_NAME");
            tvCategoryTitle.setText(categoryName);
        }

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Setup RecyclerView
        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Load products
        loadProducts();
    }

    private void loadProducts() {
        // For "Featured" or "New Arrivals", use specific endpoints
        Call<List<Product>> call = categoryName.equals("Featured") ? apiService.getFeaturedProducts() :
                categoryName.equals("New Arrivals") ? apiService.getProducts() :
                        apiService.getProductsByCategory(categoryName);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductAdapter productAdapter = new ProductAdapter(response.body(), product -> {
                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                        intent.putExtra("PRODUCT", product);
                        startActivity(intent);
                    });
                    rvProducts.setAdapter(productAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }
}