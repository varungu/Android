package com.varungupta.simpletwitterclient.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.varungupta.simpletwitterclient.Model.User;
import com.varungupta.simpletwitterclient.R;
import com.varungupta.simpletwitterclient.RestClient.TwitterClient;
import com.varungupta.simpletwitterclient.TwitterApplication;

public class ComposeActivity extends ActionBarActivity {

    TwitterClient twitterClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        twitterClient = TwitterApplication.getTwitterClient();
        User user = twitterClient.getAuthenticatedUser();

        ImageView iv_compose_user_image = (ImageView)findViewById(R.id.iv_compose_user_image);
        iv_compose_user_image.setImageResource(0);
        Picasso.with(this).load(user.profile_image_url).into(iv_compose_user_image);

        TextView tv_compose_user_name = (TextView) findViewById(R.id.tv_compose_user_name);
        tv_compose_user_name.setText(user.name);

        TextView tv_compose_user_handle = (TextView) findViewById(R.id.tv_compose_user_handle);
        tv_compose_user_handle.setText(user.screen_name);
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
        super.onBackPressed();
        overridePendingTransition(R.layout.stay_in_place, R.layout.exit_to_bottom);
    }
}
