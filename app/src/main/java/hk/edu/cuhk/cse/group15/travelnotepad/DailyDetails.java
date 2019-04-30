package hk.edu.cuhk.cse.group15.travelnotepad;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hk.edu.cuhk.cse.group15.travelnotepad.DataPackage.TripData.Checkpoint;

public class DailyDetails extends FragmentActivity implements OnMapReadyCallback,TaskLoadedCallback {
    private static final String EXTRA_TRIPDATA = "EXTRA_TRIPDATA";

    private GoogleMap mMap;
    LatLngBounds.Builder builder;
    private LatLng startLocation,endLocation;
    private MarkerOptions startOption,endOption;
    Polyline polyline;
    private double startLongitude,endLongitude, startLatitude,endLatitude;

    private List<Checkpoint> dayActivities;

    //for RecyclerView
    private RecyclerView timelineRecycler;
    private RecyclerView.Adapter timelineAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_details);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        dayActivities = new Gson().fromJson(intent.getStringExtra("EXTRA_TRIPDATA"), List.class);

        timelineRecycler = (RecyclerView) findViewById(R.id.timeline_recycler);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        timelineAdapter = new TimelineAdapter(dayActivities);
        timelineRecycler.setAdapter(timelineAdapter);
        timelineRecycler.setLayoutManager(layoutManager);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        startLatitude = 22.421535499999997;
        startLongitude  = 114.20986939999999;
        endLatitude = 22.318043;
        endLongitude = 114.168791;

        Log.d("LocationTask: ", startLongitude + ", "+startLatitude);
        Log.d("LocationTask: ", endLongitude + ", "+endLatitude);
        startLocation = new LatLng(startLatitude,startLongitude);
        endLocation = new LatLng(endLatitude,endLongitude);
        mMap.addMarker((new MarkerOptions().position(startLocation)));
        mMap.addMarker((new MarkerOptions().position(endLocation)));

//        mMap.moveCamera((CameraUpdateFactory.newLatLngZoom(startLocation,17)));
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);


        startOption = new MarkerOptions().position(startLocation);
        endOption = new MarkerOptions().position(endLocation);
        String url = getUrl(startOption.getPosition(),endOption.getPosition(),"transit");
        new FetchURL(DailyDetails.this).execute(url,"transit");
        builder = new LatLngBounds.Builder();
        builder.include(startOption.getPosition());
        builder.include(endOption.getPosition());
        LatLngBounds bounds = builder.build();
        final DisplayMetrics display = getResources().getDisplayMetrics();
        if(mMap != null)
            mMap.moveCamera((CameraUpdateFactory.newLatLngZoom(startLocation,16)));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,display.widthPixels, display.heightPixels,300));

    }

    private  String getUrl(LatLng origan, LatLng dest, String directionMode){
        String str_origin = "origin=" + origan.latitude + "," + origan.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return  url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (polyline != null){
            polyline.remove();
        }
        polyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}
