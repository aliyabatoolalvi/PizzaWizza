package com.example.pizzawizza.data.room;

import androidx.room.Embedded;

import com.example.pizzawizza.data.CartItem;
import com.example.pizzawizza.data.Product;

public class CartProduct {
    @Embedded
    public CartItem cartItem;
    @Embedded
    public Product product;
}
