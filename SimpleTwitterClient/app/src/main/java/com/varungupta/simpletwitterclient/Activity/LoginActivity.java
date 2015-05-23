package com.varungupta.simpletwitterclient.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.varungupta.simpletwitterclient.R;
import com.varungupta.simpletwitterclient.RestClient.TwitterClient;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Hide title bar
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    public void onLoginSuccess() {
        Intent timelineIntent = new Intent(this, TimelineActivity.class);
        startActivity(timelineIntent);

        overridePendingTransition(R.layout.enter_from_right, R.layout.exit_to_left);
    }

    @Override
    public void onLoginFailure(Exception e) {
        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }


    public void loginToRest(View view) {
        getClient().connect();
    }
}
