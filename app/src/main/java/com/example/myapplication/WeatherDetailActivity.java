package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class WeatherDetailActivity extends AppCompatActivity {

    Button btnWeatherFuture;
    TextView txtCity, txtState, txtTemperature, txtHumidity, txtCloud, txtWind, txtDate;
    ImageView imgIcon;
    String city="saigon";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_detail);
        init();
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        Log.d("input:",city);
        getCurrentWeatherData(city);
        btnWeatherFuture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherDetailActivity.this, WeatherFutureActivity.class);
                intent.putExtra("city",city);
                startActivity(intent);
            }
        });
    }
    private void loadData(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            String city = jsonObject.getString("name");
            txtCity.setText("City: "+getString(R.string.tab)+getString(R.string.tab)+city);
            //format Date
            String day = jsonObject.getString("dt");
            long l = Long.valueOf(day);
            Date date = new Date(l*1000L);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH-mm-ss");
            String Day = simpleDateFormat.format(date);
            txtDate.setText(Day);
            //load Array Weather
            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
            String status = jsonObjectWeather.getString("main");
            String icon = jsonObjectWeather.getString("icon");
            txtState.setText("Status:"+getString(R.string.tab) + status);
            //load Image
            Glide.with(this).load("http://openweathermap.org/img/wn/"+icon+".png").into(imgIcon);

            //load data Temp - Hum
            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
            String temperature = jsonObjectMain.getString("temp");
            String humidity = jsonObjectMain.getString("humidity");

            Double a = Double.valueOf(temperature);
            String temperatureFinal = String.valueOf(a.intValue());

            txtTemperature.setText(temperatureFinal+"°C");
            txtHumidity.setText(humidity+"%");
            // load Wind - Cloud
            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
            String wind = jsonObjectWind.getString("speed");
            txtWind.setText(wind+"m/s");
            JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
            String cloud = jsonObjectClouds.getString("all");
            txtCloud.setText(cloud+"%");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(WeatherDetailActivity.this);
        Log.d("abc","abc");
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=77780b9269d06ce0066641430cd0645d";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("city",response);
                        loadData(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }
    private void init() {
        btnWeatherFuture = (Button) findViewById(R.id.btnWeatherFuture);
        txtCity = (TextView) findViewById(R.id.textViewCity);
        txtState = (TextView) findViewById(R.id.textViewState);
        txtTemperature = (TextView) findViewById(R.id.textViewTemperature);
        txtHumidity = (TextView) findViewById(R.id.textViewHumidity);
        txtCloud = (TextView) findViewById(R.id.textViewCloud);
        txtWind = (TextView) findViewById(R.id.textViewWind);
        txtDate = (TextView) findViewById(R.id.textViewDate);
        imgIcon = (ImageView) findViewById(R.id.imageView);
    }

}
