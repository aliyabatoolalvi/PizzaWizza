package com.example.pizzawizza.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzawizza.R;
import com.example.pizzawizza.data.Product;
import com.example.pizzawizza.databinding.ItemProductBinding;
import com.example.pizzawizza.viewHolders.NoteItemViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NoteItemAdapter extends RecyclerView.Adapter<NoteItemViewHolder> {

    Context context;
    List<Product> data;

    public NoteItemAdapter(Context context, List<Product> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public NoteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding=ItemProductBinding.inflate(LayoutInflater.from(context),parent,false);
        return new NoteItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteItemViewHolder holder, int position) {
        Product noteItem=data.get(position);
        holder.binding.title.setText(noteItem.getName());
        holder.binding.details.setText(noteItem.getShortDescription());
        holder.binding.price.setText(noteItem.getPrice()+" RS");
        Picasso.get().load("http://192.168.137.1/FoodOrdering/images/"+noteItem.getPicture()).placeholder(R.drawable.welcome_pizza_img).into(holder.binding.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
