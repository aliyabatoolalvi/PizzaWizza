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
import com.example.pizzawizza.databinding.ItemProductBinding;
import com.example.pizzawizza.retrofit.APIClient;
import com.example.pizzawizza.viewHolders.ProductViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    Context context;
    List<Product> data;

    public ProductAdapter(Context context, List<Product> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding=ItemProductBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product=data.get(position);
        holder.binding.title.setText(product.getName());
        holder.binding.details.setText(product.getShortDescription());
        holder.binding.price.setText(product.getPrice()+" RS");
        Picasso.get().load(APIClient.BASE_URL_IMAGES+product.getPicture()).placeholder(R.drawable.welcome_pizza_img).into(holder.binding.image);

        CartItem cartItem= AppDatabase.getDatabase(context).cartItemDao().getByProductId(product.getId());
        if (cartItem == null) {
            holder.binding.cartIcon.setVisibility(View.INVISIBLE);
            holder.binding.cartCount.setVisibility(View.INVISIBLE);
        }else {
            holder.binding.cartIcon.setVisibility(View.VISIBLE);
            holder.binding.cartCount.setVisibility(View.VISIBLE);
            holder.binding.cartCount.setText(cartItem.getQuantity()+"");
        }

        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("id",product.getId());
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
