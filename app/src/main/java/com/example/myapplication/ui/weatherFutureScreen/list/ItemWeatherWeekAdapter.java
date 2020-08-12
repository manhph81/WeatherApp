package com.example.myapplication.ui.weatherFutureScreen.list;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.Weather;

import java.util.ArrayList;

public class ItemWeatherWeekAdapter extends RecyclerView.Adapter<ItemWeatherWeekAdapter.ViewHolder> {

    @NonNull
    private ArrayList<Weather> weathers;
    private Context context;

    public ItemWeatherWeekAdapter(@NonNull ArrayList<Weather> weathers, Context context) {
        this.weathers = weathers;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_day, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private TextView txtDate, txtState, txtTempMax, txtTempMin;
        private ImageView imgState;

        public ViewHolder(View v) {
            super(v);
            linearLayout = (LinearLayout) v.findViewById(R.id.LinearLayoutItem);
            txtDate = (TextView) v.findViewById(R.id.textViewDay);
            txtState = (TextView) v.findViewById(R.id.textViewState);
            txtTempMax = (TextView) v.findViewById(R.id.textViewMaxTemperature);
            txtTempMin = (TextView) v.findViewById(R.id.textViewMinTemperature);
            imgState = (ImageView) v.findViewById(R.id.imgState);
        }

        public void bind(int position) {
            String day = weathers.get(position).getDate();
            if(isEvenDay(day)){
                linearLayout.setBackgroundColor(Color.GREEN);
            }else{
                linearLayout.setBackgroundColor(Color.BLUE);
            }
            txtDate.setText(day);
            txtState.setText(weathers.get(position).getStatus());
            txtTempMax.setText(weathers.get(position).getMaxTemp() + "°C");
            txtTempMin.setText(weathers.get(position).getMinTemp() + "°C");
            Glide.with(context).load("http://openweathermap.org/img/wn/" + weathers.get(position).getImage() + ".png").into(imgState);
        }
    }
    private boolean isEvenDay(String day){
        boolean result;
        String[] arr = day.split("-");
        int index = arr.length-1;
        int a = Integer.parseInt(arr[index]);
        if(a%2==0){
            result=true;
        }else{
            result=false;
        }
        return result;
    }
}
