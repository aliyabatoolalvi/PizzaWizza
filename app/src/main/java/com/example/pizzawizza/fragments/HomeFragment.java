package com.example.pizzawizza.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pizzawizza.AddProductActivity;
import com.example.pizzawizza.MainActivity;
import com.example.pizzawizza.R;
import com.example.pizzawizza.adapter.ProductAdapter;
import com.example.pizzawizza.data.Product;
import com.example.pizzawizza.data.room.AppDatabase;
import com.example.pizzawizza.databinding.FragmentHomeBinding;
import com.example.pizzawizza.retrofit.APIClient;
import com.example.pizzawizza.retrofit.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    ProductAdapter adapter;
    List<Product> data = new ArrayList<>();
    FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ProductAdapter(getContext(), data);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<Product>> call1 = apiInterface.loadProducts();
        call1.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (getContext() == null) return;
                if (response.code() == 200) {
                    List<Product> products = response.body();
                    AppDatabase.getDatabase(getContext()).productDao().deleteAll();
                    AppDatabase.getDatabase(getContext()).productDao().insertOrReplaceAll(products);
                    Toast.makeText(getContext(), "Success: " + products.size(), Toast.LENGTH_SHORT).show();
                    refresh();
                } else {
                    Toast.makeText(getContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppDatabase.getDatabase(getContext()).cartItemDao().liveGetCount().observe(getActivity(), integer -> {
            refresh();
        });
        AppDatabase.getDatabase(getContext()).productDao().liveGetCount().observe(getActivity(), integer -> {
            if (getContext() != null)
                refresh();
        });

        refresh();

//        if (!AppDatabase.getDatabase(getContext()).userDao().getUser().getType().equals("Admin"))
//            binding.floatingActionButton.setVisibility(View.GONE);

        binding.floatingActionButton.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddProductActivity.class));
        });
    }

    public void refresh() {
        data.clear();
        data.addAll(AppDatabase.getDatabase(getContext()).productDao().getAll());
        adapter.notifyDataSetChanged();
    }
}