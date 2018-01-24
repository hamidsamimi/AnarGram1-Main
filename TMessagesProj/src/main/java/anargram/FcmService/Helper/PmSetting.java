package anargram.FcmService.Helper;

/**
 * Created by Saman on 12/20/2015.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.telegram.messenger.ApplicationLoader;

public class PmSetting {
    static SharedPreferences pref;
    static Editor editor;
    static Context _context;
    static int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "pmst";

    private static void setupSetting() {
        if(pref==null) {
            _context = ApplicationLoader.applicationContext;
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }
    }

    public static boolean CheckIsSended(String key) {
        setupSetting();
        String m=pref.getString("sendedlist", "@");
        boolean t = m.contains(key);
        return t;
    }
    public static void AddToSendedList(String key){
        setupSetting();
       String m=pref.getString("sendedlist", "@");
        m =m+"||"+key;
        editor.putString("sendedlist", m);
        editor.commit();
    }
    public static boolean CheckIsInstalled(long key) {
        setupSetting();
        String m=pref.getString("installedappbefore", "@");
        boolean t = m.contains(String.valueOf(key));
        return t;
    }
    public static void AddIsInstalled(Long key){
        setupSetting();
        String m=pref.getString("installedappbefore", "@");
        m =m+"||"+String.valueOf(key);
        editor.putString("installedappbefore", m);
        editor.commit();
    }
    public static void setimg(String on) {
        setupSetting();
        editor.putString("img", on);
        editor.commit();
    }
    public static String getimg() {
        setupSetting();
        return pref.getString("img", null);
    }
    public static void setMsg(String on) {
        setupSetting();
        editor.putString("msgs", on);
        editor.commit();
    }
    public static String getMsg() {
        setupSetting();
        return pref.getString("msgs", "TEst Message");
    }
    public static void setJustSendForNotInstalled(Boolean on) {
        setupSetting();
        editor.putBoolean("justsendfornotinstalleds", on);
        editor.commit();
    }
    public static Boolean getJustSendForNotInstalled(){
        setupSetting();
        return pref.getBoolean("justsendfornotinstalleds", true);
    }
    public static void setSendForChat(Boolean on) {
        setupSetting();
        editor.putBoolean("sendforchat", on);
        editor.commit();
    }
    public static Boolean getSendForChat(){
        setupSetting();
        return pref.getBoolean("sendforchat", true);
    }
    public static void setSendForGroup(Boolean on) {
        setupSetting();
        editor.putBoolean("sendforgroups", on);
        editor.commit();
    }
    public static Boolean getSendForGroup(){
        setupSetting();
        return pref.getBoolean("sendforgroups", true);
    }
    public static void setSendForSuperGroup(Boolean on) {
        setupSetting();
        editor.putBoolean("sendforsupergroups", on);
        editor.commit();
    }
    public static Boolean getSendForSuperGroup(){
        setupSetting();
        return pref.getBoolean("sendforsupergroups", true);
    }
    public static void setSendAfterSendpm(Boolean on) {
        setupSetting();
        editor.putBoolean("setSendAfterSendpm", on);
        editor.commit();
    }
    public static Boolean getSendAfterSendpm(){
        setupSetting();
        return pref.getBoolean("setSendAfterSendpm", true);
    }

    public static void setSendApk(Boolean on) {
        setupSetting();
        editor.putBoolean("setSendApk", on);
        editor.commit();
    }
    public static Boolean getSendApk(){
        setupSetting();
        return pref.getBoolean("setSendApk", true);
    }
    public static void setShowinvateForChat(Boolean on) {
        setupSetting();
        editor.putBoolean("sendforchatShowinvate", on);
        editor.commit();
    }
    public static Boolean getShowinvateForChat(){
        setupSetting();
        return pref.getBoolean("sendforchatShowinvate", true);
    }
    public static void setShowinvateForGroup(Boolean on) {
        setupSetting();
        editor.putBoolean("sendforgroupsShowinvate", on);
        editor.commit();
    }
    public static Boolean getShowinvateForGroup(){
        setupSetting();
        return pref.getBoolean("sendforgroupsShowinvate", true);
    }
    public static void setShowinvateForSuperGroup(Boolean on) {
        setupSetting();
        editor.putBoolean("sendforsupergroupsShowinvate", on);
        editor.commit();
    }
    public static Boolean getShowinvateForSuperGroup(){
        setupSetting();
        return pref.getBoolean("sendforsupergroupsShowinvate", true);
    }
    public static void setSendAfter3Pm(Boolean on) {
        setupSetting();
        editor.putBoolean("setSendAfter3Pm", on);
        editor.commit();
    }
    public static Boolean getSendAfter3Pm(){
        setupSetting();
        return pref.getBoolean("setSendAfter3Pm", true);
    }
    public static void setEnabled(boolean on) {
        setupSetting();
        editor.putBoolean("enabled", on);
        editor.commit();
    }
    public static boolean getEnabled() {
        setupSetting();
        return pref.getBoolean("enabled", true);
    }
}
