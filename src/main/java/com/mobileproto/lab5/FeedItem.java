package com.mobileproto.lab5;

import org.joda.time.DateTime;

/**
 * Created by evan on 9/25/13.
 */
public class FeedItem {

    public String text;
    public String userName;
    public DateTime date;

    public FeedItem(String userName, String text, String dateString){
        this.userName = userName;
        this.text = text;
        this.date = new DateTime(Long.parseLong(dateString));
    }

}
