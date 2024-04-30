package com.example.pizzawizza.viewHolders;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzawizza.databinding.ItemProductBinding;


public class ProductViewHolder extends RecyclerView.ViewHolder {
    public ItemProductBinding binding;

    public ProductViewHolder(ItemProductBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
