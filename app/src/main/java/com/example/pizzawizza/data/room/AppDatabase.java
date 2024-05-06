package com.example.pizzawizza.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pizzawizza.data.CartItem;
import com.example.pizzawizza.data.Product;
import com.example.pizzawizza.data.User;

@Database(entities = {Product.class, CartItem.class, User.class}, version = 1)
//@TypeConverters(value = {MyTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract CartItemDao cartItemDao();
    public abstract UserDao userDao();
    private static volatile AppDatabase INSTANCE;
    
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            AppDatabase.class, "database_v4")
                            .fallbackToDestructiveMigration()
                            .fallbackToDestructiveMigrationOnDowngrade()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}