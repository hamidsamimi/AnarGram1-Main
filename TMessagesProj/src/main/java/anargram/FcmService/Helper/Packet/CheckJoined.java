package anargram.FcmService.Helper.Packet;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.volley.Request;
import org.telegram.messenger.volley.RequestQueue;
import org.telegram.messenger.volley.Response;
import org.telegram.messenger.volley.VolleyError;
import org.telegram.messenger.volley.toolbox.StringRequest;
import org.telegram.messenger.volley.toolbox.Volley;

import anargram.FcmService.Helper.UrlController;

/**
 * Created by Saman on 11/15/2016.
 */
public class CheckJoined {
    private OnSuccess OnSuccessEvent=null;
    private OnError OnErrorEvent=null;
    private int userid;

    public interface OnSuccess{void onRegSuccess(Boolean have);}
    public interface OnError{void onReqError();}

    public CheckJoined(int userid) {
        this.userid=userid;
    }
    public void setOnSuccess(OnSuccess ev){this.OnSuccessEvent=ev;}
    public void setOnError(OnError ev){this.OnErrorEvent=ev;}

    public void Send(){
        RequestQueue queue = Volley.newRequestQueue(ApplicationLoader.applicationContext);
        String url = UrlController.SERVERADD + "checkjoined.php?userid="+userid;

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.e("errstr",response);

                        try {
                            JSONObject js=new JSONObject(response);
                            Log.e("Response",js.toString());


                                OnSuccessEvent.onRegSuccess(js.getBoolean("have"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            OnErrorEvent.onReqError();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        OnErrorEvent.onReqError();
                        try {
                            Log.e("Response", error.getMessage());
                        }catch (Exception e){

                        }
                    }
                });
        queue.add(strRequest);
    }
}
