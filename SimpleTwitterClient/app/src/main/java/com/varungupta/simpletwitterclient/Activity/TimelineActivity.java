package com.varungupta.simpletwitterclient.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.varungupta.simpletwitterclient.Adapter.TweetsAdapter;
import com.varungupta.simpletwitterclient.Model.Tweet;
import com.varungupta.simpletwitterclient.R;
import com.varungupta.simpletwitterclient.RestClient.TwitterClient;
import com.varungupta.simpletwitterclient.TwitterApplication;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    TwitterClient twitterClient;
    ArrayList<Tweet> tweets;
    TweetsAdapter tweetsAdapter;
    boolean loading;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);

        twitterClient = TwitterApplication.getTwitterClient();

        tweets = new ArrayList<Tweet>();
        tweetsAdapter = new TweetsAdapter(this, tweets);

        ListView lvTimeline = (ListView) findViewById(R.id.lvTimeline);
        lvTimeline.setAdapter(tweetsAdapter);

        getTweets(0);

        // Add infinite scroll
        lvTimeline.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!loading) {
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
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTweets(0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_compose) {
            Intent intent = new Intent(this, ComposeActivity.class);
            startActivityForResult(intent, 20);

            overridePendingTransition(R.layout.enter_from_bottom, R.layout.stay_in_place);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getTweets(final long max_id) {
        loading = true;
        twitterClient.getHomeTimeline(max_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                Log.i("tweets", response.toString());
                if (max_id == 0) {
                    tweetsAdapter.clear();
                }

                tweetsAdapter.addAll(Tweet.fromJson(response));
                loading = false;
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("Tweets", responseString);
                Toast.makeText(getBaseContext(), "Failed to get tweets", Toast.LENGTH_SHORT).show();
                loading = false;
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("Tweets", errorResponse.toString());
                Toast.makeText(getBaseContext(), "Failed to get tweets", Toast.LENGTH_SHORT).show();
                loading = false;
                swipeContainer.setRefreshing(false);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 20) {
            if(resultCode == RESULT_OK){
                Tweet tweet = (Tweet)data.getSerializableExtra("tweet");
                tweets.add(0, tweet);
                tweetsAdapter.notifyDataSetChanged();
            }
        }
    }
}
