package com.example.shopease.network;

import com.example.shopease.models.Banner;
import com.example.shopease.models.Category;
import com.example.shopease.models.Order;
import com.example.shopease.models.Product;
import com.example.shopease.models.User;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("banners")
    Call<List<Banner>> getBanners();

    @GET("categories")
    Call<List<Category>> getCategories();

    @GET("products/featured")
    Call<List<Product>> getFeaturedProducts();

    @GET("products/new")
    Call<List<Product>> getNewProducts();

    Call<User> login(User user);

    Call<User> register(User user);

    Call<Order> placeOrder(Order order);

    Call<List<Product>> getProducts();

    Call<List<Product>> getProductsByCategory(String categoryName);
}