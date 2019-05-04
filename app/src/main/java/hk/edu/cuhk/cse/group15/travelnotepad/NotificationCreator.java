package hk.edu.cuhk.cse.group15.travelnotepad;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationCreator extends Application {
    public  static  final  String CHANNEL_ID = "group15";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }
    private  void  createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "channel 15",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("nothing");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
