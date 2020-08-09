package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherFutureActivity extends AppCompatActivity {
    TextView txtCity;
    private RecyclerView recyclerView;
    ArrayList<Weather> arrayListWeather;
    ItemAdapter itemAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_future);

        init();
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        String lon = intent.getStringExtra("lon");
        String lat = intent.getStringExtra("lat");
        if(city.equals("")){
            city = "saigon";
        }
        txtCity.setText(city);
        arrayListWeather = new ArrayList<>();
        getData(lon,lat);
        itemAdapter = new ItemAdapter(arrayListWeather,getApplicationContext());
        recyclerView.setAdapter(itemAdapter);

    }

    private void init() {
        txtCity = (TextView) findViewById(R.id.txtCity);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(this,linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);


    }

    private void getData(String lon,String lat){
        RequestQueue requestQueue = Volley.newRequestQueue(WeatherFutureActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&%20exclude=daily&appid=77780b9269d06ce0066641430cd0645d";
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
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArrayList = jsonObject.getJSONArray("daily");
            for (int i = 0; i < jsonArrayList.length() ; i++) {
                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);

                String day = jsonObjectList.getString("dt");
                long l = Long.valueOf(day);
                Date date = new Date(l*1000L);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDayFormat = new SimpleDateFormat("EEEE");
                String Day = simpleDateFormat.format(date);

                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                String status = jsonObjectWeather.getString("description");
                String icon = jsonObjectWeather.getString("icon");


                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                String max = jsonObjectTemp.getString("max");
                String min = jsonObjectTemp.getString("min");

                Double a= Double.valueOf(max)-Double.valueOf(270);
                Double b= Double.valueOf(max)-Double.valueOf(270);

                String tempMax = String.valueOf(a.intValue());
                String tempMin = String.valueOf(b.intValue());

                arrayListWeather.add(new Weather(Day,status,icon,tempMax,tempMin));


            }
            itemAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
