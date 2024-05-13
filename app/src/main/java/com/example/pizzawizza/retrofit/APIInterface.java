package com.example.pizzawizza.retrofit;


import com.example.pizzawizza.data.Product;
import com.example.pizzawizza.data.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("loadProducts.php")
    Call<List<Product>> loadProducts();

    @FormUrlEncoded
    @POST("saveOrUpdateProduct.php")
    Call<Product> saveOrUpdateProduct(@FieldMap Map<String, String> items);

    @FormUrlEncoded
    @POST("signUp.php")
    Call<User> signUp(@FieldMap Map<String, String> items,@Header(value = "Password") String password);

    @FormUrlEncoded
    @POST("login.php")
    Call<User> login(@FieldMap Map<String, String> items,@Header(value = "Password") String password);

}