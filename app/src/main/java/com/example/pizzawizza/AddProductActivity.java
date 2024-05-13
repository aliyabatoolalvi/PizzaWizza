package com.example.pizzawizza;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pizzawizza.data.Product;
import com.example.pizzawizza.data.Product;
import com.example.pizzawizza.data.room.AppDatabase;
import com.example.pizzawizza.databinding.ActivityAddProductBinding;
import com.example.pizzawizza.retrofit.APIClient;
import com.example.pizzawizza.retrofit.APIInterface;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    ActivityAddProductBinding binding;
    Product product=new Product();
    boolean isForEdit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra("data")) {
            product = new Gson().fromJson(getIntent().getStringExtra("data"), Product.class);
            isForEdit=true;

            binding.productName.getEditText().setText(product.getName());
            binding.shortDetails.getEditText().setText(product.getShortDescription());
            binding.description.getEditText().setText(product.getDetails());
            binding.price.getEditText().setText(product.getPrice()+"");
            binding.discountPrice.getEditText().setText(product.getDiscountedPrice()+"");
            binding.category.getEditText().setText(product.getCategory());
            binding.subCategory.getEditText().setText(product.getSubCategory());
        }

        binding.saveBtn.setOnClickListener(v -> {
            product.setName(binding.productName.getEditText().getText().toString());
            product.setDetails(binding.description.getEditText().getText().toString());
            product.setShortDescription(binding.shortDetails.getEditText().getText().toString());
            product.setPrice(Integer.parseInt(binding.price.getEditText().getText().toString()));
            product.setDiscountedPrice(Integer.parseInt(binding.discountPrice.getEditText().getText().toString()));
            product.setCategory(binding.category.getEditText().getText().toString());
            product.setSubCategory(binding.subCategory.getEditText().getText().toString());

            if (!isForEdit) product.setId(-1);

            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Please wait while we save product...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            Map<String, String> map = objectMapper
                    .convertValue(product, new TypeReference<Map<String, String>>() {});
            Call<Product> call1 = apiInterface.saveOrUpdateProduct(map);
            call1.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        Product Product = response.body();

                        AppDatabase.getDatabase(AddProductActivity.this).productDao().insertOrReplace(Product);
                        Toast.makeText(AddProductActivity.this, "Data saved", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddProductActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(AddProductActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


    }
}