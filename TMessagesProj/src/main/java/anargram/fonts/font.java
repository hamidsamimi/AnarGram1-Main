package anargram.fonts;

import android.os.Parcel;
import android.os.Parcelable;



public class font implements Parcelable{

    String name , address ;

    public font( String name , String address){
        this.name = name ;
        this.address = address ;
    }

    protected font(Parcel in) {
        name = in.readString();
        address = in.readString();
    }

    public static final Creator<font> CREATOR = new Creator<font>() {
        @Override
        public font createFromParcel(Parcel in) {
            return new font(in);
        }

        @Override
        public font[] newArray(int size) {
            return new font[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
    }
}
