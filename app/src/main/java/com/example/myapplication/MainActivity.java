package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ArrayList<Weather> arrayListWeather;
    ItemAdapter itemAdapter;
    Database database;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        init();

        arrayListWeather = new ArrayList<>();

        database = new Database(this,"weather.sqlite",null,1);
        database.QueryData("CREATE TABLE IF NOT EXISTS Weather(City varchar(200) primary key,Status varchar(200),Icon varchar(200),MaxTemp varchar(200),MinTemp varchar(200))");
        Cursor data = database.GetData("SELECT * FROM Weather");
        while(data.moveToNext()){
            String city = data.getString(0);
            String status = data.getString(1);
            String image = data.getString(2);
            String maxTemp = data.getString(3);
            String minTemp = data.getString(4);
            arrayListWeather.add(new Weather(city,city,status,image,maxTemp,minTemp));
        }

        if(arrayListWeather.size()>0){
            itemAdapter = new ItemAdapter(arrayListWeather,getApplicationContext());
            recyclerView.setAdapter(itemAdapter);
        }

    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(this,linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ve, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.newWeather:
                findViewById(R.id.frameContent).setVisibility(View.VISIBLE);
                // Create new fragment and transaction
                Search search = new Search();
                fragmentTransaction.add(R.id.frameContent, search,"fragSearch");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                Toast.makeText(this,"New",Toast.LENGTH_SHORT).show();
                findViewById(R.id.my_recycler_view).setVisibility(View.INVISIBLE);

                break;
            case R.id.help:
                Toast.makeText(this,"Help",Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}