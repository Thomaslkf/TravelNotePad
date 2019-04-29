package hk.edu.cuhk.cse.group15.travelnotepad;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TripCreation extends AppCompatActivity {
    private SimpleDateFormat simple = new SimpleDateFormat("yyyy/MM/dd");
    private RecyclerView dayActivitiesListRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private DataPackage dataPackage;
    DataPackage.TripData tripData;
    private boolean isNewTrip = true;

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener from_date_listener;
    DatePickerDialog.OnDateSetListener end_date_listener;
    private long[] durationTracker = new long[2];
    int duration = 0;

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

        // Setup View Reference
        dataPackage = DataPackage.readDataFromStorage(this);
        tripName = findViewById(R.id.title_name);
        tripOrigin = findViewById(R.id.title_location_origin);
        tripDst = findViewById(R.id.title_location_dst);
        from_date = findViewById(R.id.title_from_date);
        to_date = findViewById(R.id.title_end_date);

        // Setup Date Picker
        setupDatePicker(from_date, from_date_listener, 0);
        setupDatePicker(to_date, end_date_listener , 1);

        if(isNewTrip) {
            // Create a new TripDate Object
            tripData = new DataPackage.TripData();
        } else {
            // Load the passed TripData
//            tripData = new DataPackage.TripData();
        }

        mAdapter = new DayActivitiesListAdapter(tripData.dayActivity, tripData.date);
        dayActivitiesListRecyclerView.setAdapter(mAdapter);
    }

    private void setupDatePicker(final EditText textField, DatePickerDialog.OnDateSetListener l, final int which){
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

        duration = (int)(((durationTracker[1]-durationTracker[0])/1000/3600/24) + 1);
        if (duration > 0) {
            tripData.initiateDayActivity(duration);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void createTrip(View v){
        if(isNewTrip) {
            // Update all field, just in case
            tripData.name = tripName.getText().toString();
            tripData.origin = tripOrigin.getText().toString();
            tripData.dst = tripDst.getText().toString();
            tripData.updateTripDate(from_date.getText().toString(), to_date.getText().toString());

            dataPackage.tripData.add(tripData);
        } else {
//            dataPackage.tripData.set(index, tripData);
        }

        dataPackage.writeDataToStorage(this);
    }
}
