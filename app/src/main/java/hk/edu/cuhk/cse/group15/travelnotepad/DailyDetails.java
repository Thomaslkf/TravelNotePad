package hk.edu.cuhk.cse.group15.travelnotepad;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

    Button finish_button;
    Bundle bundle;
    int checkpoint;
    TextView title_details,content_location,content_location_next,content_description
            ,content_distance,content_duration;

    private NotificationManagerCompat notificationManager;

    String distance[] = {"1.1","0.7","4.2","6.3","2.8","2.9","3.1","2.2","12.3","8.9"};
    String duration[] = {"5","3","5","8","10","10","6","4","15","12"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_details);

        bundle = getIntent().getExtras();
        checkpoint = bundle.getInt("currentPoint",0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
//        dayActivities = new Gson().fromJson(intent.getStringExtra("EXTRA_TRIPDATA"), List.class);

        // changed from (... ,class) into (... ,type)
        Type tempType = new TypeToken<List<Checkpoint>>(){}.getType();
        dayActivities = new Gson().fromJson(intent.getStringExtra("EXTRA_TRIPDATA"),tempType);


        timelineRecycler = (RecyclerView) findViewById(R.id.timeline_recycler);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        timelineAdapter = new TimelineAdapter(dayActivities,checkpoint);
        timelineRecycler.setAdapter(timelineAdapter);
        timelineRecycler.setLayoutManager(layoutManager);

        title_details = (TextView) findViewById(R.id.title_details);
        content_location = (TextView)findViewById(R.id.content_location);
        content_location_next = (TextView)findViewById(R.id.content_location_next);
        content_description = (TextView)findViewById(R.id.content_description);
        content_distance = (TextView)findViewById(R.id.content_distance);
        content_duration = (TextView)findViewById(R.id.content_duration);

        title_details.setText("Check point - "+Integer.toString(checkpoint+1));
        content_location.setText(dayActivities.get(checkpoint).name);
        if(checkpoint == dayActivities.size()-1){
            content_location_next.setText("");
        }else {
            content_location_next.setText(dayActivities.get(checkpoint+1).name);
        }
        if(dayActivities.get(checkpoint).description != null)
            content_location_next.setText(dayActivities.get(checkpoint).description);

        if(checkpoint <10){
            content_distance.setText(distance[checkpoint]+" km");
            content_duration.setText(duration[checkpoint]+" mins");
        }else {
            content_distance.setText(distance[checkpoint-10]+" km");
            content_duration.setText(duration[checkpoint-10]+" mins");
        }

        notificationManager = NotificationManagerCompat.from(this);
        int timer = Integer.parseInt(duration[checkpoint]);
        new CountDownTimer(timer* 60 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                sendNotification();
            }
        }.start();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        finish_button = (Button) findViewById(R.id.finish_button);
        int temp1,temp2;
        if(dayActivities.size() > 1){
            if(checkpoint == dayActivities.size()-1){
                temp1 = checkpoint;
                temp2 = checkpoint;
            }else{
                temp1 = checkpoint;
                temp2 = checkpoint + 1;
            }
        }else{
            temp1 = 0;
            temp2 = 0;
        }
        startLatitude = dayActivities.get(temp1).address.getLatitude();
        startLongitude  = dayActivities.get(temp1).address.getLongitude();
        endLatitude = dayActivities.get(temp2).address.getLatitude();
        endLongitude = dayActivities.get(temp2).address.getLongitude();

        Log.d("LocationTask: ", startLongitude + ", "+startLatitude);
        Log.d("LocationTask: ", endLongitude + ", "+endLatitude);
        startLocation = new LatLng(startLatitude,startLongitude);
        endLocation = new LatLng(endLatitude,endLongitude);
        mMap.addMarker((new MarkerOptions().position(startLocation)));
        mMap.addMarker((new MarkerOptions().position(endLocation)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("group15", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public  void sendNotification(){
        Log.d("haha","Notification created.");

        Notification notification = new NotificationCompat.Builder(this,NotificationCreator.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("Time to Next point!")
                .setContentText("It seems that you should start moving to next checkPoint!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1,notification);
    }

    public void goToNext(View v){
        Log.d("haha","finished for "+checkpoint);
        checkpoint = checkpoint + 1;
        if ((checkpoint) == dayActivities.size()){
            Log.d("haha","meet END.");
            AlertDialog.Builder builder = new AlertDialog.Builder(DailyDetails.this);
            builder.setTitle("End of Today");
            builder.setMessage("This is the last checkpoint today!");
            builder.setIcon(android.R.drawable.ic_menu_info_details);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int whichButton) {
                   DailyDetails.this.finish();
                }}).show();
        }else{
            Intent i = getIntent();
            i.putExtra("currentPoint",checkpoint);
            finish();
            v.getContext().startActivity(i);
        }
    }

}
