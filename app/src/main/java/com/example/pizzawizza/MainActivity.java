package com.example.pizzawizza;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzawizza.adapter.ProductAdapter;
import com.example.pizzawizza.data.Product;
import com.example.pizzawizza.data.User;
import com.example.pizzawizza.data.room.AppDatabase;
import com.example.pizzawizza.fragments.DiscoverFragment;
import com.example.pizzawizza.fragments.FvtFragment;
import com.example.pizzawizza.fragments.HomeFragment;
import com.example.pizzawizza.fragments.ProfileFragment;
import com.example.pizzawizza.retrofit.APIClient;
import com.example.pizzawizza.retrofit.APIInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static String session = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = AppDatabase.getDatabase(this).userDao().getUser();
        session = user.getSession();
        APIClient.retrofit = null;


        findViewById(R.id.cartFab).setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
            overridePendingTransition(R.anim.up, R.anim.stay);
        });

        getSupportFragmentManager().beginTransaction().add(R.id.framelayout, new HomeFragment()).commit();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            for (Fragment fragment : getSupportFragmentManager().getFragments())
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            if (menuItem.getItemId() == R.id.home) {
                getSupportFragmentManager().beginTransaction().add(R.id.framelayout, new HomeFragment()).commit();
            }
            if (menuItem.getItemId() == R.id.fvt) {
                getSupportFragmentManager().beginTransaction().add(R.id.framelayout, new FvtFragment()).commit();
            }
            if (menuItem.getItemId() == R.id.discover) {
                getSupportFragmentManager().beginTransaction().add(R.id.framelayout, new DiscoverFragment()).commit();
            }
            if (menuItem.getItemId() == R.id.profile) {
                getSupportFragmentManager().beginTransaction().add(R.id.framelayout, new ProfileFragment()).commit();
            }

            return true;
        });

    }

}