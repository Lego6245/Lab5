package com.mobileproto.lab5;

import org.joda.time.DateTime;

/**
 * Created by evan on 9/25/13.
 */
public class FeedNotification {

    String userFrom;
    String userTo;
    String text;
    String type;
    DateTime date;

    public FeedNotification(String userFrom, String userTo, String text, String type, long dateTime){
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.text = text;
        this.type = type;
        date = new DateTime(dateTime);
    }
}
