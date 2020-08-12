package com.example.myapplication.ui.weatherDetailScreen;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.ui.BaseActivity;
import com.example.myapplication.ui.weatherFutureScreen.WeatherFutureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class WeatherDetailActivity extends BaseActivity {
    private Button btnWeatherFuture;
    private TextView txtCity, txtState, txtTemperature, txtHumidity, txtCloud, txtWind, txtDate;
    private ImageView imgIcon;
    private String lon = "";
    private String lat = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_detail);
        init();
        getCurrentWeatherDataOneDay(getDataIntentCity());
        handleClick();
    }

    private void loadData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String city = jsonObject.getString("name");

            //format Date
            String day = jsonObject.getString("dt");
            long l = Long.valueOf(day);
            Date date = new Date(l * 1000L);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String Day = simpleDateFormat.format(date);

            //load Array Weather
            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
            String status = jsonObjectWeather.getString("main");
            String icon = jsonObjectWeather.getString("icon");

            //load Image


            //load data Temp - Hum
            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
            String temperature = jsonObjectMain.getString("temp");
            String humidity = jsonObjectMain.getString("humidity");

            Double a = Double.valueOf(temperature);
            String temperatureFinal = String.valueOf(a.intValue());

            // load Wind - Cloud
            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
            String wind = jsonObjectWind.getString("speed");

            JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
            String cloud = jsonObjectClouds.getString("all");


            JSONObject jsonObjectCoord = jsonObject.getJSONObject("coord");
            lon = jsonObjectCoord.getString("lon");
            lat = jsonObjectCoord.getString("lat");

            setView(city, Day, status, icon, temperatureFinal, humidity, wind, cloud);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setView(String city, String day, String status, String icon, String temperatureFinal, String humidity, String wind, String cloud) {
        txtCity.setText("City: " + getString(R.string.tab) + getString(R.string.tab) + city);
        txtDate.setText(day);
        txtState.setText("Status:" + getString(R.string.tab) + status);
        txtTemperature.setText(temperatureFinal + "°C");
        txtHumidity.setText(humidity + "%");
        txtWind.setText(wind + "m/s");
        txtCloud.setText(cloud + "%");
        Glide.with(this).load("http://openweathermap.org/img/wn/" + icon + ".png").into(imgIcon);
    }

    private void getCurrentWeatherDataOneDay(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(WeatherDetailActivity.this);
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=" + API_ID;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(WeatherDetailActivity.this, "City is not found.", Toast.LENGTH_SHORT).show();
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

    private void handleClick() {
        btnWeatherFuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherDetailActivity.this, WeatherFutureActivity.class);
                intent.putExtra("city", getDataIntentCity());
                intent.putExtra("lon", lon);
                intent.putExtra("lat", lat);
                startActivity(intent);
            }
        });
    }

    private String getDataIntentCity() {
        Intent intent = getIntent();
        String data = intent.getStringExtra("city");
        if (data == "") {
            data = DEFAULT_CITY;
        }
        return data;
    }

}
