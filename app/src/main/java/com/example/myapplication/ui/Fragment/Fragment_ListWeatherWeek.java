package com.example.myapplication.ui.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.ItemAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Weather;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Fragment_ListWeatherWeek extends Fragment {
    public static final String API_ID = "77780b9269d06ce0066641430cd0645d";
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private ArrayList arrayListWeatherWeek;
    private static String lon,lat;

    public static Fragment_ListWeatherWeek newInstance(Intent intent) {
        Bundle args = new Bundle();
        Fragment_ListWeatherWeek fragment = new Fragment_ListWeatherWeek();
        lon = intent.getStringExtra("lon");
        lat = intent.getStringExtra("lat");
        fragment.setArguments(args);

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_list, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getDataWeek(lon,lat);

    }
    @Override
    public void onResume() {
        super.onResume();
    }
    private void handleClick() {
    }
    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        initRecyclerView();
    }
    protected void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity().getApplicationContext(),linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);
        arrayListWeatherWeek = new ArrayList();
        itemAdapter = new ItemAdapter(arrayListWeatherWeek,getActivity().getApplicationContext());
        recyclerView.setAdapter(itemAdapter);
    }
    private void getDataWeek(String lon,String lat){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&%20exclude=daily&appid="+API_ID;
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

                int a= (int) Math.round(Double.valueOf(max)-Double.valueOf(270));
                int b= (int) Math.round(Double.valueOf(min)-Double.valueOf(270));

                String tempMax = String.valueOf(a);
                String tempMin = String.valueOf(b);

                arrayListWeatherWeek.add(new Weather(Day,status,icon,tempMax,tempMin));
                itemAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}