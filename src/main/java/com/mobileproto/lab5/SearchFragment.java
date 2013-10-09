package com.mobileproto.lab5;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by evan on 9/26/13.
 */
public class SearchFragment extends Fragment {

    List<FeedItem> feedItems = new ArrayList<FeedItem>();
    FeedListAdapter feedListAdapter;
    ListView feedList;
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.search_fragment, null);
        feedList = (ListView)v.findViewById(R.id.searchResults);
        if(feedListAdapter == null)
        {
            feedListAdapter = new FeedListAdapter(getActivity(), feedItems);
        }
        feedList.setAdapter(feedListAdapter);
        Button submit = (Button)v.findViewById(R.id.searchButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchField = (EditText)((View)view.getParent()).findViewById(R.id.searchField);
                String fieldContents = searchField.getText().toString();
                new APIFetchAsyncTask() {
                    @Override
                    protected void onPostExecute(JSONObject data){
                        if(data!=null) {
                            try {
                                List<FeedItem> parsedJson = parseJSONData(data);
                                for(FeedItem p : parsedJson)
                                {
                                    if(feedListAdapter.getPosition(p) == -1)
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
                }.execute("http://twitterproto.herokuapp.com/tweets?q=" + fieldContents);
            }
        });
        return v;
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

}
