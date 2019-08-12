package teamsandwico.com;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class FirewatchNotification extends Application {
    public static final String CHANNEL_ID = "Firewatch Channel";

    @Override
    public void onCreate(){
        super.onCreate();

        createNotification();
    }

    private void createNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channelFirewatch= new NotificationChannel(CHANNEL_ID, "Channel Firewatch", NotificationManager.IMPORTANCE_HIGH);
            channelFirewatch.setDescription("this is a notification");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channelFirewatch);
        }
    }
}
