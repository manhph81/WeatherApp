package com.example.myapplication;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.database.Database;
import com.example.myapplication.ui.weatherDetailScreen.WeatherDetailActivity;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    @NonNull
    ArrayList<Weather> weathers;
    Context context;
    Database database;
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
        holder.txtTempMax.setText(weathers.get(position).getMaxTemp() + "°C");
        holder.txtTempMin.setText(weathers.get(position).getMinTemp() + "°C");

        final String city = weathers.get(position).getCity();
        Glide.with(context).load("http://openweathermap.org/img/wn/"+weathers.get(position).getImage()+".png").into(holder.imgState);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WeatherDetailActivity.class);
                intent.putExtra("city",city);
                context.startActivity(intent);
            }

        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                database = new Database(context,"weather.sqlite",null,1);
                database.QueryData("CREATE TABLE IF NOT EXISTS Weather(City varchar(200) primary key,Status varchar(200),Icon varchar(200),MaxTemp varchar(200),MinTemp varchar(200))");
                database.QueryData("DELETE FROM Weather WHERE City = \""+city+"\"");
                return false;
            }
        });

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
