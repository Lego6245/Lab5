package com.mobileproto.lab5;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by evan on 9/25/13.
 */
public class ConnectionFragment extends Fragment {

    List<FeedNotification> notifications = new ArrayList<FeedNotification>();
    ConnectionListAdapter connectionListAdapter;
    ListView connectionList;
    SharedPreferences prefs;
    private final String BASE_URL = "http://twitterproto.herokuapp.com/";
    FeedDatabaseSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.connections_fragment, null);
        connectionList = (ListView)v.findViewById(R.id.connectionListView);
        try {
            datasource = new FeedDatabaseSource(getActivity());
            datasource.open();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        // Create dummy data for demo
        if(connectionListAdapter == null)
        {
            connectionListAdapter = new ConnectionListAdapter(getActivity(), notifications);
        }
        connectionList.setAdapter(connectionListAdapter);
        List<FeedNotification> databaseContents = datasource.getAllNotifications();
        for(FeedNotification d : databaseContents)
        {
            if(!connectionListAdapter.contains(d))
            {
                Log.println(Log.INFO, "database", "SKIPPPUUUUUUUUU");
                connectionListAdapter.add(d);
            }
        }
        connectionListAdapter.sort(new Comparator<FeedNotification>() {
            public int compare(FeedNotification o1, FeedNotification o2) {
                return o2.date.compareTo(o1.date);
            }
        });
        connectionListAdapter.notifyDataSetChanged();
        new APIFetchAsyncTask() {
            @Override
            protected void onPostExecute(JSONObject data)
            {
                executeThis(data);
            }
        }.execute(BASE_URL + "tweets?q=@" + prefs.getString("username", "lego6245"));
        new APIFetchAsyncTask() {
            @Override
            protected void onPostExecute(JSONObject data)
            {
                executeThis(data);
            }
        }.execute(BASE_URL +  prefs.getString("username", "lego6245") + "/followers");
        Log.println(Log.INFO, "connectionFrag" , "goshdangledarn" + notifications.size() + prefs.getString("username", "lego6245"));
        return v;
    }

    private void executeThis(JSONObject data) {
        if(data!=null) {
            try {
                List<FeedNotification> parsedJson = parseJSONData(data);
                for(FeedNotification p : parsedJson)
                {
                    if(!connectionListAdapter.contains(p))
                    {
                        connectionListAdapter.add(p);
                    }
                }
                connectionListAdapter.sort(new Comparator<FeedNotification>() {
                        public int compare(FeedNotification o1, FeedNotification o2) {
                        return o2.date.compareTo(o1.date);
                }});
                connectionListAdapter.notifyDataSetChanged();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<FeedNotification> parseJSONData(JSONObject feedData) {
        List<FeedNotification> feedItems = new ArrayList<FeedNotification>();
        try {
            if(feedData.has("tweets"))
            {
                JSONArray tweets = feedData.getJSONArray("tweets");
                for(int i = 0; i < tweets.length(); i++)
                {
                    JSONObject obj = tweets.getJSONObject(i);
                    feedItems.add(new MentionNotification("@" + obj.getString("username"), "@" + prefs.getString("username", "lego6245") , obj.getString("tweet"), obj.getLong("date")));
                }
            } else if (feedData.has("followers"))
            {
                JSONArray follows = feedData.getJSONArray("detail");
                for(int i = 0; i < follows.length(); i++)
                {
                    JSONObject obj = follows.getJSONObject(i);
                    feedItems.add(new FollowNotification("@" + obj.getString("username"), "@" + prefs.getString("username", "lego6245") , obj.getLong("date")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return feedItems;
    }
}
