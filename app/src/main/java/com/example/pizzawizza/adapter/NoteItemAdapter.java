package com.example.pizzawizza.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzawizza.ProductDetailsActivity;
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
        Product product=data.get(position);
        holder.binding.title.setText(product.getName());
        holder.binding.details.setText(product.getShortDescription());
        holder.binding.price.setText(product.getPrice()+" RS");
        Picasso.get().load("http://192.168.137.1/FoodOrdering/images/"+product.getPicture()).placeholder(R.drawable.welcome_pizza_img).into(holder.binding.image);

        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("id",product.getId());
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((Activity) context, holder.binding.image, "image")
                    ;
            context.startActivity(intent, options.toBundle());

        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
