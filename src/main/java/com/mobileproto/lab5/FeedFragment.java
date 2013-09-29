package com.mobileproto.lab5;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.feed_fragment, null);
        feedList = (ListView)v.findViewById(R.id.feedList);
        new FeedFetchAsyncTask(this.getActivity(), v).execute("http://twitterproto.herokuapp.com/tweets");
        return v;

    }

    private class FeedFetchAsyncTask extends AsyncTask<String, Void, JSONObject> {

        private Activity activity;
        private View v;
        private String username;
        private String parameter;

        public FeedFetchAsyncTask(Activity activity, View v)
        {
            this.activity = activity;
            this.v = v;
        }

        protected JSONObject doInBackground(String...urls){
            try {
                return fetchAllTweetsData(urls[0]);
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(JSONObject data){
            if(data!=null) {
                try {
                    if(feedListAdapter != null)
                    {
                        feedListAdapter.addAll(parseJSONData(data));
                        feedListAdapter.sort(new Comparator<FeedItem>() {
                            public int compare(FeedItem o1, FeedItem o2) {
                                return o1.date.compareTo(o2.date);
                            }
                        });
                        feedListAdapter.notifyDataSetChanged();
                    } else {
                        feedItems.addAll(parseJSONData(data));
                        feedListAdapter = new FeedListAdapter(activity, feedItems);
                        feedList.setAdapter(feedListAdapter);
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
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

        private JSONObject fetchAllTweetsData(String url) {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(url);
            try {
                HttpResponse response = client.execute(getRequest);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        builder.append(line);
                    }
                } else {
                    Log.e(FeedFragment.class.toString(), "Failed to download JSON");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return new JSONObject(builder.toString());
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing" + e.toString());
            }
            return null;

        }
    }
}
