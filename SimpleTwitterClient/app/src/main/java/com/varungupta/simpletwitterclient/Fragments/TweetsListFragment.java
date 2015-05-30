package com.varungupta.simpletwitterclient.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.varungupta.simpletwitterclient.Adapter.TweetsAdapter;
import com.varungupta.simpletwitterclient.Model.Tweet;
import com.varungupta.simpletwitterclient.R;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TweetsListFragment extends Fragment implements TweetsAdapter.TweetsAdapterListener{

    // Listener for click on reply
    public interface ITweetsListFragmentListener {
        void onReplyClicked(String usersInfo, long in_reply_to_status_id);
        void onProfileClicked(long user_id);
    }

    // Interface to get tweets
    public interface ITweetsGetter{
        void getTweets(long max_id, AsyncHttpResponseHandler handler);
    }

    // Array list and adapter
    ArrayList<Tweet> tweets;
    TweetsAdapter tweetsAdapter;

    // Swipe container and loading flag for infinite scroll
    SwipeRefreshLayout swipeContainer;
    boolean canGetMoreTweets;

    // Listeners
    public ITweetsListFragmentListener listener;
    public ITweetsGetter tweetsGetter;


    public static TweetsListFragment GetInstance(ITweetsGetter tweetsGetter, ITweetsListFragmentListener listener){
        // Create new fragment
        TweetsListFragment fragment = new TweetsListFragment();

        // Set listeners
        fragment.tweetsGetter = tweetsGetter;
        fragment.listener = listener;

        // return
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        // Set adapter
        ListView lvTimeline = (ListView) view.findViewById(R.id.lvTimeline);
        lvTimeline.setAdapter(tweetsAdapter);


        // Add infinite scroll
        lvTimeline.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (canGetMoreTweets) {
                    if (firstVisibleItem + visibleItemCount + 5 >= totalItemCount) {
                        // User has less than 5 items left to scroll, load more
                        if (tweets.size() > 0) {
                            Tweet lastTweet = tweets.get(tweets.size() - 1);
                            getTweets(lastTweet.id - 1);
                        }
                    }
                }
            }
        });

        // Add swipe to refresh
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTweets(0);
            }
        });

        // Get tweets
        getTweets(0);

        // return view
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup tweets and adapter
        tweets = new ArrayList<Tweet>();
        tweetsAdapter = new TweetsAdapter(this, getActivity(), tweets);
    }

    @Override
    public void onReplyClicked(String usersInfo, long in_reply_to_status_id) {
        listener.onReplyClicked(usersInfo, in_reply_to_status_id);
    }

    @Override
    public void onProfileClicked(long user_id) {
        this.listener.onProfileClicked(user_id);
    }

    public void addAll(List<Tweet> tweets, boolean clearExisting) {
        if (clearExisting) {
            tweetsAdapter.clear();
        }

        tweetsAdapter.addAll(tweets);
    }

    public void add(int position, Tweet tweet){
        tweets.add(position, tweet);
        tweetsAdapter.notifyDataSetChanged();
    }

    private void getTweets(final long max_id) {
        canGetMoreTweets = false;
        tweetsGetter.getTweets(max_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i("tweets", response.toString());
                List<Tweet> tweets = Tweet.fromJson(response);
                addAll(tweets, (max_id == 0));
                // Try to get more tweets only if we got something in this try
                // This is to ensure no infinite loop
                canGetMoreTweets = (tweets.size() > 0);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("tweets error", responseString);
                Toast.makeText(getActivity(), "Failed to get tweets", Toast.LENGTH_SHORT).show();
                canGetMoreTweets = false;
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("tweets error", errorResponse.toString());
                Toast.makeText(getActivity(), "Failed to get tweets", Toast.LENGTH_SHORT).show();
                canGetMoreTweets = false;
                swipeContainer.setRefreshing(false);
            }
        });
    }
}
