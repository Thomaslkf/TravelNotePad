package hk.edu.cuhk.cse.group15.travelnotepad;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hk.edu.cuhk.cse.group15.travelnotepad.DataPackage.TripData.Checkpoint;

public class TimelineAdapter extends  RecyclerView.Adapter<TimelineAdapter.timeViewHolder> {
    private List<Checkpoint> checkpointList;

    public  TimelineAdapter(List<Checkpoint> input){
        checkpointList = input;
        Log.d("haha","size = "+checkpointList.size());
    }

    @NonNull
    @Override
    public timeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkpoint_item,parent,false);
        return new timeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull timeViewHolder holder, int position) {
        holder.node.setText(Integer.toString(position));
    }

    @Override
    public int getItemCount() {
        return checkpointList.size();
    }

    public class timeViewHolder extends RecyclerView.ViewHolder{
        TextView node;

        public timeViewHolder(@NonNull View itemView) {
            super(itemView);
            node = itemView.findViewById(R.id.numNode);
        }
    }




}
