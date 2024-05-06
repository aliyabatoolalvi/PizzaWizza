package com.example.pizzawizza.retrofit;
import com.example.pizzawizza.MainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    
    public static Retrofit retrofit = null;
    public static final String BASE_URL="https://code-robust.com/FoodOrdering/api/";
    public static final String BASE_URL_IMAGES="https://code-robust.com/FoodOrdering/images/";

    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Interceptor authIntercepter=chain -> {
            return chain.proceed(chain.request().newBuilder().addHeader("session", MainActivity.session).build());
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(authIntercepter)
                .addInterceptor(interceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS).build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        
        
        return retrofit;
    }
}

