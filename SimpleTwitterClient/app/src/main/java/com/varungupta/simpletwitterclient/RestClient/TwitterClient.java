package com.varungupta.simpletwitterclient.RestClient;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.varungupta.simpletwitterclient.Model.User;

import org.apache.http.Header;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/**
 * Created by varungupta on 5/23/15.
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1/";
    public static final String REST_CONSUMER_KEY = "XUk5aS1iWOJDztNFwU1zYQE6Y";
    public static final String REST_CONSUMER_SECRET = "iNwyTEBzzMDiTuIrqShAfe0P2DdVM0bMOd61icVmifO8mvitFL";
    public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets";

    private User authenticatedUser;

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getHomeTimeline(long max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("count", 200);
        params.put("since_id", 1);
        if (max_id != 0) {
            params.put("max_id", max_id);
        }
        client.get(apiUrl, params, handler);
    }

    public void addTweet(String status, AsyncHttpResponseHandler handler) {
        //POST
        // https://api.twitter.com/1.1/statuses/update.json?status=Maybe%20he%27l
        String apiUrl = getApiUrl("statuses/update.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("status", status);
        client.post(apiUrl, params, handler);
    }

    public void verifyCredentials() {
        //GET
        //https://api.twitter.com/1.1/account/verify_credentials.json
        String apiUrl = getApiUrl("account/verify_credentials.json");
        client.get(apiUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                authenticatedUser = new User(response);
            }
        });
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
}
