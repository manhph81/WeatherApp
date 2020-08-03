package com.example.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    @NonNull
    ArrayList<Weather> weathers;
    Context context;

    public ItemAdapter(@NonNull ArrayList<Weather> weathers, Context context) {
        this.weathers = weathers;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_day,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtDate.setText(weathers.get(position).getDate());
        holder.txtState.setText(weathers.get(position).getStatus());
        holder.txtTempMax.setText(weathers.get(position).getMaxTemp());
        holder.txtTempMin.setText(weathers.get(position).getMinTemp());
//        Glide.with(R.layout.item_day).load("http://openweathermap.org/img/wn/"+weathers.get(position).getImage()+".png").into(holder.imgState);
    }

    @Override
    public int getItemCount() {
        return  weathers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate,txtState, txtTempMax, txtTempMin;
        ImageView imgState;
        public ViewHolder(View v) {
            super(v);

            txtDate = (TextView) v.findViewById(R.id.textViewDay);
            txtState = (TextView) v.findViewById(R.id.textViewState);
            txtTempMax = (TextView) v.findViewById(R.id.textViewMaxTemperature);
            txtTempMin = (TextView) v.findViewById(R.id.textViewMinTemperature);
            imgState = (ImageView) v.findViewById(R.id.imgState);
        }
    }
}
