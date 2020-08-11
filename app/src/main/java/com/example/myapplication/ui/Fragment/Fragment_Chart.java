package com.example.myapplication.ui.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.example.myapplication.R;
import com.example.myapplication.Weather;

import java.util.ArrayList;

public class Fragment_Chart extends Fragment {
    private static ArrayList<Weather> array;
    private AnyChartView anyChartView;
    private ArrayList<CustomDataEntry> seriesData;


    public static Fragment_Chart newInstance(ArrayList<Weather> arrayListWeather) {

        Bundle args = new Bundle();
        Fragment_Chart fragment = new Fragment_Chart();
        array = new ArrayList<>(arrayListWeather);
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
        loadLinechart();
    }

    private void init(View view) {
        anyChartView = view.findViewById(R.id.any_chart_view);
        seriesData = new ArrayList<>();
    }

    private void loadLinechart() {
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

//        loadDataArray();
        seriesData.add(new CustomDataEntry("09-10", 32, 31));
        seriesData.add(new CustomDataEntry("10-10", 36, 29));
        seriesData.add(new CustomDataEntry("11-10", 36, 29));
        seriesData.add(new CustomDataEntry("12-10", 36, 29));
        seriesData.add(new CustomDataEntry("13-10", 36, 29));
        seriesData.add(new CustomDataEntry("14-10", 35, 29));
        seriesData.add(new CustomDataEntry("15-10", 35, 29));


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

    private void loadDataArray() {
        for (Weather item:array) {
            seriesData.add((new CustomDataEntry(item.getDate(),Integer.parseInt(item.getMaxTemp()),Integer.parseInt(item.getMinTemp()))));
        }
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }

    }
}
