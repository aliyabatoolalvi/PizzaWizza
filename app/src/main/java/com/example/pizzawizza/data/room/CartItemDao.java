package com.example.pizzawizza.data.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pizzawizza.data.CartItem;

import java.util.List;

@Dao
public interface CartItemDao {
    @Query("SELECT * FROM CartItem")
    List<CartItem> getAll();
    @Query("SELECT * FROM CartItem limit 500")
    LiveData<List<CartItem>> liveGetAll();
    @Query("SELECT count(*) FROM CartItem limit 500")
    LiveData<Integer> liveGetCount();


    @Query("SELECT * FROM CartItem,Product where Product.id=CartItem.productId")
    List<CartProduct> getAllCartProducts();


    @Query("SELECT * FROM CartItem where productId=:pid")
    CartItem getByProductId(int pid);
    @Query("SELECT count(*) FROM CartItem")
    int getCount();

    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOrReplace(CartItem CartItem);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceAll(List<CartItem> CartItems);
    
    @Delete
    void delete(CartItem blogPost);
    
    @Update
    void update(CartItem blogPost);
    
    @Query("DELETE FROM CartItem")
    void deleteAll();

}