package hk.edu.cuhk.cse.group15.travelnotepad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CheckpointsListAdapter extends RecyclerView.Adapter<CheckpointsListAdapter.MyViewHolder> {
    private List<DataPackage.TripData.Checkpoint> checkpoints;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView AddressDisplay;
        public TextView FullAddressDisplay;
        public View[] Wrappers;

        public MyViewHolder(View v) {
            super(v);
            AddressDisplay = v.findViewById(R.id.Address);
            FullAddressDisplay = v.findViewById(R.id.fullAddress);

            Wrappers = new View[] {
                v.findViewById(R.id.AddWrapper),
                v.findViewById(R.id.UpWrapper),
                v.findViewById(R.id.DownWrapper),
                v.findViewById(R.id.deleteWrapper)
            };
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CheckpointsListAdapter(List<DataPackage.TripData.Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CheckpointsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkpoint_list_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.AddressDisplay.setText(checkpoints.get(position).name);
        holder.FullAddressDisplay.setText(checkpoints.get(position).address == null ? "" : checkpoints.get(position).address.getAddressLine(0));
        for(View Wrapper : holder.Wrappers){
            Wrapper.setTag(position);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return checkpoints.size();
    }
}
