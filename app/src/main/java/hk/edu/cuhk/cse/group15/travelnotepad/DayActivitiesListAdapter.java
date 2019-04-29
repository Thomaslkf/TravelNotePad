package hk.edu.cuhk.cse.group15.travelnotepad;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hk.edu.cuhk.cse.group15.travelnotepad.DataPackage.TripData;

public class DayActivitiesListAdapter extends RecyclerView.Adapter<DayActivitiesListAdapter.MyViewHolder> {
    private List<ArrayList<DataPackage.TripData.Checkpoint>> DayActivities;
    private Date[] Date;
    private SimpleDateFormat dateFormat;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView dayNumberView;
        public TextView dateView;

        public MyViewHolder(View v) {
            super(v);
            dayNumberView = v.findViewById(R.id.DayNumberDisplay);
            dateView = v.findViewById(R.id.DateDisplay);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DayActivitiesListAdapter(List<ArrayList<DataPackage.TripData.Checkpoint>> input, Date[] Date) {
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DayActivities = input;
        this.Date = Date;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DayActivitiesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_activities_list_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.dayNumberView.setText( "Day " + (position + 1) );

        Calendar c = Calendar.getInstance();
        c.setTime(this.Date[0]);
        c.add(Calendar.DATE, position);
        holder.dateView.setText(dateFormat.format(c.getTime()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return DayActivities.size();
    }
}
