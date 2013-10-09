package com.mobileproto.lab5;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;

import java.util.List;

/**
 * Created by evan on 9/26/13.
 */
public class ConnectionListAdapter extends ArrayAdapter<FeedNotification> {

    private final Context context;
    private final List<FeedNotification> data;

    public ConnectionListAdapter(Context context, List<FeedNotification> data){
        super(context, R.layout.connection_item, data);
        this.context = context;
        this.data = data;
    }

    private class ConnectionItemHolder{
        TextView userName;
        TextView text;
        TextView date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ConnectionItemHolder holder;
        View connectionRow = convertView;

        if(connectionRow == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            connectionRow = inflater.inflate(R.layout.connection_item, parent, false);
            holder = new ConnectionItemHolder();
            holder.userName = (TextView) connectionRow.findViewById(R.id.connectionItemUser);
            holder.text = (TextView) connectionRow.findViewById(R.id.connectionText);
            holder.date = (TextView) connectionRow.findViewById(R.id.connectionItemDate);

            connectionRow.setTag(holder);
        } else {
            holder = (ConnectionItemHolder) connectionRow.getTag();
        }

        FeedNotification item = data.get(position);

        if(item.type.equals("follow")){
            holder.userName.setText(item.text);
            holder.text.setText("");
        }else{
            holder.userName.setText(item.userFrom);
            holder.text.setText(item.text);
        }
        setDate(holder, item);

        return connectionRow;
    }

    public boolean contains(FeedNotification i)
    {
        for(FeedNotification d : data)
        {
            if(d.date.isEqual(i.date) && d.type.equals((i.type)) && d.userFrom.equals((i.userFrom)) && d.userTo.equals(i.userTo))
            {

                return true;
            }
        }
        return false;
    }

    public void setDate(ConnectionItemHolder holder, FeedNotification item) {
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
    }
}
