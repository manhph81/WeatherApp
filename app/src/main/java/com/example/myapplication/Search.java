package com.example.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Search extends Fragment {
    EditText edtSearch;
    Button btnOK;
    Database database;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        init(view);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = edtSearch.getText().toString();
                if(!content.equals("")){
                    getCurrentWeatherData(content);
                }else{
                    Toast.makeText(getActivity(),"Enter the city",Toast.LENGTH_SHORT).show();
                }

                removeFrag();


            }
        });
        return view;
    }
    private void loadData(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            String city = jsonObject.getString("name");
            //format Date
            String day = jsonObject.getString("dt");
            long l = Long.valueOf(day);
            Date date = new Date(l*1000L);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH-mm-ss");

            String Day = simpleDateFormat.format(date);

            //load Array Weather
            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
            String status = jsonObjectWeather.getString("main");
            String icon = jsonObjectWeather.getString("icon");
            //
            JSONObject jsonObjectTemp = jsonObject.getJSONObject("main");
            String maxTemp = jsonObjectTemp.getString("temp_max");
            String minTemp = jsonObjectTemp.getString("temp_min");



            database = new Database(getActivity(),"weather.sqlite",null,1);
            database.QueryData("CREATE TABLE IF NOT EXISTS Weather(City varchar(200) primary key ,Status varchar(200),Icon varchar(200),MaxTemp varchar(200),MinTemp varchar(200))");
            //Insert
            String sql = insert(city,status,icon,maxTemp,minTemp);
            database.QueryData(sql);

            removeFrag();



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void removeFrag() {
        Toast.makeText(getActivity(),"Remove",Toast.LENGTH_SHORT).show();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Search search = (Search) getFragmentManager().findFragmentByTag("fragSearch");
        fragmentTransaction.remove(search);
    }
    private String delete(){
        String result = "DROP TABLE Weather";
        return result;
    }
    private String insert(String city, String status, String image, String maxTemp, String minTemp){
        String result="INSERT INTO Weather VALUES("+"\""+city+"\","+"\""+status+"\","+"\""+image+"\","+"\""+maxTemp+"\","+"\""+minTemp+"\""+")";
        return result;
    }
    private void getCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                        Toast.makeText(getActivity(),"The city is not found",Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(stringRequest);
    }
    private void init(View view) {
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);
        btnOK = (Button) view.findViewById(R.id.btnOK);
    }

}
