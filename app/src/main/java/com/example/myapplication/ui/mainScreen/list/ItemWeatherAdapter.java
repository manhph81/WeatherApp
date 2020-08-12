package com.example.myapplication.ui.mainScreen.list;



import android.content.Context;
import android.graphics.Color;
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

public class ItemWeatherAdapter extends RecyclerView.Adapter<ItemWeatherAdapter.ViewHolder> {

    public interface ItemClickListener {
        void onClick(int position);

        void onLongClick(int position);
    }

    @NonNull
    private ArrayList<Weather> weathers;
    private Context context;
    private ItemClickListener itemClickListener;

    public ItemWeatherAdapter(@NonNull ArrayList<Weather> weathers, Context context, ItemClickListener itemClickListener) {
        this.weathers = weathers;
        this.context = context;
        this.itemClickListener = itemClickListener;
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
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onClick(getAdapterPosition());
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemClickListener.onLongClick(getAdapterPosition());
                    return false;
                }
            });
        }

        public void bind(int position) {
            String day = weathers.get(position).getDate();
            if(isEvenDay(day)){
                linearLayout.setBackgroundColor(Color.WHITE);
            }else{
                linearLayout.setBackgroundColor(Color.RED);
            }
            txtDate.setText(weathers.get(position).getCity());
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
