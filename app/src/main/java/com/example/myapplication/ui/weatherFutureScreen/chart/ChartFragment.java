package com.example.myapplication.ui.weatherFutureScreen.chart;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.myapplication.R;
import com.example.myapplication.ui.weatherFutureScreen.WeatherFutureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChartFragment extends Fragment {
    public static final String API_ID = "77780b9269d06ce0066641430cd0645d";
    private AnyChartView anyChartView;
    private ArrayList<CustomDataEntry> seriesData;

    public static ChartFragment newInstance() {

        Bundle args = new Bundle();
        ChartFragment fragment = new ChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        if (getActivity() instanceof WeatherFutureActivity) {
            String lat = ((WeatherFutureActivity) getActivity()).getLat();
            String lon = ((WeatherFutureActivity) getActivity()).getLon();
            getDataWeek(lon, lat);
        }
    }

    private void getDataWeek(String lon, String lat) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&%20exclude=daily&appid=" + API_ID;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("result", response);
                        loadDataWeek(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

    private void loadDataWeek(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArrayList = jsonObject.getJSONArray("daily");
            for (int i = 0; i < jsonArrayList.length(); i++) {
                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);

                String day = jsonObjectList.getString("dt");
                long l = Long.valueOf(day);
                Date date = new Date(l * 1000L);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDayFormat = new SimpleDateFormat("EEEE");
                String Day = simpleDateFormat.format(date);

                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                String max = jsonObjectTemp.getString("max");
                String min = jsonObjectTemp.getString("min");

                int a = (int) Math.round(Double.valueOf(max) - Double.valueOf(270));
                int b = (int) Math.round(Double.valueOf(min) - Double.valueOf(270));

                String tempMax = String.valueOf(a);
                String tempMin = String.valueOf(b);

                seriesData.add((new CustomDataEntry(Day, Integer.parseInt(tempMax), Integer.parseInt(tempMin))));
            }
            drawChart();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void init(View view) {
        anyChartView = view.findViewById(R.id.any_chart_view);
        seriesData = new ArrayList<>();
    }

    private void drawChart() {
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

    private void loadData() {
        seriesData.add(new CustomDataEntry("09-10", 32, 31));
        seriesData.add(new CustomDataEntry("10-10", 36, 29));
        seriesData.add(new CustomDataEntry("11-10", 36, 29));
        seriesData.add(new CustomDataEntry("12-10", 36, 29));
        seriesData.add(new CustomDataEntry("13-10", 36, 29));
        seriesData.add(new CustomDataEntry("14-10", 35, 29));
        seriesData.add(new CustomDataEntry("15-10", 35, 29));
        drawChart();
    }

    private void delay(final int miliis) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
//                Toast.makeText(c, "check", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, miliis);
            }
        }, miliis);
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }
}
