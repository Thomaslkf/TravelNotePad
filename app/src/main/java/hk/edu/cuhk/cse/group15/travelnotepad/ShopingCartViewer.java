package hk.edu.cuhk.cse.group15.travelnotepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

public class ShopingCartViewer extends AppCompatActivity {
    private static final String EXTRA_TRIPDATA_POS = "EXTRA_TRIPDATA_POS";

    private RecyclerView shoppingCartRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private DataPackage dataPackage;
    private int trip_pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoping_cart_viewer);

        shoppingCartRecyclerView = (RecyclerView) findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(this);
        shoppingCartRecyclerView.setLayoutManager(layoutManager);

        // Intent
        Intent intent = getIntent();
        trip_pos = intent.getIntExtra(EXTRA_TRIPDATA_POS, -1);

        // Setup View Reference
        dataPackage = DataPackage.readDataFromStorage(this);
        DataPackage.TripData td = dataPackage.tripData.get(trip_pos);

        mAdapter = new ShoppingCartAdapter(td, this);
        shoppingCartRecyclerView.setAdapter(mAdapter);
        setTitle("Shopping Cart");
    }
}
