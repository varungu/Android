package com.varungupta.simpletwitterclient.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.varungupta.simpletwitterclient.Model.User;
import com.varungupta.simpletwitterclient.R;

import java.text.DecimalFormat;

/**
 * Created by varungupta on 5/30/15.
 */
public class ProfileViewFragment extends TweetsListFragment {

    public User user;

    public static ProfileViewFragment GetInstance(User user, ITweetsGetter tweetsGetter, ITweetsListFragmentListener listener){
        // Create new fragment
        ProfileViewFragment fragment = new ProfileViewFragment();

        // Set variables
        fragment.user = user;

        // Set listeners
        fragment.tweetsGetter = tweetsGetter;
        fragment.listener = listener;

        // return
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Add header
        View headerView = View.inflate(getActivity(), R.layout.profile_header, null);
        ListView lvTimeline = (ListView) view.findViewById(R.id.lvTimeline);
        lvTimeline.addHeaderView(headerView);

        ImageView banner = (ImageView) headerView.findViewById(R.id.ivBanner);
        banner.setImageResource(0);
        Picasso.with(getActivity()).load(user.profile_banner_url).placeholder(R.drawable.default_background).into(banner);

        ImageView ivProfileImage = (ImageView) headerView.findViewById(R.id.ivImage);
        banner.setImageResource(0);
        Picasso.with(getActivity()).load(user.profile_image_url).placeholder(R.drawable.following).into(ivProfileImage);

        TextView tv_user_name = (TextView) headerView.findViewById(R.id.tv_user_name);
        tv_user_name.setText(user.name);

        if (!user.verified){
            ImageView iv_user_verified = (ImageView) headerView.findViewById(R.id.iv_user_verified);
            iv_user_verified.setVisibility(View.GONE);
        }

        TextView tv_screen_name = (TextView) headerView.findViewById(R.id.tv_screen_name);
        tv_screen_name.setText(user.screen_name);

        TextView tv_user_description = (TextView) headerView.findViewById(R.id.tv_user_description);
        tv_user_description.setText(user.description);

        TextView tv_user_url = (TextView) headerView.findViewById(R.id.tv_user_url);
        tv_user_url.setText(user.url);

        TextView tv_user_following = (TextView) headerView.findViewById(R.id.tv_user_following);
        tv_user_following.setText(Html.fromHtml(String.format("%s <font color=\"#999999\">FOLLOWING</font>", formatNumber(user.friends_count))));

        TextView tv_user_followers = (TextView) headerView.findViewById(R.id.tv_user_followers);
        tv_user_followers.setText(Html.fromHtml(String.format("%s <font color=\"#999999\">FOLLOWERS</font>", formatNumber(user.followers_count))));

        return view;
    }

    private String formatNumber(double number) {
        if (number < 1000){
            return String.valueOf(number);
        }
        else if (number < 1000000){
            DecimalFormat df = new DecimalFormat("#,###,##0.#");
            return df.format(number/1000) + "K";
        }
        else{
            DecimalFormat df = new DecimalFormat("#,###,##0.#");
            return df.format(number/1000000) + "M";
        }
    }
}
