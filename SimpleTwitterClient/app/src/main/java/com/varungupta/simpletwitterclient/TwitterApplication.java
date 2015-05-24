package com.varungupta.simpletwitterclient;

import android.content.Context;

import com.varungupta.simpletwitterclient.RestClient.TwitterClient;

public class TwitterApplication extends com.activeandroid.app.Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterApplication.context = this;
    }

    public static TwitterClient getTwitterClient() {
        return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TwitterApplication.context);
    }
}