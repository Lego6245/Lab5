package com.mobileproto.lab5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by lego6245 on 10/8/13.
 */
public class TweetDetailActivity extends Activity {

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        Intent intent = getIntent();

        String userName = intent.getStringExtra("username");
        String text = intent.getStringExtra("text");
        String date = intent.getStringExtra("date");

        TextView userNameV = (TextView)findViewById(R.id.feedItemUserD);
        TextView textV = (TextView)findViewById(R.id.feedTextD);
        TextView dateV = (TextView)findViewById(R.id.feedItemDateD);

        userNameV.setText(userName);
        textV.setText(text);
        dateV.setText(date);

    }
}