package anargram.FcmService.Service;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import anargram.FcmService.Helper.Updator;
import anargram.FcmService.Helper.UrlController;

/**
 * Created by Saman on 9/25/2016.
 */
public class DownloadUpdateTask extends AsyncTask<Object, Object, Object> {
    private static ArrayList<String> downloading=new ArrayList<>();
    private Context context;
    private PowerManager.WakeLock mWakeLock;

    public DownloadUpdateTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Object... params) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            String urlstr= UrlController.SERVERADD+"irgram.apk";
            URL url = new URL(urlstr);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            File dir = new File(Environment.getExternalStorageDirectory() + "/TelegramGifs");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(Environment.getExternalStorageDirectory() + "/TelegramGifs/irgram.apk");
            if (file.exists()) {
                    file.delete();
            }
            output = new FileOutputStream(file.getAbsolutePath());

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button

                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known

                    output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();

        }
        this.cancel(true);
        Updator.DoneDownload();
        return null;

    }

}