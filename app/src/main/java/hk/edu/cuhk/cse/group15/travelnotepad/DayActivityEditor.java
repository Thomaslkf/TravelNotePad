package hk.edu.cuhk.cse.group15.travelnotepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.schibstedspain.leku.LocationPickerActivity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.schibstedspain.leku.LocationPickerActivityKt.ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;

public class DayActivityEditor extends AppCompatActivity {
    private static final int MAP_BUTTON_REQUEST_CODE = 1;
    private static final String EXTRA_TRIPDATA_POS = "EXTRA_TRIPDATA_POS";
    private static final String EXTRA_DAY_NUMBER = "EXTRA_DAY_NUMBER";
    private static final String EXTRA_READONLY = "EXTRA_READONLY";

    private RecyclerView checkpointsRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // Map Picker
    LocationManager mLocationManager;

    private DataPackage dataPackage;
    DataPackage.TripData tripData;
    List<DataPackage.TripData.Checkpoint> checkpoints;

    private int trip_pos = 0;
    private int day_number = 0;
    private boolean isReadOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_editor);

        checkpointsRecyclerView = (RecyclerView) findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(this);
        checkpointsRecyclerView.setLayoutManager(layoutManager);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Intent intent = getIntent();
        trip_pos = intent.getIntExtra(EXTRA_TRIPDATA_POS, -1);
        day_number = intent.getIntExtra(EXTRA_DAY_NUMBER, -1);
        isReadOnly = intent.getBooleanExtra(EXTRA_READONLY, false);

        dataPackage = DataPackage.readDataFromStorage(this);
        tripData = dataPackage.tripData.get(trip_pos);
        checkpoints = tripData.dayActivity.get(day_number);

        mAdapter = new CheckpointsListAdapter(checkpoints);
        checkpointsRecyclerView.setAdapter(mAdapter);

        // Update front-end
        ((TextView)findViewById(R.id.title)).setText(tripData.name + " - Day " + (day_number+1));

        Calendar c = Calendar.getInstance();
        c.setTime(tripData.date[0]);
        c.add(Calendar.DATE, day_number);
        ((TextView)findViewById(R.id.DateDisplay)).setText(new SimpleDateFormat("yyyy/MM/dd").format(c.getTime()));
        ((TextView)findViewById(R.id.LocDisplay)).setText(tripData.dst.getCountryName());

        if(isReadOnly){
            findViewById(R.id.ButtonWrapper).setVisibility(View.GONE);
            ((Button)findViewById(R.id.button_back)).setText("Back");
        }
    }

    // Map
    public void openMap(View v) {
//        lastOpenedMapPicker = (String) v.getTag();
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
        }

        LocationPickerActivity.Builder locationPickerBuilder = new LocationPickerActivity.Builder()
        .withZipCodeHidden()
        .withSatelliteViewHidden()
        .withGooglePlacesEnabled()
        .withGoogleTimeZoneEnabled()
        .withVoiceSearchHidden()
        .withUnnamedRoadHidden()
        .withMapStyle(R.raw.map_style);

        Intent locationPickerIntent = locationPickerBuilder.build(this);
        startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.d("MAP - RESULT****", "OK");
            double latitude = data.getDoubleExtra(LATITUDE, 0.0);
            Log.d("MAP - LATITUDE****", Double.toString(latitude));
            double longitude = data.getDoubleExtra(LONGITUDE, 0.0);
            Log.d("MAP - LONGITUDE****", Double.toString(longitude));
            String address = data.getStringExtra(LOCATION_ADDRESS);
            Log.d("MAP - ADDRESS****", address.toString());
            Address fullAddress = data.getParcelableExtra(ADDRESS);
            if (fullAddress != null) {
                Log.d("MAP - FULL ADDRESS****", fullAddress.toString());
            }

            DataPackage.TripData.Checkpoint c = new DataPackage.TripData.Checkpoint();
            c.name = fullAddress.getFeatureName();
            c.address = fullAddress;
            checkpoints.add(c);
            mAdapter.notifyDataSetChanged();
        }

    }

    public void finish(View v){
        dataPackage.writeDataToStorage(this);
        finish();
    }

    public void delete(View v){
        checkpoints.remove((int)v.getTag());
        mAdapter.notifyDataSetChanged();
    }

    public void clickDown(View v){
        int pos = (int)v.getTag();
        if(pos+1 != checkpoints.size()) {
            DataPackage.TripData.Checkpoint temp;
            temp = checkpoints.get(pos);
            checkpoints.set(pos, checkpoints.get(pos+1));
            checkpoints.set(pos+1, temp);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void clickUp(View v){
        int pos = (int)v.getTag();
        if(pos != 0) {
            DataPackage.TripData.Checkpoint temp;
            temp = checkpoints.get(pos);
            checkpoints.set(pos, checkpoints.get(pos-1));
            checkpoints.set(pos-1, temp);
            mAdapter.notifyDataSetChanged();
        }
    }
}
