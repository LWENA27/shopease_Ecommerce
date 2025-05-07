package com.example.shopease.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.shopease.database.entities.WishlistEntity;
import java.util.List;

@Dao
public interface WishlistDao {

    @Query("SELECT * FROM wishlist")
    List<WishlistEntity> getAllWishlistItems();

    @Query("SELECT * FROM wishlist WHERE productId = :productId")
    WishlistEntity getWishlistItemByProductId(int productId);

    @Insert
    void insertWishlistItem(WishlistEntity wishlistItem);

    @Delete
    void deleteWishlistItem(WishlistEntity wishlistItem);
}