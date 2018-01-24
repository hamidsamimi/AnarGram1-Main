package anargram.FcmService.Threads;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.volley.DefaultRetryPolicy;
import org.telegram.messenger.volley.Request;
import org.telegram.messenger.volley.RequestQueue;
import org.telegram.messenger.volley.Response;
import org.telegram.messenger.volley.VolleyError;
import org.telegram.messenger.volley.toolbox.JsonObjectRequest;
import org.telegram.messenger.volley.toolbox.Volley;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import anargram.FcmService.Helper.UrlController;
import anargram.FcmService.Service.DownloadTask;

/**
 * Created by Saman on 9/22/2016.
 */
public class GifLoader {
    private Context context;
    private int userid=0;
    private boolean ischannel=false;
    private AvatarDrawable av=null;
    private ImageReceiver dialogcell;
    public static Map<String,String> Caches=new HashMap<>();
    public static Map<String,String> UrlCaches=new HashMap<>();
    private BackupImageView backupImageViews;
    private boolean useBackupimageview=false;
    private boolean isgif=false;
    private String thismd5="";
    String urls =null;
    public GifLoader(Object t, int userids,boolean ischannels, AvatarDrawable avs, Context mcontext){
        if(t instanceof ImageReceiver){
            this.dialogcell=(ImageReceiver)t;
        }else if(t instanceof BackupImageView){
            this.backupImageViews=(BackupImageView)t;
            useBackupimageview=true;
        }

        this.ischannel=ischannels;
        this.userid=userids;
        this.context=mcontext;
    }
    public GifLoader(ImageReceiver dialogs, int userids,boolean ischannels, AvatarDrawable avs, Context mcontext) {
        this.dialogcell=dialogs;
        this.userid=userids;
        this.ischannel=ischannels;
        this.context=mcontext;
        this.av=avs;
    }
    public GifLoader(BackupImageView backupImageViews, int userids,boolean ischannels, AvatarDrawable avs, Context mcontext) {
        this.backupImageViews=backupImageViews;
        useBackupimageview=true;
        this.ischannel=ischannels;
        this.userid=userids;
        this.context=mcontext;
        this.av=avs;

    }
    public  boolean isChached(){
        return isChached(false);
    }
    public  boolean isChached(boolean stoprunagain){
        String userids=(ischannel?"c":"")+userid;
        if( GifLoader.Caches.containsKey(userids)) {
            File s=new File(GifLoader.Caches.get(userids));
            if (dialogcell != null) {
                dialogcell.setImage(s.getAbsolutePath(),"50_50",null,null,0);
                if(!stoprunagain)this.checkifhavegifavatar();
                return true;
            }else{
                backupImageViews.getImageReceiver().setImage(s.getAbsolutePath(),"50_50",null,null,0);
                if(!stoprunagain)this.checkifhavegifavatar();
                return true;
            }

        }else {
            if(!stoprunagain)this.checkifhavegifavatar();

            return false;
        }
    }
    public static boolean IsCached(BackupImageView bk,ImageReceiver ir,int userid,boolean ischannel){
        return false;
        //return new GifLoader((bk!=null?bk:ir),userid,ischannel,null,ApplicationLoader.applicationContext).isChached();
    }
    public static String GetUserGifpath(int userid,boolean ischannel){
        return Caches.get((ischannel?"c":"")+userid);
    }
    public static boolean haveGifpath(int userid,boolean ischannel){
        return Caches.containsKey((ischannel?"c":"")+userid);
    }
    public static String GetUserGifUrl(int userid,boolean ischannel){
        return UrlCaches.get((ischannel?"c":"")+userid);
    }
    public static boolean haveGif(int userid,boolean ischannel){
        return UrlCaches.containsKey((ischannel?"c":"")+userid);
    }
    private void cachit(){
        if(!ischannel) {
            urls= UrlController.SERVERADD + "avatar/" + userid +(isgif?".gif":".mp4");
        }else{
            urls=UrlController.SERVERADD + "avatar/c" + userid + (isgif?".gif":".mp4");
        }
        UrlCaches.put((ischannel?"c":"")+userid,urls);
        DownloadTask t=new DownloadTask(context);
        t.execute(urls,(ischannel?"c":"")+userid+(isgif?".gif":".mp4"),thismd5,(ischannel?"c":"")+userid,this,userid);

    }
    public static boolean iHaveGifAvatar(){
        File file = new File(Environment.getExternalStorageDirectory() + "/TelegramGifs", UserConfig.getCurrentUser().id+".gif");
        File file2 = new File(Environment.getExternalStorageDirectory() + "/TelegramGifs",  UserConfig.getCurrentUser().id+".mp4");
        return file.exists()||file2.exists();
    }
    public static void ClearCach(int userid,boolean ischannel){
        Caches.remove(ischannel?"c":""+userid);
        File file = new File(Environment.getExternalStorageDirectory() + "/TelegramGifs", ischannel?"c":""+userid+".gif");
        File file2 = new File(Environment.getExternalStorageDirectory() + "/TelegramGifs", ischannel?"c":""+userid+".mp4");
        if (file.exists()) file.delete();
        if (file2.exists()) file2.delete();
    }
    private void checkifhavegifavatar(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                UrlController.SERVERADD+"checkgif.php?"+(ischannel?"channelid":"userid")+"="+userid, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(!response.isNull("have")&&(response.getString("have").equals("gif")||response.getString("have").equals("mp4"))){
                                isgif=response.getString("have").equals("gif");
                                thismd5=response.getString("md5");

                                cachit();
                               // getUserGif();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("tmessages", "Error: " + error.getMessage());
                    }
                }) {


        };
        jsonObjReq.setShouldCache(false);
        jsonObjReq.setTag("search");
        RequestQueue requestQueue = Volley.newRequestQueue(ApplicationLoader.applicationContext);
        requestQueue.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                30 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}