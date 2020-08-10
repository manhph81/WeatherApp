package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class WeatherFutureActivity extends AppCompatActivity {
    TextView txtCity;
    private RecyclerView recyclerView;
    ArrayList<Weather> arrayListWeather;
    ItemAdapter itemAdapter;
    Button btnList, btnChart;
    AnyChartView anyChartView;
    ArrayList<CustomDataEntry> seriesData;
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
        seriesData = new ArrayList<>();

        getData(lon,lat);
        itemAdapter = new ItemAdapter(arrayListWeather,getApplicationContext());
        recyclerView.setAdapter(itemAdapter);

        findViewById(R.id.any_chart_view).setVisibility(View.INVISIBLE);

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.my_recycler_view).setVisibility(View.VISIBLE);
                findViewById(R.id.any_chart_view).setVisibility(View.INVISIBLE);
            }
        });
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.my_recycler_view).setVisibility(View.INVISIBLE);
                findViewById(R.id.any_chart_view).setVisibility(View.VISIBLE);
            }
        });

        loadLinechart();
    }

    private void loadLinechart() {
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Weekly temperature chart.");

        cartesian.yAxis(0).title("Temperature (C)");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);


//        seriesData.add(new CustomDataEntry("09-10", 32, 31));
//        seriesData.add(new CustomDataEntry("10-10", 36, 29));
//        seriesData.add(new CustomDataEntry("11-10", 36, 29));
//        seriesData.add(new CustomDataEntry("12-10", 36, 29));
//        seriesData.add(new CustomDataEntry("13-10", 36, 29));
//        seriesData.add(new CustomDataEntry("14-10", 35, 29));
//        seriesData.add(new CustomDataEntry("15-10", 35, 29));



        Set set = Set.instantiate();
        set.data((ArrayList) seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");


        Line series1 = cartesian.line(series1Mapping);
        series1.name("Max");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(7d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(7d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name("Min");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(7d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(7d)
                .offsetY(5d);


        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
    }
    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }

    }

    private void init() {
        anyChartView = findViewById(R.id.any_chart_view);
        btnList = (Button) findViewById(R.id.btnList);
        btnChart = (Button) findViewById(R.id.btnChart);
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

                int a= (int) Math.round(Double.valueOf(max)-Double.valueOf(270));
                int b= (int) Math.round(Double.valueOf(min)-Double.valueOf(270));

                String tempMax = String.valueOf(a);
                String tempMin = String.valueOf(b);

                arrayListWeather.add(new Weather(Day,status,icon,tempMax,tempMin));
                seriesData.add((new CustomDataEntry(Day,Integer.parseInt(tempMax),Integer.parseInt(tempMin))));

            }
            itemAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
