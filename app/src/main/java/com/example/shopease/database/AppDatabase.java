package com.example.shopease.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import com.example.shopease.database.dao.CartDao;
import com.example.shopease.database.dao.WishlistDao;
import com.example.shopease.database.entities.CartEntity;
import com.example.shopease.database.entities.WishlistEntity;

@Database(entities = {CartEntity.class, WishlistEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CartDao cartDao();
    public abstract WishlistDao wishlistDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "shopease_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}