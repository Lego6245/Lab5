package com.mobileproto.lab5;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lego6245 on 9/30/13.
 */
public class APIFetchAsyncTask extends AsyncTask<String, Void, JSONObject> {

    protected JSONObject doInBackground(String...urls){
        try {
            return fetchAllTweetsData(urls[0]);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
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
    private List<FeedItem> parseJSONData(JSONObject feedData) {
        List<FeedItem> feedItems = new ArrayList<FeedItem>();
        try {
            if(feedData.has("tweets"))
            {
                JSONArray tweets = feedData.getJSONArray("tweets");
                for(int i = 0; i < tweets.length(); i++)
                {
                    JSONObject obj = tweets.getJSONObject(i);

                    feedItems.add(new FeedItem(obj.getString("username"), obj.getString("tweet"), obj.getString("date")));
                }
            } else if (feedData.has("follows"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return feedItems;
    }
}