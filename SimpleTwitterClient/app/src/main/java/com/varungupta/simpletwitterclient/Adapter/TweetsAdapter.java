package com.varungupta.simpletwitterclient.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.varungupta.simpletwitterclient.Model.Tweet;
import com.varungupta.simpletwitterclient.R;
import com.varungupta.simpletwitterclient.TwitterApplication;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by varungupta on 5/23/15.
 */
public class TweetsAdapter extends ArrayAdapter<Tweet> {
    public interface TweetsAdapterListener {
        void onReplyClicked(String usersInfo, long in_reply_to_status_id);
        void onProfileClicked(long user_id);
    }

    TweetsAdapterListener listener;
    public TweetsAdapter(TweetsAdapterListener listener, Context context, List<Tweet> objects) {
        super(context, R.layout.timeline_item, objects);
        this.listener = listener;
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
            viewHolder.iv_timeline_item_retweeted_icon = (ImageView) convertView.findViewById(R.id.iv_timeline_item_retweeted_icon);
            viewHolder.tv_timeline_item_retweeted = (TextView) convertView.findViewById(R.id.tv_timeline_item_retweeted);
            viewHolder.iv_timeline_item_embedded_photo = (ImageView) convertView.findViewById(R.id.iv_timeline_item_embedded_photo);
            viewHolder.tv_timeline_item_reply = (TextView) convertView.findViewById(R.id.tv_timeline_item_reply);
            viewHolder.tv_timeline_item_retweet = (TextView) convertView.findViewById(R.id.tv_timeline_item_retweet);
            viewHolder.tv_timeline_item_favorite = (TextView) convertView.findViewById(R.id.tv_timeline_item_favorite);
            viewHolder.tv_timeline_item_add_friend = (TextView) convertView.findViewById(R.id.tv_timeline_item_add_friend);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (TimelineItemViewHolder)convertView.getTag();
        }

        final TimelineItemViewHolder finalViewHolder = viewHolder;
        // Load profile photo
        // ivProfilePhoto.setImageResource(0) will clear the last image in case convertView is reused
        viewHolder.iv_timeline_item_icon.setImageResource(0);
        Picasso.with(getContext()).load(tweet.user_profile_image_url).into(viewHolder.iv_timeline_item_icon);
        viewHolder.iv_timeline_item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProfileClicked(tweet.user_id);
            }
        });

        viewHolder.tv_timeline_item_username.setText(tweet.user_name);
        viewHolder.tv_timeline_item_user_screen_name.setText(tweet.user_screen_name);
        viewHolder.tv_timeline_item_timestamp.setText(getRelativeTime(tweet.created_at));
        viewHolder.tv_timeline_item_text.setText(tweet.text);

        viewHolder.iv_timeline_item_embedded_photo.setImageResource(0);
        if (tweet.embedded_photo_url != null) {
            viewHolder.iv_timeline_item_embedded_photo.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(tweet.embedded_photo_url).into(viewHolder.iv_timeline_item_embedded_photo);
        }
        else {
            viewHolder.iv_timeline_item_embedded_photo.setVisibility(View.GONE);
        }

        if (tweet.retweet_user != null) {
            viewHolder.iv_timeline_item_retweeted_icon.setVisibility(View.VISIBLE);
            viewHolder.tv_timeline_item_retweeted.setVisibility(View.VISIBLE);
            viewHolder.tv_timeline_item_retweeted.setText(tweet.retweet_user + " retweeted");
        }
        else {
            viewHolder.iv_timeline_item_retweeted_icon.setVisibility(View.GONE);
            viewHolder.tv_timeline_item_retweeted.setVisibility(View.GONE);
        }

        viewHolder.tv_timeline_item_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tweet.user_screen_name + " ";
                if (tweet.retweet_user_screen_name != null) {
                    text += tweet.retweet_user_screen_name + " ";
                }
                listener.onReplyClicked(text, tweet.id);
            }
        });

        viewHolder.tv_timeline_item_retweet.setText(String.valueOf(tweet.retweet_count));
        if (tweet.retweeted) {
            viewHolder.tv_timeline_item_retweet.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.retweeted, 0, 0, 0);
            viewHolder.tv_timeline_item_retweet.setTextColor(Color.parseColor("#009900"));
        }
        else {
            viewHolder.tv_timeline_item_retweet.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.retweet, 0, 0, 0);
            viewHolder.tv_timeline_item_retweet.setTextColor(Color.parseColor("#64000000"));
        }

        viewHolder.tv_timeline_item_retweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tweet.retweeted) {
                    // retweet here
                    TwitterApplication.getTwitterClient().reTweet(tweet.id_str, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            finalViewHolder.tv_timeline_item_retweet.setText(String.valueOf(tweet.retweet_count + 1));
                            finalViewHolder.tv_timeline_item_retweet.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.retweeted, 0, 0, 0);
                            finalViewHolder.tv_timeline_item_retweet.setTextColor(Color.parseColor("#009900"));
                        }
                    });
                }
            }
        });

        viewHolder.tv_timeline_item_favorite.setText(String.valueOf(tweet.favourite_count));
        if (tweet.favorited) {
            viewHolder.tv_timeline_item_favorite.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.favotited, 0, 0, 0);
            viewHolder.tv_timeline_item_favorite.setTextColor(Color.parseColor("#FFAA00"));
        }
        else {
            viewHolder.tv_timeline_item_favorite.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.favorite, 0, 0, 0);
            viewHolder.tv_timeline_item_favorite.setTextColor(Color.parseColor("#64000000"));
        }
        viewHolder.tv_timeline_item_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tweet.favorited) {
                    TwitterApplication.getTwitterClient().addTofavorite(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            finalViewHolder.tv_timeline_item_favorite.setText(String.valueOf(tweet.favourite_count + 1));
                            finalViewHolder.tv_timeline_item_favorite.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.favotited, 0, 0, 0);
                            finalViewHolder.tv_timeline_item_favorite.setTextColor(Color.parseColor("#FFAA00"));
                        }
                    });
                }
            }
        });

        if (tweet.user_following) {
            viewHolder.tv_timeline_item_add_friend.setVisibility(View.GONE);
            // viewHolder.tv_timeline_item_add_friend.setCompoundDrawablesWithIntrinsicBounds(
            //         R.drawable.following, 0, 0, 0);
            // viewHolder.tv_timeline_item_add_friend.setTextColor(Color.parseColor("#55ACEE"));
        }
        else {
            viewHolder.tv_timeline_item_add_friend.setVisibility(View.VISIBLE);
            viewHolder.tv_timeline_item_add_friend.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.follow, 0, 0, 0);
            viewHolder.tv_timeline_item_add_friend.setTextColor(Color.parseColor("#FFAA00"));
        }
        viewHolder.tv_timeline_item_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tweet.user_following) {
                    // Start following user here
                    TwitterApplication.getTwitterClient().followUser(tweet.user_id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            finalViewHolder.tv_timeline_item_add_friend.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.following, 0, 0, 0);
                            finalViewHolder.tv_timeline_item_add_friend.setTextColor(Color.parseColor("#55ACEE"));
                        }
                    });
                }
            }
        });



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
