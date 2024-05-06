package com.example.pizzawizza;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pizzawizza.data.User;
import com.example.pizzawizza.data.room.AppDatabase;
import com.example.pizzawizza.databinding.ActivityLoginBinding;
import com.example.pizzawizza.retrofit.APIClient;
import com.example.pizzawizza.retrofit.APIInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.loginBtn.setOnClickListener(v -> {

            //validate
            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
            Map<String, String> map = new HashMap<>();
            map.put("phone", binding.username.getEditText().getText().toString());
            Call<User> call1 = apiInterface.login(map, binding.password.getEditText().getText().toString());
            call1.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        User user = response.body();
                        String session = response.headers().get("session");
                        user.setSession(session);

                        AppDatabase.getDatabase(LoginActivity.this).userDao().insertOrReplace(user);
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.register.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);


    }
}