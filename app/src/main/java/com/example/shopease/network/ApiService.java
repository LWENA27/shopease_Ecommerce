package com.example.shopease.network;

import com.example.shopease.models.Banner;
import com.example.shopease.models.Category;
import com.example.shopease.models.Order;
import com.example.shopease.models.Product;
import com.example.shopease.models.User;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("banners")
    Call<List<Banner>> getBanners();

    @GET("categories")
    Call<List<Category>> getCategories();

    @GET("products/featured")
    Call<List<Product>> getFeaturedProducts();

    @GET("products/new")
    Call<List<Product>> getNewProducts();

    @POST("login")
    Call<User> login(@Body LoginRequest request);

    @POST("register")
    Call<User> register(@Body RegisterRequest request);

    @POST("orders")
    Call<Order> placeOrder(@Body Order order);

    @GET("products")
    Call<List<Product>> getProducts();

    @GET("categories/{categoryName}/products")
    Call<List<Product>> getProductsByCategory(@Path("categoryName") String categoryName);

    class LoginRequest {
        private String email;
        private String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    class RegisterRequest {
        private String name;
        private String email;
        private String password;

        public RegisterRequest(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }
    }
}