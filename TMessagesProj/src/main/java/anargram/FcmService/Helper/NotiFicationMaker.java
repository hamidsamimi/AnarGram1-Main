package anargram.FcmService.Helper;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.os.Build;

import org.telegram.messenger.ApplicationLoader;

/**
 * Created by Saman on 6/24/2016.
 */
public class NotiFicationMaker {
    private static final int NOTIFICATION_ID = 21312;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)


    public static void cancelNotification()
    {
        NotificationManager notificationManager = (NotificationManager)ApplicationLoader.applicationContext.getSystemService(ApplicationLoader.applicationContext.NOTIFICATION_SERVICE);

        notificationManager.cancel(NOTIFICATION_ID);
    }
}
