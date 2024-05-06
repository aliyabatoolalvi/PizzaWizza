package com.example.pizzawizza;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pizzawizza.data.User;
import com.example.pizzawizza.data.room.AppDatabase;
import com.example.pizzawizza.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {
    ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        User user= AppDatabase.getDatabase(this).userDao().getUser();
        if (user!=null){
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }

        binding.register.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, SignupActivity.class));
            finish();
        });
        binding.login.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        });

    }
}