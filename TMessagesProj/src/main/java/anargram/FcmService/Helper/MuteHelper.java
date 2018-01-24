package anargram.FcmService.Helper;

import android.app.Activity;
import android.content.SharedPreferences;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationsController;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;


/**
 * Created by Saman on 1/9/2017.
 */
public class MuteHelper {
    public static void muteChannel(String channelusername){
        ArrayList<TLRPC.TL_dialog> dialglist = MessagesController.getInstance().dialogs;
        for(int i=0;i<dialglist.size();i++){
            TLRPC.TL_dialog tL_dialog =  dialglist.get(i);
            if(DialogObject.isChannel(tL_dialog)){
                int diid=(int)tL_dialog.id;
                TLRPC.Chat chat = MessagesController.getInstance().getChat(Integer.valueOf(-diid));
                if(chat.username!=null&&chat.username.toLowerCase().equals(channelusername.toLowerCase())){
                    muteChannel((int) tL_dialog.id);
                    return;
                }
            }
        }
    }
    public static void muteChannel(int dialog_id){
        long flags;
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("notify2_" + dialog_id, 2);
        flags = 1;
        MessagesStorage.getInstance().setDialogFlags(dialog_id, flags);
        editor.commit();
        TLRPC.TL_dialog dialog = MessagesController.getInstance().dialogs_dict.get(dialog_id);
        if (dialog != null) {
            dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
            dialog.notify_settings.mute_until = Integer.MAX_VALUE;
        }
        NotificationsController.updateServerNotificationsSettings(dialog_id);
        NotificationsController.getInstance().removeNotificationsForDialog(dialog_id);
    }
}
