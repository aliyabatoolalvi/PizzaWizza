package com.example.pizzawizza.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzawizza.R;
import com.example.pizzawizza.data.Product;
import com.example.pizzawizza.viewHolders.NoteItemViewHolder;
import com.google.gson.Gson;

import java.util.List;

public class NoteItemAdapter extends RecyclerView.Adapter<NoteItemViewHolder> {

    Context context;
    List<Product> data;

    public NoteItemAdapter(Context context, List<Product> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public NoteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_note,parent,false);
        return new NoteItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteItemViewHolder holder, int position) {
        Product noteItem=data.get(position);
        holder.title.setText(noteItem.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
