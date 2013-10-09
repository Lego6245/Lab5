package com.mobileproto.lab5;

/**
 * Created by evan on 9/25/13.
 */
public class MentionNotification extends FeedNotification {

    String userFrom;
    String userTo;
    String text;

    public MentionNotification(String userFrom, String userTo, String text, long dateTime){
        super(userFrom, userTo, text, "mention", dateTime);
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.text = text;
    }
}
