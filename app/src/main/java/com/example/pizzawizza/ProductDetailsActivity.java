package com.example.pizzawizza;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pizzawizza.data.CartItem;
import com.example.pizzawizza.data.Product;
import com.example.pizzawizza.data.room.AppDatabase;
import com.example.pizzawizza.databinding.ActivityProductDetailsBinding;
import com.example.pizzawizza.retrofit.APIClient;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {
    Product product;
    CartItem cartItem;
    AppDatabase appDatabase;
    ActivityProductDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int id = getIntent().getIntExtra("id", -1);
        appDatabase = AppDatabase.getDatabase(this);
        product = appDatabase.productDao().getProductById(id);
        if (product == null) {
            Toast.makeText(this, "No product found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding.addCart.setOnClickListener(v -> {
            cartItem = new CartItem();
            cartItem.setProductId(product.getId());
            cartItem.setQuantity(1);
            appDatabase.cartItemDao().insertOrReplace(cartItem);
            refreshCart();
        });

        refresh();

        appDatabase.productDao().getProductByIdLive(product.getId()).observe(this, product1 -> {
            if (product1 != null) product = product1;
            refresh();
        });

//        if (!AppDatabase.getDatabase(this).userDao().getUser().getType().equals("Admin"))
//            binding.floatingActionButton.setVisibility(View.GONE);

        binding.floatingActionButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AddProductActivity.class).putExtra("data", new Gson().toJson(product)));
        });
    }

    public void refresh() {
        binding.productName.setText(product.getName());
        binding.productDetails.setText(product.getDetails());
        binding.productPrice.setText(product.getPrice() + " Rs.");
        Picasso.get().load(APIClient.BASE_URL_IMAGES + product.getPicture()).placeholder(R.drawable.welcome_pizza_img).into(binding.productImage);

        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.plus.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            appDatabase.cartItemDao().insertOrReplace(cartItem);
            refreshCart();
        });

        binding.minus.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            if (cartItem.getQuantity() <= 0)
                appDatabase.cartItemDao().delete(cartItem);
            else
                appDatabase.cartItemDao().insertOrReplace(cartItem);
            refreshCart();
        });

        refreshCart();

    }

    public void refreshCart() {
        cartItem = appDatabase.cartItemDao().getByProductId(product.getId());
        if (cartItem == null) {
            binding.addCart.setVisibility(View.VISIBLE);
            binding.plus.setVisibility(View.INVISIBLE);
            binding.minus.setVisibility(View.INVISIBLE);
            binding.quantity.setVisibility(View.INVISIBLE);
        } else {
            binding.addCart.setVisibility(View.INVISIBLE);
            binding.plus.setVisibility(View.VISIBLE);
            binding.minus.setVisibility(View.VISIBLE);
            binding.quantity.setVisibility(View.VISIBLE);
            binding.quantity.setText(cartItem.getQuantity() + "");
        }

        binding.cartCount.setText(appDatabase.cartItemDao().getCount() + "");
    }
}