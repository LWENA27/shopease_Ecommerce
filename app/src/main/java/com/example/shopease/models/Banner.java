package com.example.shopease.models;

import java.io.Serializable;

public class Banner implements Serializable {
    private int id;
    private String title;
    private String description;
    private String imageUrl;

    public Banner(int id, String title, String description, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}