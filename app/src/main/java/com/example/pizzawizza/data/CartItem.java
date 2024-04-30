
package com.example.pizzawizza.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CartItem {
    @PrimaryKey
    int productId;
    int quantity;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
