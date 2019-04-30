package hk.edu.cuhk.cse.group15.travelnotepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Landing extends AppCompatActivity {
    private static final int TEXT_REQUEST = 1;

    private RecyclerView tripListRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);
        tripListRecyclerView = (RecyclerView) findViewById(R.id.landing_trip_list_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        tripListRecyclerView.setLayoutManager(layoutManager);

//        DataPackage.purgeData(this);
        DataPackage dataPackage  = DataPackage.readDataFromStorage(this);

        mAdapter = new TripListAdapter(dataPackage.tripData);
        tripListRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.arrow_up_float);
    }

    public void newTrip(View view){
        Intent intent = new Intent(Landing.this, TripCreation.class);
        startActivityForResult(intent, TEXT_REQUEST);


    }

}
