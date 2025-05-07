package com.example.shopease.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wishlist")
public class WishlistEntity {

    @PrimaryKey
    private int productId;
    private String productName;
    private String productImage;
    private double price;

    public WishlistEntity(int productId, String productName, String productImage, double price) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.price = price;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductImage() { return productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getName() {
        return "";
    }

    public byte[] getImageUrl() {
        return new byte[0];
    }
}