package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class WeatherFutureActivity extends AppCompatActivity {
    TextView txtCity;
    ImageView imgState;
    ListView lv;
    ArrayAdapter itemAdapter;
    ArrayList<Weather> arrayListWeather;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_future);
        init();
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        if(city.equals("")){
            city = "saigon";
        }
        txtCity.setText("City: " + city);

        getData(city);
    }

    private void init() {
        txtCity = (TextView) findViewById(R.id.txtContent);
        imgState = (ImageView) findViewById(R.id.imgState);
        lv = (ListView) findViewById(R.id.listView);
        arrayListWeather = new ArrayList<Weather>();
        ArrayAdapter itemAdapter = new ArrayAdapter(WeatherFutureActivity.this,R.layout.item_day,arrayListWeather);
//        itemAdapter = new ItemAdapter(WeatherFutureActivity.this,R.layout.item_day,arrayListWeather);
        lv.setAdapter(itemAdapter);
    }

    private void getData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(WeatherFutureActivity.this);
        String url = "https://samples.openweathermap.org/data/2.5/forecast/daily?q="+data+"&appid=77780b9269d06ce0066641430cd0645d";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("result",response);
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

    private void loadData(String response) {
    }
}
