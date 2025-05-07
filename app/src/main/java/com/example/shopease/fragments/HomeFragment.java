//package com.example.shopease.fragments;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewpager2.widget.ViewPager2;
//import com.example.shopease.R;
//import com.example.shopease.activities.CartActivity;
//import com.example.shopease.activities.ProductDetailActivity;
//import com.example.shopease.adapters.BannerAdapter;
//import com.example.shopease.adapters.CategoryAdapter;
//import com.example.shopease.adapters.ProductAdapter;
//import com.example.shopease.models.Banner;
//import com.example.shopease.models.Category;
//import com.example.shopease.models.Product;
//import com.example.shopease.network.ApiClient;
//import com.example.shopease.network.ApiService;
//import java.util.ArrayList;
//import java.util.List;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class HomeFragment extends Fragment {
//
//    private ViewPager2 bannerSlider;
//    private RecyclerView rvCategories, rvFeaturedProducts, rvNewProducts;
//    private EditText etSearch;
//    private ImageView btnCart;
//    private TextView tvViewAllFeatured, tvViewAllNew;
//    private ApiService apiService;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_home, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        // Initialize views
//        bannerSlider = view.findViewById(R.id.banner_slider);
//        rvCategories = view.findViewById(R.id.rv_categories);
//        rvFeaturedProducts = view.findViewById(R.id.rv_featured_products);
//        rvNewProducts = view.findViewById(R.id.rv_new_products);
//        etSearch = view.findViewById(R.id.et_search);
//        btnCart = view.findViewById(R.id.btn_cart);
//        tvViewAllFeatured = view.findViewById(R.id.tv_view_all_featured);
//        tvViewAllNew = view.findViewById(R.id.tv_view_all_new);
//
//        // Initialize API service
//        apiService = ApiClient.getClient().create(ApiService.class);
//
//        // Setup click listeners
//        setupClickListeners();
//
//        // Setup recycler views
//        setupRecyclerViews();
//
//        // Load data
//        loadBanners();
//        loadCategories();
//        loadFeaturedProducts();
//        loadNewProducts();
//    }
//
//    private void setupClickListeners() {
//        btnCart.setOnClickListener(v -> {
//            startActivity(new Intent(getActivity(), CartActivity.class));
//        });
//
//        etSearch.setOnClickListener(v -> {
//            Toast.makeText(getContext(), "Search functionality coming soon", Toast.LENGTH_SHORT).show();
//            // TODO: Implement search functionality
//        });
//
//        tvViewAllFeatured.setOnClickListener(v -> navigateToCategory("Featured"));
//
//        tvViewAllNew.setOnClickListener(v -> navigateToCategory("New Arrivals"));
//    }
//
//    private void navigateToCategory(String categoryName) {
//        Bundle bundle = new Bundle();
//        bundle.putString("CATEGORY_NAME", categoryName);
//        CategoryFragment categoryFragment = new CategoryFragment();
//        categoryFragment.setArguments(bundle);
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container, categoryFragment)
//                .addToBackStack(null)
//                .commit();
//    }
//
//    private void setupRecyclerViews() {
//        // Setup Categories RecyclerView
//        rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
//
//        // Setup Featured Products RecyclerView
//        rvFeaturedProducts.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
//
//        // Setup New Products RecyclerView
//        rvNewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
//    }
//
//    private void loadBanners() {
//        // Mock data (replace with API call if available)
//        List<Banner> banners = new ArrayList<>();
//        banners.add(new Banner(1, "Summer Sale", "Up to 50% off", "https://example.com/banner1.jpg"));
//        banners.add(new Banner(2, "New Collection", "Check out our latest products", "https://example.com/banner2.jpg"));
//        banners.add(new Banner(3, "Free Shipping", "On orders over $50", "https://example.com/banner3.jpg"));
//        BannerAdapter bannerAdapter = new BannerAdapter(banners);
//        bannerSlider.setAdapter(bannerAdapter);
//    }
//
//    private void loadCategories() {
//        apiService.getCategories().enqueue(new Callback<List<Category>>() {
//            @Override
//            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    CategoryAdapter categoryAdapter = new CategoryAdapter(response.body(), category -> navigateToCategory(category.getName()));
//                    rvCategories.setAdapter(categoryAdapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Category>> call, Throwable t) {
//                Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void loadFeaturedProducts() {
//        apiService.getFeaturedProducts().enqueue(new Callback<List<Product>>() {
//            @Override
//            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    ProductAdapter productAdapter = new ProductAdapter(response.body(), this::navigateToProductDetail);
//                    rvFeaturedProducts.setAdapter(productAdapter);
//                }
//            }
//
//            private void navigateToProductDetail(Product product) {
//            }
//
//            @Override
//            public void onFailure(Call<List<Product>> call, Throwable t) {
//                Toast.makeText(getContext(), "Failed to load featured products", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void loadNewProducts() {
//        apiService.getProducts().enqueue(new Callback<List<Product>>() {
//            @Override
//            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    ProductAdapter productAdapter = new ProductAdapter(response.body(), this::navigateToProductDetail);
//                    rvNewProducts.setAdapter(productAdapter);
//                }
//            }
//
//            private void navigateToProductDetail(Product product) {
//            }
//
//            @Override
//            public void onFailure(Call<List<Product>> call, Throwable t) {
//                Toast.makeText(getContext(), "Failed to load new products", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void navigateToProductDetail(Product product) {
//        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
//        intent.putExtra("PRODUCT", product);
//        startActivity(intent);
//    }
//}