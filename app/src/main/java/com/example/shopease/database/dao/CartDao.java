package com.example.shopease.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shopease.database.entities.CartEntity;

import java.util.List;

@Dao
public interface CartDao {

    @Query("SELECT * FROM cart_items")
    List<CartEntity> getAllCartItems();

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    CartEntity getCartItemByProductId(int productId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCartItem(CartEntity cartItem);

    @Update
    void updateCartItem(CartEntity cartItem);

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    void deleteCartItem(int productId);

    @Query("DELETE FROM cart_items")
    void clearCart();

    @Query("SELECT COUNT(*) FROM cart_items")
    int getCartItemCount();

    @Query("SELECT SUM(price * quantity) FROM cart_items")
    double getCartTotal();
}