package com.example.pizzawizza;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzawizza.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.signupBtn.setOnClickListener(v -> {
            startActivity(new Intent(this,MainActivity.class));
            finish();
            });
            binding.login.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

    }
}