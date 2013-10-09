package com.mobileproto.lab5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lego6245 on 10/8/13.
 */
public class FeedDatabaseSource {

    private SQLiteDatabase database;
    private FeedDatabaseHelper dbHelper;
    private String[] tweetCols = { FeedDatabaseHelper.COLUMN_ID,
            FeedDatabaseHelper.COLUMN_USER, FeedDatabaseHelper.COLUMN_TEXT,
            FeedDatabaseHelper.COLUMN_DATE };
    private String[] notificationCols = { FeedDatabaseHelper.COLUMN_ID,
            FeedDatabaseHelper.COLUMN_USERFROM, FeedDatabaseHelper.COLUMN_USERTO,
            FeedDatabaseHelper.COLUMN_TEXT, FeedDatabaseHelper.COLUMN_TYPE,
            FeedDatabaseHelper.COLUMN_DATE};

    public FeedDatabaseSource(Context context) {
        dbHelper = new FeedDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void writeTweet(FeedItem f)
    {
            ContentValues values = new ContentValues();
            values.put(FeedDatabaseHelper.COLUMN_TEXT, f.text);
            values.put(FeedDatabaseHelper.COLUMN_DATE, f.date.getMillis());
            values.put(FeedDatabaseHelper.COLUMN_USER, f.userName);
            long insertID = database.insert(FeedDatabaseHelper.TABLE_TWEETS, null, values);
    }
    public void writeTweets(List<FeedItem> tweets)
    {
        for(FeedItem f : tweets)
        {
            ContentValues values = new ContentValues();
            values.put(FeedDatabaseHelper.COLUMN_TEXT, f.text);
            values.put(FeedDatabaseHelper.COLUMN_DATE, f.date.getMillis());
            values.put(FeedDatabaseHelper.COLUMN_USER, f.userName);
            long insertID = database.insert(FeedDatabaseHelper.TABLE_TWEETS, null, values);
        }
    }

    public void writeNotification(FeedNotification f)
    {
            ContentValues values = new ContentValues();
            values.put(FeedDatabaseHelper.COLUMN_DATE, f.date.getMillis());
            values.put(FeedDatabaseHelper.COLUMN_TEXT, f.text);
            values.put(FeedDatabaseHelper.COLUMN_TYPE, f.type);
            values.put(FeedDatabaseHelper.COLUMN_USERTO, f.userTo);
            values.put(FeedDatabaseHelper.COLUMN_USERFROM, f.userFrom);
            long insertID = database.insert(FeedDatabaseHelper.TABLE_NOTIFICATIONS, null, values);
    }

    public void writeNotifications(List<FeedNotification> notifications)
    {
        for(FeedNotification f : notifications)
        {
            ContentValues values = new ContentValues();
            values.put(FeedDatabaseHelper.COLUMN_DATE, f.date.getMillis());
            values.put(FeedDatabaseHelper.COLUMN_TEXT, f.text);
            values.put(FeedDatabaseHelper.COLUMN_TYPE, f.type);
            values.put(FeedDatabaseHelper.COLUMN_USERTO, f.userTo);
            values.put(FeedDatabaseHelper.COLUMN_USERFROM, f.userFrom);
            long insertID = database.insert(FeedDatabaseHelper.TABLE_NOTIFICATIONS, null, values);
        }
    }

    public List<FeedItem> getAllTweets() {
        List<FeedItem> comments = new ArrayList<FeedItem>();

        Cursor cursor = database.query(FeedDatabaseHelper.TABLE_TWEETS,
                tweetCols, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            FeedItem comment = cursorToTweet(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return comments;
    }

    public List<FeedNotification> getAllNotifications() {
        List<FeedNotification> comments = new ArrayList<FeedNotification>();

        Cursor cursor = database.query(FeedDatabaseHelper.TABLE_NOTIFICATIONS,
                notificationCols, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            FeedNotification comment = cursorToNotification(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return comments;
    }

    private FeedItem cursorToTweet(Cursor cursor) {
        FeedItem comment = new FeedItem(cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return comment;
    }

    private FeedNotification cursorToNotification(Cursor cursor) {
        FeedNotification comment = new FeedNotification(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getLong(5));
        return comment;
    }
}
