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
        params.put("count", 50);
        params.put("since_id", 1);
        params.put("contributor_details", true);

        if (max_id != 0) {
            params.put("max_id", max_id);
        }
        client.get(apiUrl, params, handler);
    }

    public void getUserTimeline(long user_id, long max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("count", 50);
        params.put("since_id", 1);
        params.put("contributor_details", true);

        if (user_id != 0){
            params.put("user_id", user_id);
        }

        if (max_id != 0) {
            params.put("max_id", max_id);
        }
        client.get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(long max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("since_id", 1);
        params.put("contributor_details", true);

        if (max_id != 0) {
            params.put("max_id", max_id);
        }
        client.get(apiUrl, params, handler);
    }

    public void addTweet(String status, long in_reply_to_status_id, AsyncHttpResponseHandler handler) {
        //POST
        // https://api.twitter.com/1.1/statuses/update.json?status=Maybe%20he%27l
        String apiUrl = getApiUrl("statuses/update.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("status", status);

        if (in_reply_to_status_id != 0) {
            params.put("in_reply_to_status_id", in_reply_to_status_id);
        }
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

    public void reTweet(String id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(String.format("statuses/retweet/%s.json", id));
        client.post(apiUrl, handler);
    }

    public void addTofavorite(long id, AsyncHttpResponseHandler handler) {
        //POST
        //https://api.twitter.com/1.1/favorites/create.json?id=243138128959913986
        String apiUrl = getApiUrl("favorites/create.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(apiUrl, params, handler);
    }

    public void followUser(long user_id, AsyncHttpResponseHandler handler) {
        //POST
        //https://api.twitter.com/1.1/friendships/create.json?user_id=1401881&follow=true
        String apiUrl = getApiUrl("friendships/create.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("id", user_id);
        params.put("follow", true);
        client.post(apiUrl, params, handler);
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
}
