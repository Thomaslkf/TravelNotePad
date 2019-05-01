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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CloudDownload extends AppCompatActivity {
    RequestQueue queue;
    String data;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_download);

        ApplicationInfo app = null;
        try {
            app = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        bundle = app.metaData;

        queue = Volley.newRequestQueue(this);
    }

    public void download(View v) {
        String code = ((EditText) findViewById(R.id.TripCode)).getText().toString();
        get(bundle.getString("server.address") + "/download/" + code);
    }

    private void get(String urlString) {
        StringRequest postRequest = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        TripData td = new Gson().fromJson(response, TripData.class);
                        write(td);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.networkResponse.statusCode + "");
                        if (error.networkResponse.statusCode == 400) toaster("Trip Not Found");
                    }
                }
        );
        queue.add(postRequest);
    }

    private void toaster(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void write(TripData td) {
        DataPackage dp = DataPackage.readDataFromStorage(this);
        dp.tripData.add(td);
        dp.writeDataToStorage(this);
        toaster("Trip Added");
        finish();
    }
}