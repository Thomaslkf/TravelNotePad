package hk.edu.cuhk.cse.group15.travelnotepad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Landing extends AppCompatActivity {
    private static final int TEXT_REQUEST = 1;
    private static final String EXTRA_TRIPDATA_POS = "EXTRA_TRIPDATA_POS";

    private RecyclerView tripListRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    DataPackage dataPackage;
    List<DataPackage.TripData> tripData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);
        tripListRecyclerView = (RecyclerView) findViewById(R.id.landing_trip_list_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        tripListRecyclerView.setLayoutManager(layoutManager);

//        DataPackage.purgeData(this);
        dataPackage  = DataPackage.readDataFromStorage(this);
        tripData = dataPackage.tripData;
        mAdapter = new TripListAdapter(tripData);
        tripListRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.arrow_up_float);
    }

    public void newTrip(View view){
        Intent intent = new Intent(Landing.this, TripCreation.class);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    public void openTrip(View view){
        int position = (int) view.getTag();

        Intent intent = new Intent(Landing.this, TripMenu.class);
        intent.putExtra(EXTRA_TRIPDATA_POS, position);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataPackage  = DataPackage.readDataFromStorage(this);
        tripData = dataPackage.tripData;

        tripListRecyclerView.swapAdapter(new TripListAdapter(tripData), false);
    }
}
