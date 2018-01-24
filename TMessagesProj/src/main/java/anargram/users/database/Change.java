package anargram.users.database;

import android.os.Parcel;
import android.os.Parcelable;


public class Change implements Parcelable{

    private int id ;
    private int uid ;
    private int type ; // 1 for photo changed and 2 for phone number change and 3 for user name change
    private String time ;


    protected Change(Parcel in) {
        id = in.readInt();
        uid = in.readInt();
        type = in.readInt();
        time = in.readString();
    }

    public static final Creator<Change> CREATOR = new Creator<Change>() {
        @Override
        public Change createFromParcel(Parcel in) {
            return new Change(in);
        }

        @Override
        public Change[] newArray(int size) {
            return new Change[size];
        }
    };

    public Change() {

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(uid);
        dest.writeInt(type);
        dest.writeString(time);
    }
}
