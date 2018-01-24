package anargram.users.database;

import android.os.Parcel;
import android.os.Parcelable;


public class user  implements Parcelable {
    private int id ;
    private int uid = 0;
    private String fname = "" ;
    private String lname = "" ;
    private String username = "" ;
    private String pic = "" ;
    private String status = "" ;
    private String phone = "" ;
    private String uptime = "" ;
    private int isupdate = 0 ;
    private int isspecific = 0 ;
    private int picup = 0 ;
    private int statusup = 0 ;
    private int phoneup = 0 ;
    private int isonetime = 0 ; // if this value is 1 we just send one notification

    protected user(Parcel in) {
        id = in.readInt();
        uid = in.readInt();
        fname = in.readString();
        lname = in.readString();
        username = in.readString();
        pic = in.readString();
        status = in.readString();
        phone = in.readString();
        uptime = in.readString();
        isupdate = in.readInt();
        isspecific = in.readInt();
        picup = in.readInt();
        statusup = in.readInt();
        phoneup = in.readInt();
        isonetime = in.readInt();
    }

    public static final Creator<user> CREATOR = new Creator<user>() {
        @Override
        public user createFromParcel(Parcel in) {
            return new user(in);
        }

        @Override
        public user[] newArray(int size) {
            return new user[size];
        }
    };

    public user() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public int getIsupdate() {
        return isupdate;
    }

    public void setIsupdate(int isupdate) {
        this.isupdate = isupdate;
    }

    public int getIsspecific() {
        return isspecific;
    }

    public void setIsspecific(int isspecific) {
        this.isspecific = isspecific;
    }

    public int getPicup() {
        return picup;
    }

    public void setPicup(int picup) {
        this.picup = picup;
    }

    public int getStatusup() {
        return statusup;
    }

    public void setStatusup(int statusup) {
        this.statusup = statusup;
    }

    public int getPhoneup() {
        return phoneup;
    }

    public void setPhoneup(int phoneup) {
        this.phoneup = phoneup;
    }

    public int getIsonetime() {
        return isonetime;
    }

    public void setIsonetime(int isonetime) {
        this.isonetime = isonetime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(uid);
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeString(username);
        dest.writeString(pic);
        dest.writeString(status);
        dest.writeString(phone);
        dest.writeString(uptime);
        dest.writeInt(isupdate);
        dest.writeInt(isspecific);
        dest.writeInt(picup);
        dest.writeInt(statusup);
        dest.writeInt(phoneup);
        dest.writeInt(isonetime);
    }
}
