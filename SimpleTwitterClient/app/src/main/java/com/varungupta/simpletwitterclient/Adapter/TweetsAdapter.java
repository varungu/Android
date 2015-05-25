package com.varungupta.simpletwitterclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.varungupta.simpletwitterclient.Model.Tweet;
import com.varungupta.simpletwitterclient.R;

import java.util.Date;
import java.util.List;

/**
 * Created by varungupta on 5/23/15.
 */
public class TweetsAdapter extends ArrayAdapter<Tweet> {
    public TweetsAdapter(Context context, List<Tweet> objects) {
        super(context, R.layout.timeline_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);

        TimelineItemViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.timeline_item, parent, false);

            viewHolder = new TimelineItemViewHolder();
            viewHolder.iv_timeline_item_icon = (ImageView)convertView.findViewById(R.id.iv_timeline_item_icon);
            viewHolder.tv_timeline_item_username = (TextView)convertView.findViewById(R.id.tv_timeline_item_username);
            viewHolder.tv_timeline_item_timestamp = (TextView)convertView.findViewById(R.id.tv_timeline_item_timestamp);
            viewHolder.tv_timeline_item_user_screen_name = (TextView) convertView.findViewById(R.id.tv_timeline_item_user_screen_name);
            viewHolder.tv_timeline_item_text = (TextView) convertView.findViewById(R.id.tv_timeline_item_text);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (TimelineItemViewHolder)convertView.getTag();
        }

        // Load profile photo
        // ivProfilePhoto.setImageResource(0) will clear the last image in case convertView is reused
        viewHolder.iv_timeline_item_icon.setImageResource(0);
        Picasso.with(getContext()).load(tweet.user_profile_image_url).into(viewHolder.iv_timeline_item_icon);

        viewHolder.tv_timeline_item_username.setText(tweet.user_name);
        viewHolder.tv_timeline_item_user_screen_name.setText(tweet.user_screen_name);
        viewHolder.tv_timeline_item_timestamp.setText(getRelativeTime(tweet.created_at));
        viewHolder.tv_timeline_item_text.setText(tweet.text);

        return convertView;
    }

    private String getRelativeTime(long time)
    {
        Date currentTime = new Date();
        long timeDiff = (currentTime.getTime() - time)/1000; // Time difference in seconds

        // Convert seconds to string
        long minute = 60;
        long hour = minute * 60;
        long day = hour * 24;
        long week = day * 7;

        if (timeDiff < minute){
            return String.format("%ds", timeDiff);
        }
        else if (timeDiff < hour){
            return String.format("%dm", timeDiff/minute);
        }
        if (timeDiff < day){
            return String.format("%dh", timeDiff/hour);
        }
        if (timeDiff < week){
            return String.format("%dd", timeDiff/day);
        }
        return String.format("%dw", timeDiff/week);
    }
}
