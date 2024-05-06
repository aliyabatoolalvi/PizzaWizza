package com.example.pizzawizza;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzawizza.adapter.ProductAdapter;
import com.example.pizzawizza.data.Product;
import com.example.pizzawizza.data.User;
import com.example.pizzawizza.data.room.AppDatabase;
import com.example.pizzawizza.retrofit.APIClient;
import com.example.pizzawizza.retrofit.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<Product> data = new ArrayList<>();
    public static String session="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user= AppDatabase.getDatabase(this).userDao().getUser();
        session=user.getSession();
        APIClient.retrofit=null;


        adapter = new ProductAdapter(this, data);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        refresh();

        findViewById(R.id.cartFab).setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
            overridePendingTransition(R.anim.up, R.anim.stay);
        });

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<Product>> call1 = apiInterface.loadProducts();
        call1.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.code() == 200) {
                    List<Product> products = response.body();
                    AppDatabase.getDatabase(MainActivity.this).productDao().deleteAll();
                    AppDatabase.getDatabase(MainActivity.this).productDao().insertOrReplaceAll(products);
                    Toast.makeText(MainActivity.this, "Success: " + products.size(), Toast.LENGTH_SHORT).show();
                    refresh();
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
                ;

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppDatabase.getDatabase(this).cartItemDao().liveGetCount().observe(this, integer -> {
            refresh();
        });

    }

    public void refresh() {
        data.clear();
        data.addAll(AppDatabase.getDatabase(this).productDao().getAll());
        adapter.notifyDataSetChanged();
    }

}