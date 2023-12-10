package com.example.travel_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travel_app.entities.Travel;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<Travel> dataList;

    public MyAdapter(Context context, List<Travel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getImageUrl()).into(holder.recImage);
        holder.recTitle.setText(dataList.get(position).getTitle());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getImageUrl());
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDescription());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("Key",dataList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("Latitude", dataList.get(holder.getAdapterPosition()).getLatitude());
                intent.putExtra("Longitude", dataList.get(holder.getAdapterPosition()).getLongitude());
                intent.putExtra("Country", dataList.get(holder.getAdapterPosition()).getCountry());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<Travel> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recTitle;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recTitle = itemView.findViewById(R.id.recTitle);
    }
}