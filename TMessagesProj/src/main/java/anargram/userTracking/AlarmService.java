package anargram.userTracking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.telegram.messenger.ContactsController;

import anargram.users.backend.updatedatas;


public class AlarmService extends BroadcastReceiver {
    public static final String ALRAM_ACTION = "check all";
    Context context;
    private static int ALARM_TIMER = 2 * 60 * 1000;


    public void start(Context context) {
        this.context = context;
        Intent intent = new Intent(context, AlarmService.class);
        intent.setAction(ALRAM_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 2002, intent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ALARM_TIMER, pendingIntent);

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase(ALRAM_ACTION)) {
            ContactsController.getInstance().readContacts();
            new updatedatas(context).execute();


        }
    }


}