package com.example.pizzawizza.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzawizza.R;


public class NoteItemViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView details;
    public TextView date;
    public ImageView fvt;
    public LinearLayout post;
    public NoteItemViewHolder(@NonNull View itemView) {
        super(itemView);
        title=itemView.findViewById(R.id.title);
        details =itemView.findViewById(R.id.details);
        date=itemView.findViewById(R.id.date);
        fvt=itemView.findViewById(R.id.fvt);
        post=itemView.findViewById(R.id.post);
    }
}
