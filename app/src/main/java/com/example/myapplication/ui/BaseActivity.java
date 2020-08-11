package com.example.myapplication.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.database.Database;

public class BaseActivity extends AppCompatActivity {
    public static final String DEFAULT_CITY = "SAIGON";
    public static final String API_ID = "77780b9269d06ce0066641430cd0645d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void addFragment(int containView, Fragment fragment, Boolean addPopBackStack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(containView, fragment);
        if (addPopBackStack) {
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commit();
    }
    protected void replaceFragment(int containView, Fragment fragment, Boolean addPopBackStack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(containView, fragment);
        if (addPopBackStack) {
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commit();
    }

}
