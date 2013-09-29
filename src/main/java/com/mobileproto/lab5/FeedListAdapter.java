package com.mobileproto.lab5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by evan on 9/25/13.
 */
public class FeedListAdapter extends ArrayAdapter<FeedItem> {

    private final Context context;
    private final List<FeedItem> data;

    public FeedListAdapter(Context context,  List<FeedItem> data){
        super(context, R.layout.feed_item, data);
        this.context = context;
        this.data = data;
    }


    private class FeedItemHolder{

        TextView userName;
        TextView text;
        TextView date;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        FeedItemHolder holder;
        View feedRow = convertView;

        if(feedRow == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            feedRow = inflater.inflate(R.layout.feed_item, parent, false);
            holder = new FeedItemHolder();
            holder.userName = (TextView) feedRow.findViewById(R.id.feedItemUser);
            holder.text = (TextView) feedRow.findViewById(R.id.feedText);
            holder.date = (TextView) feedRow.findViewById(R.id.feedItemDate);

            feedRow.setTag(holder);
        } else {
            holder = (FeedItemHolder) feedRow.getTag();
        }

        FeedItem item = data.get(position);

        holder.userName.setText(item.userName);
        holder.text.setText(item.text);
        DateTime dNow = new DateTime();
        Period period = new Period(item.date, dNow);
        int seconds = period.getSeconds();
        int minutes = period.getMinutes();
        int hours = period.getHours();
        int days = Days.daysBetween(item.date, dNow).getDays();
        if(days == 0) {
            if(hours == 0) {
                if(minutes == 0) {
                    holder.date.setText(seconds + " seconds ago");
                } else {
                    holder.date.setText(minutes + " minutes ago");
                }
            } else {
                holder.date.setText(hours + " hours ago");
            }
        } else {
            holder.date.setText(days + " days ago");
        }

        return feedRow;
    }

}
