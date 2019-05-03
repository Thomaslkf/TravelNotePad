package hk.edu.cuhk.cse.group15.travelnotepad;


import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hk.edu.cuhk.cse.group15.travelnotepad.DataPackage.TripData.Checkpoint;

public class TimelineAdapter extends  RecyclerView.Adapter<TimelineAdapter.timeViewHolder> {
    private List<Checkpoint> checkpointList;
    int currentPoint;
    int num;

    public  TimelineAdapter(List<Checkpoint> input,int checkPoint){
        checkpointList = input;
        currentPoint = checkPoint+1;
        num = checkpointList.size();
        Log.d("haha","current = "+ currentPoint);
    }

    @NonNull
    @Override
    public timeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkpoint_item,parent,false);
        return new timeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull timeViewHolder holder, int position) {
        if(currentPoint > 5){
            holder.node.setText(Integer.toString(position+currentPoint));
            if(num - currentPoint >= 5){
                if((currentPoint-1) % 5  == position) {
                    holder.node.setTextColor(Color.WHITE);
                    holder.lineNode.setImageResource(R.drawable.time_node__active_part);
                }
            }else if(position == 0) {
                holder.node.setTextColor(Color.WHITE);
                holder.lineNode.setImageResource(R.drawable.time_node__active_part);
            }
        }else{
            holder.node.setText(Integer.toString(position+1));
            if(position == currentPoint-1) {
                holder.node.setTextColor(Color.WHITE);
                holder.lineNode.setImageResource(R.drawable.time_node__active_part);
            }
        }


    }

    @Override
    public int getItemCount() {
        if (num >= 5){
            if(currentPoint <= 5){
                return 5;
            }else{
                return  num - currentPoint+1;
            }
        }
        return num;
    }

    public class timeViewHolder extends RecyclerView.ViewHolder{
        TextView node;
        ImageView lineNode;

        public timeViewHolder(@NonNull View itemView) {
            super(itemView);
            node = itemView.findViewById(R.id.numNode);
            lineNode = itemView.findViewById(R.id.lineNode);
        }
    }




}
