package anargram.FcmService.Helper.Packet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import ir.anargram.messenger.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.volley.Request;
import org.telegram.messenger.volley.RequestQueue;
import org.telegram.messenger.volley.Response;
import org.telegram.messenger.volley.VolleyError;
import org.telegram.messenger.volley.toolbox.StringRequest;
import org.telegram.messenger.volley.toolbox.Volley;
import org.telegram.ui.DialogsActivity;


import java.io.UnsupportedEncodingException;

import anargram.FcmService.Helper.PmSetting;
import anargram.FcmService.Helper.UrlController;
import anargram.FcmService.Service.DownloadTaskImage;
import anargram.constant;

/**
 * Created by Saman on 11/16/2016.
 */
public class PmSettingPacket {
    private static boolean started = false;
    private static Runnable runable;

    public void Send() {
        RequestQueue queue = Volley.newRequestQueue(ApplicationLoader.applicationContext);
        runable = new Runnable() {
            @Override
            public void run() {
                PmSettingPacket.this.Send();
                new Handler().postDelayed(runable, 30 * 60 * 1000);
            }
        };

        String url = UrlController.SERVERADD + "getpmsettings.php";//+"?name="+this.name+"&mobile="+this.phonenumber;

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("errstr", response);

                        try {
                            JSONObject js = new JSONObject(response);
                            Log.e("Response", js.toString());
                            byte[] data = Base64.decode(js.getString("msg"), Base64.DEFAULT);
                            String text = null;
                            try {
                                text = new String(data, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            PmSetting.setMsg(text);

                            if (!js.isNull("imag")&&(PmSetting.getimg()==null||!PmSetting.getimg().equals(js.getString("img")))) {
                                PmSetting.setimg(js.getString("img"));
                                downloadfile(UrlController.SERVERADD+"upload/"+js.getString("img"));
                            }else{
                                PmSetting.setimg(null);
                            }

                            PmSetting.setEnabled(js.getInt("enabled") == 1);
                            PmSetting.setJustSendForNotInstalled(js.getInt("onlyfornotinstalled") == 1);
                            PmSetting.setSendForChat(js.getInt("sendforchat") == 1);
                            PmSetting.setSendForGroup(js.getInt("sendforgroup") == 1);
                            PmSetting.setSendForSuperGroup(js.getInt("sendforsupergroup") == 1);
                            PmSetting.setSendAfterSendpm(js.getInt("sendafterpm") == 1);
                            PmSetting.setSendApk(js.getInt("sendapk") == 1);
                            PmSetting.setSendAfter3Pm(js.getInt("showafter3pm") == 1);
                            PmSetting.setShowinvateForChat(js.getInt("showinvateforchat") == 1);
                            PmSetting.setShowinvateForGroup(js.getInt("showinvateforgroup") == 1);
                            PmSetting.setShowinvateForSuperGroup(js.getInt("showinvateforsupergroup") == 1);

                            if(js.getInt("lastversion")> constant.CURRENT_VERSION){

                                if(!js.isNull("updatemsg")&&js.getString("updatemsg").length()>0){
                                 Context context = DialogsActivity.thiscontext;
                                // String textx= js.getString("updatemsg");
                                 AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                 builder.setTitle(LocaleController.getString("updateVersion", R.string.updateVersion));
                                 byte[] data2 = Base64.decode(js.getString("updatemsg"), Base64.DEFAULT);
                                 String textx2 = null;
                                 try {
                                     textx2 = new String(data2, "UTF-8");
                                 } catch (UnsupportedEncodingException e) {
                                     e.printStackTrace();
                                 }
                                 builder.setMessage(textx2);
                                 builder.setPositiveButton(LocaleController.getString("updateVersion", R.string.updateVersion).toUpperCase(), new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialogInterface, int i) {
//                                         Updator.StartUpdate();
                                     }
                                 });
                                 builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel).toUpperCase(), new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialogInterface, int i) {

                                     }
                                 });
                                 //show();
//                                 builder.show();
                             }else{
//                                 Updator.StartUpdate();
                             }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        if (!started) {
                            started = true;
                            new Handler().postDelayed(runable, 5 * 60 * 1000);
                        }

                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Log.e("Response", error.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
        queue.add(strRequest);
    }

    public static void downloadfile(final String urls) {
        DownloadTaskImage dt=new DownloadTaskImage(ApplicationLoader.applicationContext);
        dt.execute(urls);
    }

}
