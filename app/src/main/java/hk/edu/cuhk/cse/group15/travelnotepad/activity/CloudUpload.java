package hk.edu.cuhk.cse.group15.travelnotepad.activity;

import androidx.appcompat.app.AppCompatActivity;
import hk.edu.cuhk.cse.group15.travelnotepad.DataPackage;
import hk.edu.cuhk.cse.group15.travelnotepad.DataPackage.*;
import hk.edu.cuhk.cse.group15.travelnotepad.R;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class CloudUpload extends AppCompatActivity {
    private static final String EXTRA_UPLOAD_INFO = "EXTRA_UPLOAD_INFO";

    RequestQueue queue;
    String data;
    ClipboardManager clipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_upload);

        ApplicationInfo app = null;
        try {
            app = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Bundle bundle = app.metaData;
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        Intent intent = getIntent();
        data = intent.getStringExtra(EXTRA_UPLOAD_INFO);

        queue = Volley.newRequestQueue(this);
        Log.d("Address", bundle.getString("server.address") + "/submit");
        post(bundle.getString("server.address") + "/submit", data);
    }

    private void post(String urlString, final String data){
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlString,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        ((TextView)findViewById(R.id.TripCode)).setText(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("tripdata", data);

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void copyToClipboard(View v){
        String code = ((TextView)findViewById(R.id.TripCode)).getText().toString();
        ClipData clip = ClipData.newPlainText("TripCode", code);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }
}
