package anargram.FcmService.Helper.Channel;

import org.json.JSONObject;

/**
 * Created by Saman on 12/6/2016.
 */
public interface OnResponseReadyListener {

    void OnResponseReady(boolean error, JSONObject data, String message);
}
