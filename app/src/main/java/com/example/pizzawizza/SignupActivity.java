package com.example.pizzawizza;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzawizza.data.Product;
import com.example.pizzawizza.data.User;
import com.example.pizzawizza.data.room.AppDatabase;
import com.example.pizzawizza.databinding.ActivitySignupBinding;
import com.example.pizzawizza.retrofit.APIClient;
import com.example.pizzawizza.retrofit.APIInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.signupBtn.setOnClickListener(v -> {

            //validate
            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
            Map<String, String> map = new HashMap<>();
            map.put("email", "");
            map.put("phone", binding.phone.getEditText().getText().toString());
            map.put("name", binding.username.getEditText().getText().toString());
            Call<User> call1 = apiInterface.signUp(map, binding.password.getEditText().getText().toString());
            call1.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        User user = response.body();
                        String session = response.headers().get("session");
                        user.setSession(session);

                        AppDatabase.getDatabase(SignupActivity.this).userDao().insertOrReplace(user);
                        Toast.makeText(SignupActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(SignupActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.login.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

    }
}