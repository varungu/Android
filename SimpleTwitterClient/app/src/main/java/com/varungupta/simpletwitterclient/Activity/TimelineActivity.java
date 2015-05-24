package com.varungupta.simpletwitterclient.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    TwitterClient twitterClient;
    ArrayList<Tweet> tweets;
    TweetsAdapter tweetsAdapter;

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

        getTweets();
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
            startActivity(intent);

            overridePendingTransition(R.layout.enter_from_bottom, R.layout.stay_in_place);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getTweets() {
        twitterClient.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                tweetsAdapter.addAll(Tweet.fromJson(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(), "Failed to get tweets", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
