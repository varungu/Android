package com.varungupta.simpletwitterclient.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.varungupta.simpletwitterclient.Fragments.TweetsListFragment;
import com.varungupta.simpletwitterclient.Model.Tweet;
import com.varungupta.simpletwitterclient.R;
import com.varungupta.simpletwitterclient.TwitterApplication;

public class TimelineActivity extends ActionBarActivity implements TweetsListFragment.ITweetsListFragmentListener {

    TweetsListFragment homeTweetsListFragment;
    TweetsListFragment notificationsTweetsListFragment;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        homeTweetsListFragment = null;
        notificationsTweetsListFragment = null;

        // Set action bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);

        // Set tabs and viewpager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);


    }

    public TweetsListFragment getFragment(int position) {
        if (position == 0) {
            // Return home timeline fragment
            if (homeTweetsListFragment == null){
                homeTweetsListFragment = TweetsListFragment.GetInstance(
                    new TweetsListFragment.ITweetsGetter() {
                        @Override
                        public void getTweets(long max_id, AsyncHttpResponseHandler handler) {
                            TwitterApplication.getTwitterClient().getHomeTimeline(max_id, handler);
                        }
                    },
                    this
                );
            }

            return homeTweetsListFragment;
        } else if (position == 1){
            if (notificationsTweetsListFragment == null) {
                notificationsTweetsListFragment = TweetsListFragment.GetInstance(
                        new TweetsListFragment.ITweetsGetter() {
                            @Override
                            public void getTweets(long max_id, AsyncHttpResponseHandler handler) {
                                TwitterApplication.getTwitterClient().getMentionsTimeline(max_id, handler);
                            }
                        },
                        this
                );
            }

            return notificationsTweetsListFragment;
        }

        return null;
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
            startComposeActivity("", 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 20) {
            if(resultCode == RESULT_OK){
                Tweet tweet = (Tweet)data.getSerializableExtra("tweet");
                getFragment(0).add(0, tweet);
            }
        }
    }

    @Override
    public void onReplyClicked(String usersInfo, long in_reply_to_status_id) {
        startComposeActivity(usersInfo, in_reply_to_status_id);
    }

    private void startComposeActivity(String usersInfo, long in_reply_to_status_id) {
        Intent intent = new Intent(this, ComposeActivity.class);
        intent.putExtra("info", usersInfo);
        intent.putExtra("in_reply_to_status_id", in_reply_to_status_id);
        startActivityForResult(intent, 20);

        overridePendingTransition(R.layout.enter_from_bottom, R.layout.stay_in_place);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
        TimelineActivity timelineActivity;
        String[] tabsTitles = new String[]{"Home", "Notifications"};

        public TweetsPagerAdapter(FragmentManager fragmentManager, TimelineActivity timelineActivity){
            super(fragmentManager);
            this.timelineActivity = timelineActivity;
        }

        @Override
        public Fragment getItem(int position) {
            return timelineActivity.getFragment(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabsTitles[position];
        }


        @Override
        public int getCount() {
            return tabsTitles.length;
        }

        @Override
        public int getPageIconResId(int position) {
            int selectedIndex = timelineActivity.viewPager.getCurrentItem();
            if (selectedIndex == position){
                if (position == 0){
                    return R.drawable.home_selected;
                }
                else {
                    return R.drawable.notifications_selected;
                }
            }
            else{
                if (position == 0){
                    return R.drawable.home;
                }
                else {
                    return R.drawable.notifications;
                }
            }
        }
    }
}
