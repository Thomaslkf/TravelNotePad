package hk.edu.cuhk.cse.group15.travelnotepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import hk.edu.cuhk.cse.group15.travelnotepad.DataPackage.TripData;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.MyViewHolder> {
    private TripData TripData;
    private SimpleDateFormat dateFormat;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout ItemWrapper;
        public TextView DayNumberDisplay;

        public MyViewHolder(View v) {
            super(v);
            DayNumberDisplay = v.findViewById(R.id.DayNumberDisplay);
            ItemWrapper = v.findViewById(R.id.ItemWrapper);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ShoppingCartAdapter(TripData input, Context c) {
        context = c;
        TripData = input;
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ShoppingCartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_list_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Calendar c = Calendar.getInstance();
        c.setTime(TripData.date[0]);
        c.add(Calendar.DATE, position);
        holder.DayNumberDisplay.setText("Day "+(position+1)+" - "+ dateFormat.format(c.getTime()));

        LayoutParams lparams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
        );
        lparams.setMargins(0,8,0,8);

        List<DataPackage.TripData.Checkpoint> checkpoints = TripData.dayActivity.get(position);
        for (DataPackage.TripData.Checkpoint checkpoint : checkpoints) {
            for (String item : checkpoint.shoppingCart) {
                TextView tv = new TextView(context);
                tv.setLayoutParams(lparams);
                tv.setText(item);
                tv.setTextSize(16);
                holder.ItemWrapper.addView(tv);
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return TripData.dayActivity.size();
    }
}
