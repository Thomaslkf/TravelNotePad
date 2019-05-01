package hk.edu.cuhk.cse.group15.travelnotepad;

import androidx.appcompat.app.AppCompatActivity;
import hk.edu.cuhk.cse.group15.travelnotepad.activity.CloudUpload;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class TripMenu extends AppCompatActivity {
    private static final String EXTRA_IS_NEW_TRIP = "EXTRA_IS_NEW_TRIP";
    private static final String EXTRA_READONLY = "EXTRA_READONLY";
    private static final String EXTRA_TRIPDATA_POS = "EXTRA_TRIPDATA_POS";
    private static final String EXTRA_UPLOAD_INFO = "EXTRA_UPLOAD_INFO";

    private int deleteTripBuffer = 0;
    private int trip_pos = 0;
    private DataPackage.TripData tripData;

    DataPackage dataPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_menu);
        dataPackage = DataPackage.readDataFromStorage(this);

        Intent intent = getIntent();
        trip_pos = intent.getIntExtra(EXTRA_TRIPDATA_POS, 0);
        tripData = dataPackage.tripData.get(trip_pos);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        dataPackage = DataPackage.readDataFromStorage(this);
        tripData = dataPackage.tripData.get(trip_pos);
    }

    public void startTrip(View v) {
        Intent intent = new Intent(TripMenu.this, DayActivitiesPicker.class);
        intent.putExtra(EXTRA_TRIPDATA_POS, trip_pos);

        startActivityForResult(intent, 1);
    }

    public void viewTrip(View v) {
        Intent intent = new Intent(TripMenu.this, TripCreation.class);
        intent.putExtra(EXTRA_READONLY, true);
        intent.putExtra(EXTRA_IS_NEW_TRIP, false);
        intent.putExtra(EXTRA_TRIPDATA_POS, trip_pos);

        startActivityForResult(intent, 1);
    }

    public void editTrip(View v) {
        Intent intent = new Intent(TripMenu.this, TripCreation.class);
        intent.putExtra(EXTRA_IS_NEW_TRIP, false);
        intent.putExtra(EXTRA_TRIPDATA_POS, trip_pos);

        startActivityForResult(intent, 1);
    }

    public void openShoppingCart(View v) {
        Intent intent = new Intent(TripMenu.this, ShopingCartViewer.class);
        intent.putExtra(EXTRA_TRIPDATA_POS, trip_pos);

        startActivityForResult(intent, 1);
    }

    public void openUploader(View v) {
        Intent intent = new Intent(TripMenu.this, CloudUpload.class);
        intent.putExtra(EXTRA_UPLOAD_INFO, tripData.toString());

        startActivity(intent);
    }

    public void deleteTrip(View v) {
        deleteTripBuffer++;
        if(deleteTripBuffer == 2){
            dataPackage.tripData.remove(trip_pos);
            dataPackage.writeDataToStorage(this);
            finish();
        } else {
            Toast.makeText(this, "Press again to delete Trip", Toast.LENGTH_SHORT).show();
        }
    }
}
