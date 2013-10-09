package com.mobileproto.lab5;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lego6245 on 10/8/13.
 */
public class NewTweetActivity extends Activity {

    SharedPreferences prefs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_create);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String username = prefs.getString("username", "lego6245");

        final TextView tweetField = (TextView)findViewById(R.id.tweetField);
        Button sendTweet = (Button)findViewById(R.id.tweetSend);
        sendTweet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String tweetText = tweetField.getText().toString();
                new APIPostAsyncTask().execute(tweetText, username);
                finish();
            }
        });
    }

    private class APIPostAsyncTask extends AsyncTask<String, Void, HttpResponse> {

        protected HttpResponse doInBackground(String... metaData) {
            if(metaData.length == 2 && metaData[0] != null && metaData[1] != null)
            {
                // Create a new HttpClient and Post Header
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://twitterproto.herokuapp.com/" + metaData[1] + "/tweets");
                Log.println(Log.INFO, "posttweet", "http://twitterproto.herokuapp.com/" + metaData[1] + "/tweets");

                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("tweet", metaData[0]));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    Log.println(Log.INFO, "posttweet", response.getStatusLine().getStatusCode() + "");
                    return response;

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            }
            return null;
        }
    }
}
