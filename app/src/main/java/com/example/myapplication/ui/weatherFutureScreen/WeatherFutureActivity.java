package com.example.myapplication.ui.weatherFutureScreen;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.Weather;
import com.example.myapplication.ui.BaseActivity;
import com.example.myapplication.ui.Fragment.Fragment_Chart;
import com.example.myapplication.ui.Fragment.Fragment_ListWeatherWeek;

import java.util.ArrayList;



public class WeatherFutureActivity extends BaseActivity {
    TextView txtCity;
    ArrayList<Weather> arrayListWeather;
    Button btnList, btnChart;
    String city;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_future);
        init();
        getDataIntent();
        txtCity.setText(city);
        addFragment(R.id.frameLayout, Fragment_ListWeatherWeek.newInstance(getIntent()),false);
        handleClick();
    }

    private void init() {
        btnList = (Button) findViewById(R.id.btnList);
        btnChart = (Button) findViewById(R.id.btnChart);
        txtCity = (TextView) findViewById(R.id.txtCity);
        arrayListWeather = new ArrayList<>();
    }

    private void handleClick(){
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.frameLayout, Fragment_ListWeatherWeek.newInstance(getIntent()),true);
            }
        });
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(R.id.frameLayout, Fragment_Chart.newInstance(arrayListWeather),false);
            }
        });
    }

    private void getDataIntent(){
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        if(city.equals("")){
            city = DEFAULT_CITY;
        }
    }

}
