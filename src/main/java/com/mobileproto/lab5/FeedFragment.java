package com.mobileproto.lab5;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.preference.PreferenceManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by evan on 9/25/13.
 */
public class FeedFragment extends Fragment {

    List<FeedItem> feedItems = new ArrayList<FeedItem>();
    FeedListAdapter feedListAdapter;
    ListView feedList;
    SharedPreferences prefs;
    FeedDatabaseSource datasource;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.feed_fragment, null);
        try {
            datasource = new FeedDatabaseSource(getActivity());
            datasource.open();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        feedList = (ListView)v.findViewById(R.id.feedList);
        if(feedListAdapter == null)
        {
            feedListAdapter = new FeedListAdapter(getActivity(), feedItems);
        }
        feedList.setAdapter(feedListAdapter);
        List<FeedItem> databaseContents = datasource.getAllTweets();
        for(FeedItem d : databaseContents)
        {
            if(!feedListAdapter.contains(d))
            {
               Log.println(Log.INFO, "database", "SKIPPPUUUUUUUUU");
                feedListAdapter.add(d);
            }
        }
        feedListAdapter.sort(new Comparator<FeedItem>() {
            public int compare(FeedItem o1, FeedItem o2) {
                return o2.date.compareTo(o1.date);
            }
        });
        feedListAdapter.notifyDataSetChanged();
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
                                Log.println(Log.INFO, "doubleadd", "youreaddingagainmaybe");
                                feedListAdapter.add(p);
                                datasource.writeTweet(p);
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
        }.execute("http://twitterproto.herokuapp.com/tweets");
        return v;

    }

//    public void onResume(){
//        super.onResume();
//        // Make the adapter again
//        feedListAdapter = new FeedListAdapter(getActivity(), feedItems);
//        // Set it to the list again
//        feedList.setAdapter(feedListAdapter);
//    }

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
}
