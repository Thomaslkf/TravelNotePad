package hk.edu.cuhk.cse.group15.travelnotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.checkerframework.checker.linear.qual.Linear;

import java.util.ArrayList;

public class CheckpointEditor extends AppCompatActivity {
    private static final String EXTRA_DES = "EXTRA_DES";
    private static final String EXTRA_LIST = "EXTRA_LIST";

    LinearLayout holder;
    String des;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoint_editor);

        Intent intent = getIntent();
        des = intent.getStringExtra(EXTRA_DES);
        list = intent.getStringArrayListExtra(EXTRA_LIST);
        holder = findViewById(R.id.itemHolder);

        ((TextView)findViewById(R.id.desContent)).setText(des);
        for(String item : list){
            TextView v = new TextView( new ContextThemeWrapper(this, R.style.shopping_list_item_style), null, 0);
            v.setText(item);

            holder.addView(v);
        }
    }

    public void finish(View v){
        des = ((TextView)findViewById(R.id.desContent)).getText().toString();

        getIntent().putExtra(EXTRA_DES, des);
        getIntent().putStringArrayListExtra(EXTRA_LIST, list);
        setResult(RESULT_OK, getIntent());
        finish();
    }

    public void add(View v){
        String itemName = ((TextView)findViewById(R.id.itemName)).getText().toString();
        ((TextView)findViewById(R.id.itemName)).setText("");
        list.add(itemName);

        TextView tv = new TextView( new ContextThemeWrapper(this, R.style.shopping_list_item_style), null, 0);
        tv.setText(itemName);

        holder.addView(tv);
    }
}
