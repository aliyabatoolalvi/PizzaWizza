package com.example.pizzawizza;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pizzawizza.data.Product;
import com.example.pizzawizza.data.room.AppDatabase;
import com.example.pizzawizza.databinding.ActivityProductDetailsBinding;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {
    Product product;
    ActivityProductDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int id=getIntent().getIntExtra("id",-1);
        product= AppDatabase.getDatabase(this).productDao().getProductById(id);
        if(product==null){
            Toast.makeText(this,"No product found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        refresh();
    }

    public void refresh(){
        binding.productName.setText(product.getName());
        binding.productDetails.setText(product.getDetails());
        binding.productPrice.setText(product.getPrice()+" Rs.");
        Picasso.get().load("http://192.168.137.1/FoodOrdering/images/"+product.getPicture()).placeholder(R.drawable.welcome_pizza_img).into(binding.productImage);

        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });

    }
}