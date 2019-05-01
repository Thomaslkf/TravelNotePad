package hk.edu.cuhk.cse.group15.travelnotepad;

import android.content.Context;
import android.location.Address;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;

public class DataPackage {
    public List<TripData> tripData = new ArrayList<TripData>();

    public static class TripData {
        public static class Checkpoint {
            public String name;
            public Address address;
            public String description;
            public List<String> shoppingCart = new ArrayList<>();

            public Checkpoint(){}

            public static Checkpoint getDummy() {
                Checkpoint c = new Checkpoint();
                c.name = "Dummy Checkpoint";
                c.description = "Dummy Des";

                c.shoppingCart.add("Dummy Item #1");
                c.shoppingCart.add("Dummy Item #2");

                return c;
            }
        }

        public String name;
        public Address origin;
        public Address dst;
        public Date[] date = new Date[2];

        public List<ArrayList<Checkpoint>> dayActivity = new ArrayList<>();

        public TripData(){

            updateTripDate("2018/01/01", "2018/01/01");
        }

        public void initiateDayActivity(int duration){
            dayActivity.clear();
            for (int i = 0; i < duration; i++) {
                dayActivity.add(new ArrayList<Checkpoint>());
//                dayActivity.get(dayActivity.size()-1).add(Checkpoint.getDummy());
            }
        }

        public void updateTripDate(String from_date, String to_date){
            SimpleDateFormat simple = new SimpleDateFormat("yyyy/MM/dd");
            try {
                date[0] = simple.parse(from_date);
                date[1] = simple.parse(to_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @NonNull
        @Override
        public String toString() {
            Gson gson = new Gson();
            return gson.toJson(this, TripData.class);
        }
    }

    public void writeDataToStorage(Context context) {
        try {
            Gson gson = new Gson();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("data.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(gson.toJson(this, DataPackage.class));
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    };

    public static DataPackage readDataFromStorage(Context context){
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("data.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        if(ret.length() > 0)
            return new Gson().fromJson(ret, DataPackage.class);
        else
            return new DataPackage();
    }

    public static void purgeData(Context context){

        new DataPackage().writeDataToStorage(context);
    };

}
