package com.mobileproto.lab5;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lego6245 on 10/8/13.
 */
public class FeedDatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_TWEETS = "tweets";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER = "userName";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_DATE = "date";

    public static final String TABLE_NOTIFICATIONS = "notifications";
    public static final String COLUMN_USERTO = "userTo";
    public static final String COLUMN_USERFROM = "userFrom";
    public static final String COLUMN_TYPE = "type";

    private static final String DATABASE_NAME = "twitterproto.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_TWEETS = "create table "
            + TABLE_TWEETS + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_USER + " text not null, " + COLUMN_TEXT + " text not null, "
            + COLUMN_DATE + " integer not null);";

    private static final String DATABASE_CREATE_NOTIFICATIONS = "create table "
            + TABLE_NOTIFICATIONS + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_USERTO + " text not null, " + COLUMN_USERFROM + " text not null, "
            + COLUMN_TEXT + " text not null, " + COLUMN_TYPE + " text not null, "
            + COLUMN_DATE + " integer not null);";

    public FeedDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_NOTIFICATIONS);
        database.execSQL(DATABASE_CREATE_TWEETS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(FeedDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TWEETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        onCreate(db);
    }

}
