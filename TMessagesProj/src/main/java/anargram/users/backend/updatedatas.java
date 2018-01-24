package anargram.users.backend;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import ir.anargram.messenger.R;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.LaunchActivity;

import java.util.ArrayList;
import java.util.Iterator;

import anargram.users.database.Change;
import anargram.users.database.changesDBAdapter;
import anargram.users.database.user;
import anargram.users.database.userDBAdapter;

import static android.content.Context.NOTIFICATION_SERVICE;



@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class updatedatas extends AsyncTask {

    userDBAdapter db;
    changesDBAdapter adapter;
    Context context;
    SharedPreferences preferences ;


    public updatedatas(Context c) {
        this.context = c;
        preferences = c.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        db = new userDBAdapter(context);
        adapter = new changesDBAdapter(context);
        db.open();
        adapter.open();

    }

    @Override
    protected Object doInBackground(Object[] params) {


        ContactsController.getInstance().readContacts();
        for (String str : ContactsController.getInstance().usersSectionsDict.keySet()) {
            Iterator it = ((ArrayList) ContactsController.getInstance().usersSectionsDict.get(str)).iterator();
            while (it.hasNext()) {

                TLRPC.TL_contact tL_contact = (TLRPC.TL_contact) it.next();

                TLRPC.User user = MessagesController.getInstance().getUser(Integer.valueOf(tL_contact.user_id));
                user item = new user();
                user Muser = new user();


                if (db != null && user != null && !db.Exists(user.id)) {
                    item.setUid(user.id);
                    if (user.phone != null)
                        item.setPhone(user.phone);
                    if (user.first_name != null)
                        item.setFname(user.first_name);
                    if (user.last_name != null)
                        item.setLname(user.last_name);
                    if (user.username != null)
                        item.setUsername(user.username);
                    if (user.photo != null)
                        item.setPic(String.valueOf(user.photo.photo_id));
                    if (user.status != null) {
                        String st = String.valueOf(user.status);
                        item.setStatus(st.substring(0, st.indexOf("@")));
                    }
                    db.insert(item);
                } else if (db != null && user != null) {
                    Muser = db.getItm(user.id);
                    Change ch = new Change();
                    int update = 0;
                    if (user.username != null) {
                        if (Muser.getUsername() == null || !Muser.getUsername().equals(String.valueOf(user.username))) {
                            ch.setUid(user.id);
                            ch.setType(3);
                            adapter.insert(ch);
                            db.updateUsername(user.id, user.username);
                            int count  = preferences.getInt("ContactChangesCount" , 0 );
                            preferences.edit().putInt("ContactChangesCount", count + 1).commit();
                            update = 3;
                        }
                    }
                    if (user.photo != null) {
                        if (Muser.getPic() == null || !Muser.getPic().equals(String.valueOf(user.photo.photo_id))) {
                            ch.setUid(user.id);
                            ch.setType(1);
                            adapter.insert(ch);
                            db.updatePhoto(user.id, String.valueOf(user.photo.photo_id));
                            int count  = preferences.getInt("ContactChangesCount" , 0 );
                            preferences.edit().putInt("ContactChangesCount", count + 1).commit();
                            update = 1;
                        }
                    }

                    if (user.phone != null) {
                        if (Muser.getPhone() == null || !Muser.getPhone().equals(user.phone)) {
                            ch.setUid(user.id);
                            ch.setType(2);
                            adapter.insert(ch);
                            update = 2;
                            db.updatePhone(user.id, user.phone);
                            int count  = preferences.getInt("ContactChangesCount" , 0 );
                            preferences.edit().putInt("ContactChangesCount", count + 1).commit();
                        }
                    }

                    if (user.status != null) {
                        String st = String.valueOf(user.status);
                        st = st.substring(0, st.indexOf("@"));
                        if (Muser.getStatus() == null || !Muser.getStatus().equals(st)) {
                            db.updateStatus(user.id, st);
                            update = 4;
                        }
                    }


                    if (update != 0) {


                        if (Muser.getIsspecific() == 1) {
                            if (Muser.getIsonetime() == 1) {
                                db.updateIsSpecific(Muser.getUid(), 0);
                                db.updateIsOneTime(Muser.getUid(), 0);
                                db.updateStatusUp(Muser.getUid(), 0);
                                db.updatePhoneUp(Muser.getUid(), 0);
                                db.updatePicUp(Muser.getUid(), 0);

                            }

                            String text = "";
                            String name = "";
                            int k = 0;

                            if (user.username != null) {
                                name = user.username;
                            } else if (user.first_name != null) {
                                name = user.first_name;
                            }
                            switch (update) {
                                case 1:
                                    text = context.getResources().getString(R.string.typeChangePic);
                                    break;
                                case 2:
                                    text = context.getResources().getString(R.string.typeChangephone);
                                    break;
                                case 3:
                                    text = context.getResources().getString(R.string.typeChangeusername);
                                    break;
                                case 4:
                                    if (String.valueOf(user.status).contains("userStatusOnline"))
                                        text = context.getResources().getString(R.string.typeisOnline);
                                    else
                                        text = context.getResources().getString(R.string.typeisOffline);
                            }
                            if (k == 0) {
                                Alert(name, text, user.id);
                            }

                        }
                    }

                }
            }

        }


        db.close();
        adapter.close();


        return true;
    }


    public void Alert(String name, String text, int id) {
        Intent intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
        intent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
        intent.setFlags(32768);

        intent.putExtra("userId", id);

        PendingIntent contentIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_ONE_SHOT);

        Resources r = context.getResources();
        Notification notification = new NotificationCompat.Builder(context)
                .setTicker(r.getString(R.string.SpecificContacts))
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(name)
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/specific"))
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }


}
