package com.mobileproto.lab5;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lego6245 on 10/9/13.
 */
public class ProfileActivity extends Activity {
    List<FeedItem> feedItems = new ArrayList<FeedItem>();
    FeedListAdapter feedListAdapter;
    ListView feedList;
    SharedPreferences prefs;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        final String profileUser = intent.getStringExtra("username");
        this.setTitle(profileUser);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String yourUser = prefs.getString("username", "lego6245");
        Button followButton = (Button) findViewById(R.id.followButton);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new APIPostAsyncTask().execute(profileUser, yourUser);
            }
        });
        feedList = (ListView)findViewById(R.id.profileList);
        if(feedListAdapter == null)
        {
            feedListAdapter = new FeedListAdapter(this, feedItems);
        }
        feedList.setAdapter(feedListAdapter);
        new APIFetchAsyncTask() {
            @Override
            protected void onPostExecute(JSONObject data){
                if(data!=null) {
                    try {
                        List<FeedItem> parsedJson = parseJSONData(data);
                        for(FeedItem p : parsedJson)
                        {
                            if(!feedListAdapter.contains(p))
                            {
                                feedListAdapter.add(p);
                            }
                        }
                        feedListAdapter.sort(new Comparator<FeedItem>() {
                            public int compare(FeedItem o1, FeedItem o2) {
                                return o2.date.compareTo(o1.date);
                            }
                        });
                        feedListAdapter.notifyDataSetChanged();

                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute("http://twitterproto.herokuapp.com/" + profileUser + "/tweets");
    }

    private List<FeedItem> parseJSONData(JSONObject feedData) {
        List<FeedItem> feedItems = new ArrayList<FeedItem>();
        try {
            JSONArray tweets = feedData.getJSONArray("tweets");
            for(int i = 0; i < tweets.length(); i++)
            {
                JSONObject obj = tweets.getJSONObject(i);

                feedItems.add(new FeedItem(obj.getString("username"), obj.getString("tweet"), obj.getString("date")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return feedItems;
    }

    private class APIPostAsyncTask extends AsyncTask<String, Void, HttpResponse> {

        protected HttpResponse doInBackground(String... metaData) {
            if(metaData.length == 2 && metaData[0] != null && metaData[1] != null)
            {
                // Create a new HttpClient and Post Header
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://twitterproto.herokuapp.com/" + metaData[1] + "/follow");

                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("username", metaData[0]));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
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
