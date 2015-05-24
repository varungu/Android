package com.varungupta.simpletwitterclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.varungupta.simpletwitterclient.Model.Tweet;
import com.varungupta.simpletwitterclient.R;

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

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (TimelineItemViewHolder)convertView.getTag();
        }

        // Load profile photo
        // ivProfilePhoto.setImageResource(0) will clear the last image in case convertView is reused
        viewHolder.iv_timeline_item_icon.setImageResource(0);
        Picasso.with(getContext()).load(tweet.user_profile_image_url).into(viewHolder.iv_timeline_item_icon);

        return convertView;
    }
}
