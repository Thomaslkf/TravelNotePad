package hk.edu.cuhk.cse.group15.travelnotepad;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import hk.edu.cuhk.cse.group15.travelnotepad.DataPackage.TripData;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.MyViewHolder> {
    private TripData[] TripData;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView TripNameView;
        public TextView TripDstView;
        public TextView TripDateView;
        public MyViewHolder(View v) {
            super(v);
            TripNameView = v.findViewById(R.id.TripNameDisplay);
            TripDstView = v.findViewById(R.id.TripDstDisplay);
            TripDateView = v.findViewById(R.id.TripDateDisplay);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TripListAdapter(TripData[] input) {
        TripData = input;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TripListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.TripNameView.setText(TripData[position].name);
        holder.TripDstView.setText(TripData[position].origin + " - " + TripData[position].dst);
        holder.TripDateView.setText(TripData[position].date);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return TripData.length;
    }
}
