package hk.edu.cuhk.cse.group15.travelnotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class TripMenu extends AppCompatActivity {
    private static final String EXTRA_IS_NEW_TRIP = "EXTRA_IS_NEW_TRIP";
    private static final String EXTRA_READONLY = "EXTRA_READONLY";
    private static final String EXTRA_TRIPDATA_POS = "EXTRA_TRIPDATA_POS";

    private int trip_pos = 0;
    private DataPackage.TripData tripData;

    DataPackage dataPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_menu);
        dataPackage  = DataPackage.readDataFromStorage(this);

        Intent intent = getIntent();
        trip_pos = intent.getIntExtra(EXTRA_TRIPDATA_POS, 0);
        tripData = dataPackage.tripData.get(trip_pos);
    }

    public void startTrip(View v){}

    public void viewTrip(View v){
        Intent intent = new Intent(TripMenu.this, TripCreation.class);
        intent.putExtra(EXTRA_READONLY, true);
        intent.putExtra(EXTRA_IS_NEW_TRIP, false);
        intent.putExtra(EXTRA_TRIPDATA_POS, trip_pos);

        startActivityForResult(intent, 1);
    }

    public void editTrip(View v){
        Intent intent = new Intent(TripMenu.this, TripCreation.class);
        intent.putExtra(EXTRA_IS_NEW_TRIP, false);
        intent.putExtra(EXTRA_TRIPDATA_POS, trip_pos);

        startActivityForResult(intent, 1);
    }

    public void openShoppingCart(View v){}
}
