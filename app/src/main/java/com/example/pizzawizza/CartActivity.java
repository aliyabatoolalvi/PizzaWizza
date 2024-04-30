package com.example.pizzawizza;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzawizza.adapter.CartProductAdapter;
import com.example.pizzawizza.adapter.ProductAdapter;
import com.example.pizzawizza.data.Product;
import com.example.pizzawizza.data.room.AppDatabase;
import com.example.pizzawizza.data.room.CartProduct;
import com.example.pizzawizza.databinding.ActivityCartBinding;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    CartProductAdapter adapter;
    List<CartProduct> data = new ArrayList<>();
    ActivityCartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new CartProductAdapter(this, data);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);
        refresh();

        binding.back.setOnClickListener(v -> onBackPressed());
    }

    public void refresh(){
        data.clear();
        data.addAll(AppDatabase.getDatabase(this).cartItemDao().getAllCartProducts());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay,R.anim.down);
    }
}