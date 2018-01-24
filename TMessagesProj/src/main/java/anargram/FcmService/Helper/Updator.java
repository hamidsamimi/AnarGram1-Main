package anargram.FcmService.Helper;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import ir.anargram.messenger.R;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.LaunchActivity;


import java.io.File;

/**
 * Created by Saman on 11/19/2016.
 */
public class Updator {
    public static  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (LaunchActivity.thiscontext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //   Log.v(TAG,"Permission is granted");
                return true;
            } else {

                // Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(LaunchActivity.thiscontext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //    Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    public static void StartUpdate() {
        //DownloadUpdateTask dut=new DownloadUpdateTask(ApplicationLoader.applicationContext);
        //dut.execute("update");
        if(!isStoragePermissionGranted())return ;
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = "irgram.apk";
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);

        //Delete update file if exists
        File file = new File(destination);
        if (file.exists())
            //file.delete() - test this, I think sometimes it doesnt work
            file.delete();

        //get url of app on server
        String url =UrlController.SERVERADD+"irgram.apk";

        //set downloadmanager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription(LocaleController.getString("Updating", R.string.Updating));
        request.setTitle(LocaleController.getString("AppName", R.string.AppName));

        //set destination
        request.setDestinationUri(uri);

        // get download service and enqueue file
        final DownloadManager manager = (DownloadManager)ApplicationLoader.applicationContext.getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        //set BroadcastReceiver to install app when .apk is downloaded
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                install.setDataAndType(uri,
                        manager.getMimeTypeForDownloadedFile(downloadId));
                ApplicationLoader.applicationContext.startActivity(install);

                ApplicationLoader.applicationContext.unregisterReceiver(this);

            }
        };
        //register receiver for when .apk download is compete
        ApplicationLoader.applicationContext.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public static void DoneDownload() {
        File file = new File(Environment.getExternalStorageDirectory() + "/TelegramGifs/irgram.apk");
        if(file.exists()){

        }
    }
}
