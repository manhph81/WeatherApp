package com.example.myapplication;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {
    Context context;
    ArrayList<Weather> arrayList;

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_day,null);

        Weather weather = arrayList.get(i);

        TextView txtDay = (TextView )view.findViewById(R.id.textViewDay);
        TextView txtState = (TextView )view.findViewById(R.id.textViewState);
        TextView txtMaxTemperature = (TextView )view.findViewById(R.id.textViewMaxTemperature);
        TextView txtMinTemperature = (TextView )view.findViewById(R.id.textViewMinTemperature);
        ImageView imgState = (ImageView) view.findViewById(R.id.imgState);

        txtDay.setText(Weather.date);
        txtState.setText(Weather.state);
        txtMaxTemperature.setText(Weather.maxTemp + "°C");
        txtMinTemperature.setText(Weather.minTemp+ "°C");



        return view;
    }
}
