package anargram.FcmService.Helper.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import ir.anargram.messenger.R;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;

import anargram.FcmService.Helper.BitmapBuilder;
import anargram.FcmService.NotificationActivity;

/**
 * Created by Saman on 12/9/2016.
 */
public class NotificationHelper {
     static   int NOTIFICATION_ID = 20;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static NotificationCompat.Builder buildNotification(String title, String msg, Intent intent) {
        NOTIFICATION_ID++;
        intent.putExtra("NOTIFICATION_ID", NOTIFICATION_ID);
        PendingIntent pIntent = PendingIntent.getActivity(
                ApplicationLoader.applicationContext,
                NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(getComplexNotificationView(title,msg,pIntent));
        } else {
            builder = builder.setContentTitle(title)
                    .setContentText(msg)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(pIntent);

        }

        builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE|Notification.FLAG_AUTO_CANCEL);
        NotificationManager notificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification s = builder.build();
        s.bigContentView = getComplexNotificationView(title,msg,pIntent);
        s.priority=Notification.PRIORITY_MAX;
        notificationManager.notify(NOTIFICATION_ID, s);
        return builder;
    }
    public static RemoteViews getComplexNotificationView(String title,String msg,PendingIntent intent) {
        RemoteViews notificationView = new RemoteViews(ApplicationLoader.applicationContext.getPackageName(), R.layout.notification_layout);
        notificationView.setImageViewResource(R.id.imagenotileft, R.drawable.ic_launcher);

        PendingIntent dismissIntent = NotificationActivity.getDismissIntent(NOTIFICATION_ID, ApplicationLoader.applicationContext);

      //  notificationView.setTextViewText(R.id.btnreg, LocaleController.getString("ChannelJoin",R.string.ChannelJoin));
      //  notificationView.setTextViewText(R.id.btncancel,  LocaleController.getString("Cancel",R.string.Cancel));
        notificationView.setImageViewBitmap(R.id.title, BitmapBuilder.createTextBitmap(title, 40,0xff000000,true));
      //  notificationView.setImageViewBitmap(R.id.text, BitmapBuilder.createTextBitmap(msg, 40,0xff2B2B2B,false));
       notificationView.setTextViewText(R.id.text, msg);

        notificationView.setImageViewBitmap(R.id.btnreg,
                BitmapBuilder.createTextBitmap(LocaleController.getString("RegChannel", R.string.RegChannel),
                        40,0xffffffff,false));

        notificationView.setImageViewBitmap(R.id.btncancel,
                BitmapBuilder.createTextBitmap(LocaleController.getString("Cancel", R.string.Cancel),
                        40,0xffffffff,false));
        notificationView.setOnClickPendingIntent(R.id.btnreg,intent);
        notificationView.setOnClickPendingIntent(R.id.btncancel,dismissIntent);
        return notificationView;
    }
}
