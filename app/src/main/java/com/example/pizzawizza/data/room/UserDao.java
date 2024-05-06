package com.example.pizzawizza.data.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pizzawizza.data.CartItem;
import com.example.pizzawizza.data.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User limit 1")
    User getUser();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOrReplace(User CartItem);
    
    @Query("DELETE FROM User")
    void deleteAll();

}