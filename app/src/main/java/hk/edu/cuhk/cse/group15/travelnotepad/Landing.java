package hk.edu.cuhk.cse.group15.travelnotepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import hk.edu.cuhk.cse.group15.travelnotepad.DataPackage.TripData;

public class Landing extends AppCompatActivity {
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

//      Get Data
        TripData[] data = {
            new TripData("Grad Trip", "Hong Kong", "Tokyo", "2019/05/22"),
            new TripData("Family Trip", "Hong Kong", "Taipei", "2019/05/31"),
        };

        mAdapter = new TripListAdapter(data);
        tripListRecyclerView.setAdapter(mAdapter);
    }
}
