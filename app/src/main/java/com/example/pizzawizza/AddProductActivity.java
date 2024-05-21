package com.example.pizzawizza;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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
import com.example.pizzawizza.utils.MediaUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    ActivityAddProductBinding binding;
    Product product=new Product();
    MediaUtils mediaUtils;
    boolean isForEdit=false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mediaUtils=new MediaUtils(this,uri -> {
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
            binding.image.setImageURI(uri);
        });
        mediaUtils.setAllowFromGallery(true);

        if (getIntent().hasExtra("data")) {
            product = new Gson().fromJson(getIntent().getStringExtra("data"), Product.class);
            isForEdit=true;

            Picasso.get().load(APIClient.BASE_URL_IMAGES + product.getPicture()).placeholder(R.drawable.welcome_pizza_img).into(binding.image);
            binding.productName.getEditText().setText(product.getName());
            binding.shortDetails.getEditText().setText(product.getShortDescription());
            binding.description.getEditText().setText(product.getDetails());
            binding.price.getEditText().setText(product.getPrice()+"");
            binding.discountPrice.getEditText().setText(product.getDiscountedPrice()+"");
            String[] cats = getResources().getStringArray(R.array.categories);
            for (int i = 0; i < cats.length; i++) {
                if (cats[i].equals(product.getCategory())) {
                    binding.categorySpinner.setSelection(i);
                    break;
                }
            }
            binding.subCategoryAutocomplete.setText(product.getSubCategory());
        }

        binding.chooseImage.setOnClickListener(v -> {
            mediaUtils.startImageSelectionActivity();
        });

        List<String> subCats=AppDatabase.getDatabase(this).productDao().getAllSubCats();
        ArrayAdapter<String> suggestionsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, subCats);
        binding.subCategoryAutocomplete.setAdapter(suggestionsAdapter);

        binding.saveBtn.setOnClickListener(v -> {
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Please wait while we save product...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            if (mediaUtils.choosedImageuri==null){
                uploadProductData();
                return;
            }
            uploadImage(mediaUtils.getSelectedImagePath());


        });


    }

    public void uploadImage(String path){
        File file = new File(path);
        RequestBody fileReqBody = RequestBody.create(file, MediaType.parse("image/*"));
        MultipartBody.Part part = MultipartBody.Part.createFormData("filename", System.currentTimeMillis()+".jpg", fileReqBody);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResponseBody> call1 = apiInterface.uploadPicture(part);
        call1.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String url = response.body().string();
                        product.setPicture(url);
                        uploadProductData();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    try {
                        File fdelete = file;
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                            }
                        }
                    } catch (Exception e) {

                    }
                } else Toast.makeText(AddProductActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void uploadProductData(){
        if (binding.categorySpinner.getSelectedItemPosition()==0){
            Toast.makeText(this, "Select category", Toast.LENGTH_SHORT).show();
            return;
        }

        product.setName(binding.productName.getEditText().getText().toString());
        product.setDetails(binding.description.getEditText().getText().toString());
        product.setShortDescription(binding.shortDetails.getEditText().getText().toString());
        product.setPrice(Integer.parseInt(binding.price.getEditText().getText().toString()));
        product.setDiscountedPrice(Integer.parseInt(binding.discountPrice.getEditText().getText().toString()));
        product.setCategory(binding.categorySpinner.getSelectedItem().toString());
        product.setSubCategory(binding.subCategoryAutocomplete.getText().toString());

        if (!isForEdit) product.setId(-1);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaUtils.checkActivityResult(requestCode, resultCode, data);
    }
}