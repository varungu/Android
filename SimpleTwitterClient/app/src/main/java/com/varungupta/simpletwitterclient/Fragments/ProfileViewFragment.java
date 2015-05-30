package com.varungupta.simpletwitterclient.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;
import com.varungupta.simpletwitterclient.Model.User;
import com.varungupta.simpletwitterclient.R;

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

        return view;
    }
}
