package com.varungupta.simpletwitterclient.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

@Table(name = "Tweets")
public class Tweet extends Model {
    // Define database columns and associated fields
    @Column(name = "id_str")
    public String id_str;
    @Column(name = "created_at")
    public long created_at;
    @Column(name = "text")
    public String text;
    @Column(name = "user_name")
    public String user_name;
    @Column(name = "user_profile_image_url")
    public String user_profile_image_url;
    @Column(name = "user_id_str")
    public String user_id_str;
    @Column(name = "user_screen_name")
    public String user_screen_name;


    // Make sure to always define this constructor with no arguments
    public Tweet() {
        super();
    }

    public Tweet(JSONObject object){
        super();

        try {
            /*
              {
                "coordinates": null,
                "truncated": false,
                "created_at": "Tue Aug 28 21:16:23 +0000 2012",
                "favorited": false,
                "id_str": "240558470661799936",
                "in_reply_to_user_id_str": null,
                "entities": {
                  "urls": [
             
                  ],
                  "hashtags": [
             
                  ],
                  "user_mentions": [
             
                  ]
                },
                "text": "just another test",
                "contributors": null,
                "id": 240558470661799936,
                "retweet_count": 0,
                "in_reply_to_status_id_str": null,
                "geo": null,
                "retweeted": false,
                "in_reply_to_user_id": null,
                "place": null,
                "source": "<a href="//realitytechnicians.com\"" rel="\"nofollow\"">OAuth Dancer Reborn</a>",
                "user": {
                  "name": "OAuth Dancer",
                  "profile_sidebar_fill_color": "DDEEF6",
                  "profile_background_tile": true,
                  "profile_sidebar_border_color": "C0DEED",
                  "profile_image_url":"http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
                  "created_at": "Wed Mar 03 19:37:35 +0000 2010",
                  "location": "San Francisco, CA",
                  "follow_request_sent": false,
                  "id_str": "119476949",
                  "is_translator": false,
                  "profile_link_color": "0084B4",
                  "entities": {
                    "url": {
                      "urls": [
                        {
                          "expanded_url": null,
                          "url": "http://bit.ly/oauth-dancer",
                          "indices": [
                            0,
                            26
                          ],
                          "display_url": null
                        }
                      ]
                    },
                    "description": null
                  },
                  "default_profile": false,
                  "url": "http://bit.ly/oauth-dancer",
                  "contributors_enabled": false,
                  "favourites_count": 7,
                  "utc_offset": null,
                  "profile_image_url_https":"https://si0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
                  "id": 119476949,
                  "listed_count": 1,
                  "profile_use_background_image": true,
                  "profile_text_color": "333333",
                  "followers_count": 28,
                  "lang": "en",
                  "protected": false,
                  "geo_enabled": true,
                  "notifications": false,
                  "description": "",
                  "profile_background_color": "C0DEED",
                  "verified": false,
                  "time_zone": null,
                  "profile_background_image_url_https":"https://si0.twimg.com/profile_background_images/80151733/oauth-dance.png",
                  "statuses_count": 166,
                  "profile_background_image_url":"http://a0.twimg.com/profile_background_images/80151733/oauth-dance.png",
                  "default_profile_image": false,
                  "friends_count": 14,
                  "following": false,
                  "show_all_inline_media": false,
                  "screen_name": "oauth_dancer"
                },
                "in_reply_to_screen_name": null,
                "in_reply_to_status_id": null
              },

             */
            this.id_str = object.getString("id_str");
            this.created_at = Date.parse(object.getString("created_at"));
            this.text = object.getString("text");

            JSONObject user = object.getJSONObject("user");
            this.user_name = user.getString("name");
            this.user_profile_image_url = user.getString("profile_image_url");
            this.user_id_str = user.getString("id_str");
            this.user_screen_name = user.getString("screen_name");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = new Tweet(tweetJson);
            tweet.save();
            tweets.add(tweet);
        }

        return tweets;
    }
}
