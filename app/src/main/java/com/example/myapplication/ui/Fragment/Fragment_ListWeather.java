package com.example.myapplication.ui.Fragment;
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
import com.example.myapplication.ItemAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Weather;
import com.example.myapplication.database.Database;
import java.util.ArrayList;
public class Fragment_ListWeather extends Fragment {
    public static final String SQL_NAME = "weather.sqlite";
    public static final String API_ID = "77780b9269d06ce0066641430cd0645d";
    private RecyclerView recyclerView;
    public ItemAdapter itemAdapter;
    public Database database;
    public ArrayList arrayListWeather;

    public static Fragment_ListWeather newInstance() {
        Bundle args = new Bundle();
        Fragment_ListWeather fragment = new Fragment_ListWeather();
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
        arrayListWeather = new ArrayList();
        itemAdapter = new ItemAdapter(arrayListWeather,getActivity().getApplicationContext());
        recyclerView.setAdapter(itemAdapter);
    }
    protected void loadDataArrayWeather() {
        arrayListWeather.clear();
        database = new Database(getActivity(),SQL_NAME,null,1);
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
        itemAdapter.notifyDataSetChanged();
    }
}