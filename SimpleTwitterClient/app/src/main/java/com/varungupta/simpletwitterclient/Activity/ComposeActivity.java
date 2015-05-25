package com.varungupta.simpletwitterclient.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.varungupta.simpletwitterclient.Model.Tweet;
import com.varungupta.simpletwitterclient.Model.User;
import com.varungupta.simpletwitterclient.R;
import com.varungupta.simpletwitterclient.RestClient.TwitterClient;
import com.varungupta.simpletwitterclient.TwitterApplication;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeActivity extends ActionBarActivity {

    TwitterClient twitterClient;
    EditText et_compose_tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);

        twitterClient = TwitterApplication.getTwitterClient();
        User user = twitterClient.getAuthenticatedUser();

        ImageView iv_compose_user_image = (ImageView)findViewById(R.id.iv_compose_user_image);
        iv_compose_user_image.setImageResource(0);
        Picasso.with(this).load(user.profile_image_url).into(iv_compose_user_image);

        TextView tv_compose_user_name = (TextView) findViewById(R.id.tv_compose_user_name);
        tv_compose_user_name.setText(user.name);

        TextView tv_compose_user_handle = (TextView) findViewById(R.id.tv_compose_user_handle);
        tv_compose_user_handle.setText(user.screen_name);

        final TextView tv_compose_actionbar_char_count = (TextView)findViewById(R.id.tv_compose_actionbar_char_count);

        et_compose_tweet = (EditText) findViewById(R.id.et_compose_tweet);
        et_compose_tweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_compose_actionbar_char_count.setText(String.valueOf(140 - s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_compose_tweet.setText(getIntent().getExtras().getString("info"));
        et_compose_tweet.setSelection(et_compose_tweet.getText().length());

        ImageView iv_compose_actionbar_cancel = (ImageView) findViewById(R.id.iv_compose_actionbar_cancel);
        iv_compose_actionbar_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
                overridePendingTransition(R.layout.stay_in_place, R.layout.exit_to_bottom);
            }
        });

        Button btn_compose_tweet = (Button) findViewById(R.id.btn_compose_tweet);
        btn_compose_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = et_compose_tweet.getText().toString();
                twitterClient.addTweet(text, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Tweet newTweet = Tweet.CreateTweet(response);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("tweet",newTweet);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                        overridePendingTransition(R.layout.stay_in_place, R.layout.exit_to_bottom);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(getBaseContext(), "Failed to add tweet", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
        overridePendingTransition(R.layout.stay_in_place, R.layout.exit_to_bottom);
    }
}
