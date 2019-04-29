package hk.edu.cuhk.cse.group15.travelnotepad;

import android.content.Context;
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

public class DataPackage {
    List<TripData> tripData = new ArrayList<TripData>();

    static class TripData {
        static class Checkpoint {
            public String name;
            public double[] coordinate = new double[2];

            public Checkpoint(String name, double lat, double lng){
                this.name = name;
                this.coordinate[0] = lat;
                this.coordinate[1] = lng;
            }
        }

        public String name;
        public String origin;
        public String dst;
        public Date[] duration = new Date[2];

        public List<ArrayList<Checkpoint>> dayActivity = new ArrayList<>();

        public TripData( String name, String origin, String dst, String start_date, String end_date){
            this.name = name;
            this.origin = origin;
            this.dst = dst;

            java.text.SimpleDateFormat simple = new SimpleDateFormat();
            simple.applyPattern("yyyy/MM/dd");
            try {
                this.duration[0] = simple.parse(start_date);
                this.duration[1] = simple.parse(end_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    static void generateTestData(Context context) {
        TripData tripData[] = {
                new TripData("Grad Trip", "Hong Kong", "Tokyo", "2019/05/22", "2019/05/25"),
                new TripData("Family Trip", "Hong Kong", "Taipei", "2019/05/31", "2019/06/5"),
        };

        DataPackage dp = new DataPackage();
        dp.tripData.add(tripData[0]);
        dp.tripData.add(tripData[1]);

        dp.writeDatToStorage(context);
    };

    void writeDatToStorage(Context context) {
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

    static DataPackage readDataFromStorage(Context context){
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

        return new Gson().fromJson(ret, DataPackage.class);
    }

    static void purgeData(Context context){

        new DataPackage().writeDatToStorage(context);
    };

}
