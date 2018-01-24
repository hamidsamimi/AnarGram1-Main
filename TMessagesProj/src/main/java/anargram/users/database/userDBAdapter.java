package anargram.users.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;



public class userDBAdapter  {
    private static final String CREATE_MAINTABLE_USER = "CREATE TABLE \"users\" (\"id\" INTEGER PRIMARY KEY  NOT NULL  UNIQUE , \"uid\" INTEGER NOT NULL  UNIQUE , \"fname\" TEXT, \"lname\" TEXT, \"username\" TEXT, \"pic\" TEXT, \"status\" TEXT, \"phone\" TEXT, \"uptime\" DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP, \"isupdate\" INTEGER NOT NULL  DEFAULT 0, \"isspecific\" INTEGER NOT NULL  DEFAULT 0, \"picup\" INTEGER NOT NULL  DEFAULT 0, \"statusup\" INTEGER NOT NULL  DEFAULT 0, \"phoneup\" INTEGER NOT NULL  DEFAULT 0, \"isonetime\" INTEGER NOT NULL  DEFAULT 0)";

        private static final String DATABASE_MAINTABLE = "users";
        private static final String DATABASE_NAME = "AnarGram3";
        private static final int DATABASE_VERSION = 1;


        private static final String KEY_ID = "id";
        private static final String KEY_UID = "uid";
        private static final String KEY_FNAME = "fname";
        private static final String KEY_LNAME = "lname";
        private static final String KEY_USERNAME = "username";
        private static final String KEY_PIC = "pic";
        private static final String KEY_STATUS = "status";
        private static final String KEY_PHONE = "phone";
        private static final String KEY_UPTIME = "uptime";
        private static final String KEY_ISUPDATE = "isupdate";
        private static final String KEY_ISSPECEFIC = "isspecific";
        private static final String KEY_PICUP = "picup";
        private static final String KEY_STATUSUP = "statusup";
        private static final String KEY_PHONEUP = "phoneup";
        private static final String KEY_ISONETIME = "isonetime";


        private static final String TAG = "AnarGram_USER_TABLE";
        private final DatabaseHelper DBHelper;
        private SQLiteDatabase db;
        private final String[] yek_SH_flashkart;

private static class DatabaseHelper extends SQLiteOpenHelper {
    DatabaseHelper(Context context) {
        super(context, userDBAdapter.DATABASE_NAME, null, userDBAdapter.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(userDBAdapter.CREATE_MAINTABLE_USER);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(userDBAdapter.TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS main");
        onCreate(db);
    }
}

    public userDBAdapter(Context ctx) {
        this.yek_SH_flashkart = new String[]{KEY_ID,KEY_UID,KEY_FNAME,KEY_LNAME,KEY_USERNAME,KEY_PIC,KEY_STATUS,KEY_PHONE,KEY_UPTIME,KEY_ISUPDATE,KEY_ISSPECEFIC,KEY_PICUP,KEY_STATUSUP,KEY_PHONEUP,KEY_ISONETIME};
        this.DBHelper = new DatabaseHelper(ctx);
    }

    public void open() throws SQLException {
        this.db = this.DBHelper.getWritableDatabase();
    }

    public void close() {
        this.DBHelper.close();
    }

    public List<user> getAllItms(int specific) {
        return cursorToList(this.db.query(true, DATABASE_MAINTABLE, this.yek_SH_flashkart, "isspecific == '" + specific + "'", null, null, null, null, null));
    }

    public List<user> getAllItms() {
        return cursorToList(this.db.query(DATABASE_MAINTABLE, this.yek_SH_flashkart, null, null, null, null, null));
    }


    public int getsize() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM users;", null);
        boolean isOk = cursor.moveToFirst();
        return cursor.getInt(0);
    }


    private List<user> cursorToList(Cursor cursor) {
        List<user> items = new ArrayList();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                user nam = new user();
                nam.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                nam.setUid(cursor.getInt(cursor.getColumnIndex(KEY_UID)));
                nam.setFname(cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
                nam.setLname(cursor.getString(cursor.getColumnIndex(KEY_LNAME)));
                nam.setUsername(cursor.getString(cursor.getColumnIndex(KEY_USERNAME)));
                nam.setPic(cursor.getString(cursor.getColumnIndex(KEY_PIC)));
                nam.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                nam.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
                nam.setUptime(cursor.getString(cursor.getColumnIndex(KEY_UPTIME)));
                nam.setIsupdate(cursor.getInt(cursor.getColumnIndex(KEY_ISUPDATE)));
                nam.setIsspecific(cursor.getInt(cursor.getColumnIndex(KEY_ISSPECEFIC)));
                nam.setPicup(cursor.getInt(cursor.getColumnIndex(KEY_PICUP)));
                nam.setStatusup(cursor.getInt(cursor.getColumnIndex(KEY_STATUSUP)));
                nam.setPhoneup(cursor.getInt(cursor.getColumnIndex(KEY_PHONEUP)));
                nam.setIsonetime(cursor.getInt(cursor.getColumnIndex(KEY_ISONETIME)));


                items.add(nam);
            }
        }
        return items;
    }


    public boolean Exists(int uid) {


        Cursor cursor = db.query(DATABASE_MAINTABLE, this.yek_SH_flashkart, "uid == '" + uid + "' ", null, null, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public user getItm(int uid) throws SQLException {
        Cursor cursor = this.db.query(true, DATABASE_MAINTABLE, this.yek_SH_flashkart, "uid == '" + uid + "' ", null, null, null, null, null);
        user nam = new user();
        if (cursor != null) {
            cursor.moveToFirst();
            nam.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            nam.setUid(cursor.getInt(cursor.getColumnIndex(KEY_UID)));
            nam.setFname(cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
            nam.setLname(cursor.getString(cursor.getColumnIndex(KEY_LNAME)));
            nam.setUsername(cursor.getString(cursor.getColumnIndex(KEY_USERNAME)));
            nam.setPic(cursor.getString(cursor.getColumnIndex(KEY_PIC)));
            nam.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
            nam.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            nam.setUptime(cursor.getString(cursor.getColumnIndex(KEY_UPTIME)));
            nam.setIsupdate(cursor.getInt(cursor.getColumnIndex(KEY_ISUPDATE)));
            nam.setIsspecific(cursor.getInt(cursor.getColumnIndex(KEY_ISSPECEFIC)));
            nam.setPicup(cursor.getInt(cursor.getColumnIndex(KEY_PICUP)));
            nam.setStatusup(cursor.getInt(cursor.getColumnIndex(KEY_STATUSUP)));
            nam.setPhoneup(cursor.getInt(cursor.getColumnIndex(KEY_PHONEUP)));
            nam.setIsonetime(cursor.getInt(cursor.getColumnIndex(KEY_ISONETIME)));

        }
        assert cursor != null;
        cursor.close();
        return nam;
    }

    public boolean updateIsUpdate(user thisFlash) {
        ContentValues args = new ContentValues();
        args.put(KEY_ISUPDATE, thisFlash.getIsupdate());
        return this.db.update(DATABASE_MAINTABLE, args, "uid=" + thisFlash.getUid(), null) > 0;
    }


    public boolean updateIsSpecific(int uid ,int value) {
        ContentValues args = new ContentValues();
        args.put(KEY_ISSPECEFIC, value);
        return this.db.update(DATABASE_MAINTABLE, args, "uid=" + uid, null) > 0;
    }


    public boolean updatePicUp(int uid ,int value) {
        ContentValues args = new ContentValues();
        args.put(KEY_PICUP, value);
        return this.db.update(DATABASE_MAINTABLE, args, "uid=" + uid, null) > 0;
    }


    public boolean updateStatusUp(int uid ,int value) {
        ContentValues args = new ContentValues();
        args.put(KEY_STATUSUP, value);
        return this.db.update(DATABASE_MAINTABLE, args, "uid=" + uid, null) > 0;
    }


    public boolean updatePhoneUp(int uid ,int value) {
        ContentValues args = new ContentValues();
        args.put(KEY_PHONEUP, value);
        return this.db.update(DATABASE_MAINTABLE, args, "uid=" + uid, null) > 0;
    }

    public boolean updateIsOneTime(int uid ,int value) {
        ContentValues args = new ContentValues();
        args.put(KEY_ISONETIME, value);
        return this.db.update(DATABASE_MAINTABLE, args, "uid=" + uid, null) > 0;
    }

    public boolean updatePhoto(int uid ,String value) {
        ContentValues args = new ContentValues();
        args.put(KEY_PIC, value);
        return this.db.update(DATABASE_MAINTABLE, args, "uid=" + uid, null) > 0;
    }

    public boolean updatePhone(int uid ,String value) {
        ContentValues args = new ContentValues();
        args.put(KEY_PHONE, value);
        return this.db.update(DATABASE_MAINTABLE, args, "uid=" + uid, null) > 0;
    }

    public boolean updateStatus(int uid ,String value) {
        ContentValues args = new ContentValues();
        args.put(KEY_STATUS, value);
        return this.db.update(DATABASE_MAINTABLE, args, "uid=" + uid, null) > 0;
    }

    public boolean updateUsername(int uid ,String value) {
        ContentValues args = new ContentValues();
        args.put(KEY_USERNAME, value);
        return this.db.update(DATABASE_MAINTABLE, args, "uid=" + uid, null) > 0;
    }

    public void insert(user user) {
        //Open connection to write data
        ContentValues values = new ContentValues();
        values.put(KEY_UID , user.getUid());
        values.put(KEY_FNAME, user.getFname());
        values.put(KEY_LNAME, user.getLname());
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PIC, user.getPic());
        values.put(KEY_STATUS, user.getStatus());
        values.put(KEY_PHONE, user.getPhone());

        // Inserting Row
        db.insert(DATABASE_MAINTABLE, null, values);

    }


    public void update(user user) {
        //Open connection to write data
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PIC, user.getPic());
        values.put(KEY_STATUS, user.getStatus());
        values.put(KEY_PHONE, user.getPhone());

        this.db.update(DATABASE_MAINTABLE, values, "uid=" + user.getUid(), null) ;

    }

    public void delete(int UID) {

        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(DATABASE_MAINTABLE, KEY_UID + "= ?", new String[]{String.valueOf(UID)});
        db.close(); // Closing database connection
    }

}
