package com.example.myapplication.ui.weatherFutureScreen;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.ui.BaseActivity;
import com.example.myapplication.ui.weatherFutureScreen.chart.ChartFragment;
import com.example.myapplication.ui.weatherFutureScreen.list.ListWeatherWeekFragment;


public class WeatherFutureActivity extends BaseActivity {
    private TextView txtCity;
    private Button btnList, btnChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_future);
        init();
        txtCity.setText(getCity());
        addFragment(R.id.frameLayout, ListWeatherWeekFragment.newInstance(), false);
        handleClick();
    }


    public String getLat() {
        Intent intent = getIntent();
        return intent.getStringExtra("lat");
    }

    public String getLon() {
        Intent intent = getIntent();
        return intent.getStringExtra("lon");
    }

    private void init() {
        btnList = (Button) findViewById(R.id.btnList);
        btnChart = (Button) findViewById(R.id.btnChart);
        txtCity = (TextView) findViewById(R.id.txtCity);
    }

    private void handleClick() {
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.frameLayout, ListWeatherWeekFragment.newInstance(), false);
            }
        });
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.frameLayout, ChartFragment.newInstance(), false);
            }
        });
    }

    private String getCity() {
        Intent intent = getIntent();
        if (intent.getStringExtra("city").equals("")) {
            return DEFAULT_CITY;
        }
        return intent.getStringExtra("city");
    }
}
