package anargram.users.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class changesDBAdapter {

    private static final String CREATE_MAINTABLE_CHANGES = "CREATE TABLE \"changes\" (\"id\" INTEGER PRIMARY KEY  NOT NULL  UNIQUE , \"uid\" INTEGER NOT NULL , \"type\" INTEGER NOT NULL  DEFAULT 0 , \"time\" DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP)";

    private static final String DATABASE_MAINTABLE = "changes";
    private static final String DATABASE_NAME = "AnarGram6";
    private static final int DATABASE_VERSION = 1;


    private static final String KEY_ID = "id";
    private static final String KEY_UID = "uid";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TIME = "time";




    private static final String TAG = "AnarGram_USER_TABLE_CHANGES";
    private final changesDBAdapter.DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private final String[] yek_SH_flashkart;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, changesDBAdapter.DATABASE_NAME, null, changesDBAdapter.DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(changesDBAdapter.CREATE_MAINTABLE_CHANGES);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS main");
            onCreate(db);
        }
    }

    public changesDBAdapter(Context ctx) {
        this.yek_SH_flashkart = new String[]{KEY_ID,KEY_UID,KEY_TYPE,KEY_TIME};
        this.DBHelper = new changesDBAdapter.DatabaseHelper(ctx);
    }

    public void open() throws SQLException {
        this.db = this.DBHelper.getWritableDatabase();
    }

    public void close() {
        this.DBHelper.close();
    }

    public List<Change> getAllItms(int type) {
        return cursorToList(this.db.query(true, DATABASE_MAINTABLE, this.yek_SH_flashkart, "type == '" + type + "'", null, null, null, " id desc", null));
    }

    public List<Change> getAllItms() {
        return cursorToList(this.db.query(DATABASE_MAINTABLE, this.yek_SH_flashkart, null, null, null, null, " id desc"));
    }


    public int getsize() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM changes;", null);
        boolean isOk = cursor.moveToFirst();
        return cursor.getInt(0);
    }


    private List<Change> cursorToList(Cursor cursor) {
        List<Change> items = new ArrayList();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Change nam = new Change();
                nam.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                nam.setUid(cursor.getInt(cursor.getColumnIndex(KEY_UID)));
                nam.setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE)));
                nam.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME)));



                items.add(nam);
            }
        }
        return items;
    }

    public Change getItm(int ID) throws SQLException {
        Cursor cursor = this.db.query(true, DATABASE_MAINTABLE, this.yek_SH_flashkart, "id == '" + ID + "' ", null, null, null, null, null);
        Change nam = new Change();
        if (cursor != null) {
            cursor.moveToFirst();
            nam.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            nam.setUid(cursor.getInt(cursor.getColumnIndex(KEY_UID)));
            nam.setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE)));
            nam.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME)));
        }
        assert cursor != null;
        cursor.close();
        return nam;
    }





    public void insert(Change change) {
        //Open connection to write data
        ContentValues values = new ContentValues();
        values.put(KEY_UID , change.getUid());
        values.put(KEY_TYPE, change.getType());
//        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
//        values.put(KEY_TIME, currentDateTimeString);

        // Inserting Row
        db.insert(DATABASE_MAINTABLE, null, values);

    }

    public void delete(int UID) {

        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(DATABASE_MAINTABLE, KEY_UID + "= ?", new String[]{String.valueOf(UID)});
        db.close(); // Closing database connection
    }

    public void deleteAll() {

        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(DATABASE_MAINTABLE, null, null);
        db.close(); // Closing database connection
    }

}
