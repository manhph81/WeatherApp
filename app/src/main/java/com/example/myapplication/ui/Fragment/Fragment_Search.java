package com.example.myapplication.ui.Fragment;

import android.app.Fragment;
import android.database.Cursor;
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
import com.example.myapplication.R;
import com.example.myapplication.database.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Fragment_Search extends Fragment {
    public static final String SQL_NAME = "weather.sqlite";
    public static final String API_ID = "77780b9269d06ce0066641430cd0645d";

    EditText edtSearch;
    Button btnOK;
    Database database;

    public static Fragment_Search newInstance() {

        Bundle args = new Bundle();

        Fragment_Search fragment = new Fragment_Search();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        handleClick();
    }

    private void handleClick(){
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = edtSearch.getText().toString();
                if(!content.equals("")){
                    getCurrentWeatherData(content);
                    hintKeyboad(edtSearch);
                }else{
                    Toast.makeText(getActivity(),"Enter the city",Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }
        });
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

            int a= (int) Math.round(Double.valueOf(maxTemp));
            int b= (int) Math.round(Double.valueOf(minTemp));

            writeDatabase(city,status,icon,String.valueOf(a),String.valueOf(b));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void writeDatabase(String city, String status, String icon, String maxTemp, String minTemp){
        database = new Database(getActivity(),SQL_NAME,null,1);
        database.QueryData("CREATE TABLE IF NOT EXISTS Weather(City varchar(200) primary key ,Status varchar(200),Icon varchar(200),MaxTemp varchar(200),MinTemp varchar(200))");
        //Insert
        if(!isExistsCity(city)){
            String sql = insertDatabase(city,status,icon,maxTemp,minTemp);
            database.QueryData(sql);
        }

        getActivity().onBackPressed();
    }

    private boolean isExistsCity(String city) {
        boolean result = false;
        Cursor data = database.GetData("SELECT * FROM Weather");
        while(data.moveToNext()){
            if(city.equals(data.getString(0))){
                result = true;
                break;
            }
        }
        return result;
    }

    private void hintKeyboad(EditText edt){
        edt.setCursorVisible(false);
        edt.setFocusableInTouchMode(false);
        edt.setFocusable(false);
    }

    private String delete(){
        String result = "DROP TABLE Weather";
        return result;
    }

    private String insertDatabase(String city, String status, String image, String maxTemp, String minTemp){
        String result="INSERT INTO Weather VALUES("+"\""+city+"\","+"\""+status+"\","+"\""+image+"\","+"\""+maxTemp+"\","+"\""+minTemp+"\""+")";
        return result;
    }

    private void getCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid="+API_ID;
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
