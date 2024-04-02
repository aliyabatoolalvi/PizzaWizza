package com.example.pizzawizza.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzawizza.R;
import com.example.pizzawizza.databinding.ItemProductBinding;


public class NoteItemViewHolder extends RecyclerView.ViewHolder {
    public ItemProductBinding binding;

    public NoteItemViewHolder(ItemProductBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
