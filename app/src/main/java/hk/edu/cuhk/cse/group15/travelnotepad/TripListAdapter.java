package hk.edu.cuhk.cse.group15.travelnotepad;

import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import hk.edu.cuhk.cse.group15.travelnotepad.DataPackage.TripData;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.MyViewHolder> {
    private List<TripData> TripData;
    private SimpleDateFormat dateFormat;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView TripNameView;
        public TextView TripOriginView;
        public TextView TripDstView;
        public TextView TripDateView;
        public View TripListButtonWrapper;

        public MyViewHolder(View v) {
            super(v);
            TripNameView = v.findViewById(R.id.TripNameDisplay);
            TripOriginView = v.findViewById(R.id.TripOriginDisplay);
            TripDstView = v.findViewById(R.id.TripDstDisplay);
            TripDateView = v.findViewById(R.id.TripDateDisplay);
            TripListButtonWrapper = v.findViewById(R.id.TripListButtonWrapper);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TripListAdapter(List<TripData> input) {
        TripData = input;
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
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
        Address originAdd = TripData.get(position).origin;
        Address dstAdd = TripData.get(position).dst;

        String origin = originAdd.getCountryName();
        String dst = dstAdd.getCountryName() + ", " + (dstAdd.getAdminArea() == null ? dstAdd.getFeatureName() : dstAdd.getAdminArea());

        holder.TripNameView.setText(TripData.get(position).name);
        holder.TripOriginView.setText(origin + " - ");
        holder.TripDstView.setText(dst);
        holder.TripDateView.setText(dateFormat.format(TripData.get(position).date[0]) + " ~ "  + dateFormat.format(TripData.get(position).date[1]) );
        holder.TripListButtonWrapper.setTag(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return TripData.size();
    }
}
