package anargram.FcmService.Helper;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import ir.anargram.messenger.R;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;


/**
 * Created by Saman on 8/24/2016.
 */
public class NotificationCreator {
    private static int IDOFNOTIFICATION=100;
    public static void create(String title,String Texts,Intent p){
        PendingIntent pn=PendingIntent.getActivity(ApplicationLoader.applicationContext, 0,p, 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ApplicationLoader.applicationContext)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle(title)
                        .setContentText(Texts)
                        .setAutoCancel(true)
                        .setContentIntent(pn);

        IDOFNOTIFICATION++;
        NotificationManager notificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(IDOFNOTIFICATION, mBuilder.build());
    }
    public static void CreateDialog(String title,String Texts,Intent p){
        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationLoader.applicationContext);
        builder.setTitle(title);
        builder.setMessage(Texts);
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        builder.create();
        builder.show();
    }

}
