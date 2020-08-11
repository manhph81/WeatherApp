package com.example.myapplication.ui.mainScreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.ui.Fragment.Fragment_ListWeather;
import com.example.myapplication.ui.Fragment.Fragment_Search;
import com.example.myapplication.R;
import com.example.myapplication.ui.BaseActivity;

public class MainActivity extends BaseActivity {

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        addFragment(R.id.frameContent, Fragment_ListWeather.newInstance(),false);
    }

    private void initView() {
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
                replaceFragment(R.id.frameContent, Fragment_Search.newInstance(),true);
                Toast.makeText(this,"New",Toast.LENGTH_SHORT).show();
                break;
            case R.id.help:
                Toast.makeText(this,"Help",Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}