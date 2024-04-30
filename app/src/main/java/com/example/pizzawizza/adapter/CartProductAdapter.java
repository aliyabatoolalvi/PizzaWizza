package com.example.pizzawizza.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzawizza.ProductDetailsActivity;
import com.example.pizzawizza.R;
import com.example.pizzawizza.data.CartItem;
import com.example.pizzawizza.data.Product;
import com.example.pizzawizza.data.room.AppDatabase;
import com.example.pizzawizza.data.room.CartProduct;
import com.example.pizzawizza.databinding.ItemCartProductBinding;
import com.example.pizzawizza.databinding.ItemProductBinding;
import com.example.pizzawizza.viewHolders.CartProductViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductViewHolder> {

    Context context;
    List<CartProduct> data;

    public CartProductAdapter(Context context, List<CartProduct> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public CartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartProductBinding binding= ItemCartProductBinding.inflate(LayoutInflater.from(context),parent,false);
        return new CartProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductViewHolder holder, int position) {
        CartProduct cartProduct=data.get(position);
        holder.binding.title.setText(cartProduct.product.getName());
        holder.binding.details.setText(cartProduct.product.getShortDescription());
        holder.binding.price.setText(cartProduct.product.getPrice()+" RS");
        Picasso.get().load("http://192.168.137.1/FoodOrdering/images/"+cartProduct.product.getPicture()).placeholder(R.drawable.welcome_pizza_img).into(holder.binding.image);
        holder.binding.quantity.setText(cartProduct.cartItem.getQuantity()+"");

        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("id",cartProduct.product.getId());
            Pair<View,String> p1 = Pair.create(holder.binding.image, "image");
            Pair<View,String> p2 = Pair.create(holder.binding.title, "title");
            Pair<View,String> p3 = Pair.create(holder.binding.price, "price");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, p1,p2,p3);
            context.startActivity(intent, options.toBundle());

        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
