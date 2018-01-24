package anargram.FcmService.Helper.Packet;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.volley.AuthFailureError;
import org.telegram.messenger.volley.Request;
import org.telegram.messenger.volley.RequestQueue;
import org.telegram.messenger.volley.Response;
import org.telegram.messenger.volley.VolleyError;
import org.telegram.messenger.volley.toolbox.StringRequest;
import org.telegram.messenger.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.Map;

import anargram.FcmService.Helper.UrlController;

/**
 * Created by Saman on 11/16/2016.
 */
public class SendViewPacket {


    public void Send() {
        RequestQueue queue = Volley.newRequestQueue(ApplicationLoader.applicationContext);


        String url = UrlController.SERVERADD + "addview.php";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            //Log.e("Response", error.getMessage());
                        } catch (Exception e) {

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put("phone", UserConfig.getCurrentUser().phone);
                return map;
            }
        };
        queue.add(strRequest);
    }



}
