package hk.edu.cuhk.cse.group15.travelnotepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.List;

public class DayActivitiesPicker extends AppCompatActivity {
    private static final String EXTRA_TRIPDATA = "EXTRA_TRIPDATA";
    private static final String EXTRA_TRIPDATA_POS = "EXTRA_TRIPDATA_POS";

    private RecyclerView dayActivitiesListRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private DataPackage dataPackage;
    private int trip_pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_activities_picker);

        dayActivitiesListRecyclerView = (RecyclerView) findViewById(R.id.day_activities_list_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        dayActivitiesListRecyclerView.setLayoutManager(layoutManager);

        // Intent
        Intent intent = getIntent();
        trip_pos = intent.getIntExtra(EXTRA_TRIPDATA_POS, -1);

        // Setup View Reference
        dataPackage = DataPackage.readDataFromStorage(this);
        DataPackage.TripData td = dataPackage.tripData.get(trip_pos);

        mAdapter = new DayActivitiesListAdapter(td.dayActivity, td.date);
        dayActivitiesListRecyclerView.setAdapter(mAdapter);
        setTitle("Start " + td.name);
    }

    public void dayOnClick(View v){
        Gson gs = new Gson();
        int day = (int) v.getTag();

        Intent intent = new Intent(this, DailyDetails.class);
        intent.putExtra(EXTRA_TRIPDATA, gs.toJson(dataPackage.tripData.get(trip_pos).dayActivity.get(day), List.class));

        startActivityForResult(intent, 1);
    }
}
