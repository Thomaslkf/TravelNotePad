package hk.edu.cuhk.cse.group15.travelnotepad;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;

import com.schibstedspain.leku.LocationPickerActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.schibstedspain.leku.LocationPickerActivityKt.*;

public class TripCreation extends AppCompatActivity {
    private static final int MAP_BUTTON_REQUEST_CODE = 1;
    private static final String EXTRA_IS_NEW_TRIP = "EXTRA_IS_NEW_TRIP";
    private static final String EXTRA_TRIPDATA_POS = "EXTRA_TRIPDATA_POS";
    private static final String EXTRA_READONLY = "EXTRA_READONLY";

    // Field
    private SimpleDateFormat simple = new SimpleDateFormat("yyyy/MM/dd");
    private RecyclerView dayActivitiesListRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isReadOnly;

    // Data Package
    private DataPackage dataPackage;
    DataPackage.TripData tripData;
    private int trip_pos = 0;
    private boolean isNewTrip = true;

    // Date Picker
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener from_date_listener;
    DatePickerDialog.OnDateSetListener end_date_listener;
    private long[] durationTracker = {0,0};
    int duration = 0;

    // Map Picker
    LocationManager mLocationManager;
    private String lastOpenedMapPicker = "origin";
    private Address originAddress;
    private Address dstAddress;

    // View Reference
    EditText tripName;
    EditText tripOrigin;
    EditText tripDst;
    EditText from_date;
    EditText to_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_creation);
        dayActivitiesListRecyclerView = (RecyclerView) findViewById(R.id.day_activities_list_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        dayActivitiesListRecyclerView.setLayoutManager(layoutManager);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Intent
        Intent intent = getIntent();
        isNewTrip = intent.getBooleanExtra(EXTRA_IS_NEW_TRIP, true);
        trip_pos = intent.getIntExtra(EXTRA_TRIPDATA_POS, -1);
        isReadOnly = intent.getBooleanExtra(EXTRA_READONLY, false);

        // Setup View Reference
        dataPackage = DataPackage.readDataFromStorage(this);
        tripName = findViewById(R.id.title_name);
        tripOrigin = findViewById(R.id.title_location_origin);
        tripDst = findViewById(R.id.title_location_dst);
        from_date = findViewById(R.id.title_from_date);
        to_date = findViewById(R.id.title_end_date);

        // Setup Date Picker
        if(!isReadOnly) {
            setupDatePicker(from_date, from_date_listener, 0);
            setupDatePicker(to_date, end_date_listener, 1);
        }

        if (isNewTrip) {
            // Create a new TripDate Object
            tripData = new DataPackage.TripData();
        } else {
            // Load the passed TripData
            tripData = dataPackage.tripData.get(trip_pos);

            // Set Values
            originAddress = tripData.origin;
            dstAddress = tripData.dst;
            String origin = originAddress.getCountryName();
            String dst = dstAddress.getCountryName() + ", " + (dstAddress.getAdminArea() == null ? dstAddress.getFeatureName() : dstAddress.getAdminArea());

            durationTracker[0] = tripData.date[0].getTime();
            durationTracker[1] = tripData.date[1].getTime();

            tripName.setText(tripData.name);
            tripOrigin.setText(origin);
            tripDst.setText(dst);
            from_date.setText(simple.format(tripData.date[0]));
            to_date.setText(simple.format(tripData.date[1]));

            if(isReadOnly){
                tripName.setFocusable(false);
                tripOrigin.setFocusable(false);
                tripDst.setFocusable(false);
                from_date.setFocusable(false);
                to_date.setFocusable(false);

                findViewById(R.id.OriginMapPickerButton).setVisibility(View.GONE);
                findViewById(R.id.DstMapPickerButton).setVisibility(View.GONE);
                tripOrigin.getLayoutParams().width = LayoutParams.MATCH_PARENT;
                tripDst.getLayoutParams().width = LayoutParams.MATCH_PARENT;
            }
        }

        if(isReadOnly){
            findViewById(R.id.button_back).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.button_create).setVisibility(View.VISIBLE);
            ((Button)findViewById(R.id.button_create)).setText(isNewTrip ? "Create" : "Save");
        }

        mAdapter = new DayActivitiesListAdapter(tripData.dayActivity, tripData.date);
        dayActivitiesListRecyclerView.setAdapter(mAdapter);
    }

    private void setupDatePicker(final EditText textField, DatePickerDialog.OnDateSetListener l, final int which) {
        l = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                durationTracker[which] = myCalendar.getTimeInMillis();
                updateLabel(textField);
            }
        };

        final DatePickerDialog.OnDateSetListener finalL = l;
        textField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TripCreation.this, finalL, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel(EditText v) {
        v.setText(simple.format(myCalendar.getTime()));
        tripData.updateTripDate(from_date.getText().toString(), to_date.getText().toString());

        duration = (int) (((durationTracker[1] - durationTracker[0]) / 1000 / 3600 / 24) + 1);
        if (duration > 0) {
            tripData.initiateDayActivity(duration);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void storeTrip(View v) {
        // Update all field, just in case
        tripData.name = tripName.getText().toString();
        tripData.origin = originAddress;
        tripData.dst = dstAddress;
        tripData.updateTripDate(from_date.getText().toString(), to_date.getText().toString());

        if (isNewTrip) {
            dataPackage.tripData.add(tripData);
            dataPackage.writeDataToStorage(this);

            Intent intent = new Intent(this, TripMenu.class);
            intent.putExtra(EXTRA_TRIPDATA_POS, dataPackage.tripData.size()-1);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
        } else {
            dataPackage.tripData.set(trip_pos, tripData);
            dataPackage.writeDataToStorage(this);
        }

        finish();
    }

    // Map
    public void openMap(View v) {
        if(isReadOnly) return;

        lastOpenedMapPicker = (String) v.getTag();
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
        }

        Intent locationPickerIntent = new LocationPickerActivity.Builder()
        .shouldReturnOkOnBackPressed()
        .withZipCodeHidden()
        .withSatelliteViewHidden()
        .withGooglePlacesEnabled()
        .withGoogleTimeZoneEnabled()
        .withVoiceSearchHidden()
        .withUnnamedRoadHidden()
        .withMapStyle(R.raw.map_style)
        .build(TripCreation.this);

        startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE);
    }

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

            if (lastOpenedMapPicker.equals("origin")) {
                originAddress = fullAddress;
                String txt = originAddress.getCountryName();
                this.tripOrigin.setText(txt);
            } else {
                dstAddress = fullAddress;
                String txt = dstAddress.getCountryName() + ", " + (dstAddress.getAdminArea() == null ? dstAddress.getFeatureName() : dstAddress.getAdminArea());
                this.tripDst.setText(txt);
            }

            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("MAP - RESULT****", "CANCELLED");
            }
        }
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

    public void finish(View v){

        finish();
    }
}
