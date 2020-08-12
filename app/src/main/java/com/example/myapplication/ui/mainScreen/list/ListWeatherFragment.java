package com.example.myapplication.ui.mainScreen.list;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Fragment;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Weather;
import com.example.myapplication.database.Database;
import com.example.myapplication.ui.weatherDetailScreen.WeatherDetailActivity;

import java.util.ArrayList;

public class ListWeatherFragment extends Fragment implements ItemWeatherAdapter.ItemClickListener {
    public static final String SQL_NAME = "weather.sqlite";
    private RecyclerView recyclerView;
    public ItemWeatherAdapter itemAdapter;
    public Database database;
    public ArrayList<Weather> arrayListWeather;

    public static ListWeatherFragment newInstance() {
        Bundle args = new Bundle();
        ListWeatherFragment fragment = new ListWeatherFragment();
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
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataArrayWeather();
    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity().getApplicationContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);
        arrayListWeather = new ArrayList();
        itemAdapter = new ItemWeatherAdapter(arrayListWeather, getActivity().getApplicationContext(), this);
        recyclerView.setAdapter(itemAdapter);
    }

    private void loadDataArrayWeather() {
        arrayListWeather.clear();
        database = new Database(getActivity(), SQL_NAME, null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS Weather(City varchar(200) primary key,Date varchar(200) ,Status varchar(200),Icon varchar(200),MaxTemp varchar(200),MinTemp varchar(200))");
        Cursor data = database.GetData("SELECT * FROM Weather");
        while (data.moveToNext()) {
            String city = data.getString(0);
            String date = data.getString(1);
            String status = data.getString(2);
            String image = data.getString(3);
            String maxTemp = data.getString(4);
            String minTemp = data.getString(5);
            arrayListWeather.add(new Weather(city, date, status, image, maxTemp, minTemp));
        }
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(), WeatherDetailActivity.class);
        intent.putExtra("city", arrayListWeather.get(position).city);
        startActivity(intent);
    }

    @Override
    public void onLongClick(int position) {
        database = new Database(getActivity(), "weather.sqlite", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS Weather(City varchar(200) primary key,Date varchar(200),Status varchar(200),Icon varchar(200),MaxTemp varchar(200),MinTemp varchar(200))");
        database.QueryData("DELETE FROM Weather WHERE City = \"" + arrayListWeather.get(position).getCity() + "\"");
    }
}