package com.example.pizzawizza.viewHolders;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzawizza.databinding.ItemCartProductBinding;
import com.example.pizzawizza.databinding.ItemProductBinding;


public class CartProductViewHolder extends RecyclerView.ViewHolder {
    public ItemCartProductBinding binding;

    public CartProductViewHolder(ItemCartProductBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
