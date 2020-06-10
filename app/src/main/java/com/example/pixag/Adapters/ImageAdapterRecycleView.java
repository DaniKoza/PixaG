package com.example.pixag.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.pixag.R;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapterRecycleView extends RecyclerView.Adapter<ImageAdapterRecycleView.MyViewHolder> {
    private Context mContext;
    private HashMap<Integer, HashMap<String, String>> imagesData;

    public ImageAdapterRecycleView(Context context, HashMap<Integer, HashMap<String, String>> imagesData) {
        this.mContext = context;
        this.imagesData = imagesData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picture_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String currentUrl = imagesData.get(position).get("ImageUrl");
        Glide.with(mContext).load(currentUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imagesData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
        }
    }

}
