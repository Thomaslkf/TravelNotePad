package hk.edu.cuhk.cse.group15.travelnotepad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class DayActivitiesListAdapter extends RecyclerView.Adapter<DayActivitiesListAdapter.MyViewHolder> {
    private List<ArrayList<DataPackage.TripData.Checkpoint>> DayActivities;
    private Date[] Date;
    private SimpleDateFormat dateFormat;
    private boolean isNewTrip;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView dayNumberView;
        public TextView dateView;
        public View DayActivitiesListButtonWrapper;

        public MyViewHolder(View v) {
            super(v);
            dayNumberView = v.findViewById(R.id.DayNumberDisplay);
            dateView = v.findViewById(R.id.DateDisplay);
            DayActivitiesListButtonWrapper = v.findViewById(R.id.DayActivitiesListButtonWrapper);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DayActivitiesListAdapter(List<ArrayList<DataPackage.TripData.Checkpoint>> input, Date[] Date, boolean isNewTrip) {
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DayActivities = input;
        this.isNewTrip = isNewTrip;
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
        holder.DayActivitiesListButtonWrapper.setTag(position);
        if(isNewTrip) holder.DayActivitiesListButtonWrapper.setVisibility(View.INVISIBLE);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return DayActivities.size();
    }
}
