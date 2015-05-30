package com.varungupta.simpletwitterclient.Model;

import android.util.Log;

import com.activeandroid.annotation.Column;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by varungupta on 5/24/15.
 */
public class User implements Serializable{
    @Column(name = "user_id")
    public long id;
    @Column(name = "user_id_str")
    public String id_str;
    @Column(name = "user_screen_name")
    public String screen_name;
    @Column(name = "user_name")
    public String name;
    public String location;
    public String description;
    public String url;
    public String display_url;
    public int followers_count;
    public int friends_count;
    public boolean verified;
    public String profile_banner_url;
    @Column(name = "user_profile_image_url")
    public String profile_image_url;
    public boolean following;


    // Make sure to always define this constructor with no arguments
    public User() {
        super();
    }

    public User(JSONObject user){
        super();

        try {
            /*
               {
               "id":2442880182,
               "id_str":"2442880182",
               "name":"Varun Gupta",
               "screen_name":"vrungpta",
               "location":"San Francisco, CA",
               "description":"Dreamer, Doer, Thinker",
               "url":"https:\/\/t.co\/oScJELLip7",
               "entities":{
                  "url":{
                     "urls":[
                        {
                           "url":"https:\/\/t.co\/oScJELLip7",
                           "expanded_url":"https:\/\/github.com\/varungu\/",
                           "display_url":"github.com\/varungu\/",
                           "indices":[
                              0,
                              23
                           ]
                        }
                     ]
                  },
                  "description":{
                     "urls":[

                     ]
                  }
               },
               "protected":false,
               "followers_count":11,
               "friends_count":30,
               "listed_count":0,
               "created_at":"Mon Apr 14 00:18:46 +0000 2014",
               "favourites_count":4,
               "utc_offset":-25200,
               "time_zone":"America\/Los_Angeles",
               "geo_enabled":true,
               "verified":false,
               "statuses_count":11,
               "lang":"en",
               "status":{
                  "created_at":"Sat May 30 04:38:15 +0000 2015",
                  "id":604507089088544768,
                  "id_str":"604507089088544768",
                  "text":"RT @nytimes: The strongest evidence in the kayak case seems to be the suspect's incriminating statements http:\/\/t.co\/XbPnv9XVYM",
                  "source":"<a href=\"https:\/\/github.com\/varungu\/Android-Bootcamp\" rel=\"nofollow\">VarunAndroidTwitterClient<\/a>",
                  "truncated":false,
                  "in_reply_to_status_id":null,
                  "in_reply_to_status_id_str":null,
                  "in_reply_to_user_id":null,
                  "in_reply_to_user_id_str":null,
                  "in_reply_to_screen_name":null,
                  "geo":null,
                  "coordinates":null,
                  "place":null,
                  "contributors":null,
                  "retweeted_status":{
                     "created_at":"Fri May 29 23:21:06 +0000 2015",
                     "id":604427275384328192,
                     "id_str":"604427275384328192",
                     "text":"The strongest evidence in the kayak case seems to be the suspect's incriminating statements http:\/\/t.co\/XbPnv9XVYM",
                     "source":"<a href=\"http:\/\/www.socialflow.com\" rel=\"nofollow\">SocialFlow<\/a>",
                     "truncated":false,
                     "in_reply_to_status_id":null,
                     "in_reply_to_status_id_str":null,
                     "in_reply_to_user_id":null,
                     "in_reply_to_user_id_str":null,
                     "in_reply_to_screen_name":null,
                     "geo":null,
                     "coordinates":null,
                     "place":null,
                     "contributors":null,
                     "retweet_count":31,
                     "favorite_count":54,
                     "entities":{
                        "hashtags":[

                        ],
                        "symbols":[

                        ],
                        "user_mentions":[

                        ],
                        "urls":[
                           {
                              "url":"http:\/\/t.co\/XbPnv9XVYM",
                              "expanded_url":"http:\/\/nyti.ms\/1FILUrm",
                              "display_url":"nyti.ms\/1FILUrm",
                              "indices":[
                                 92,
                                 114
                              ]
                           }
                        ]
                     },
                     "favorited":false,
                     "retweeted":true,
                     "possibly_sensitive":false,
                     "lang":"en"
                  },
                  "retweet_count":31,
                  "favorite_count":0,
                  "entities":{
                     "hashtags":[

                     ],
                     "symbols":[

                     ],
                     "user_mentions":[
                        {
                           "screen_name":"nytimes",
                           "name":"The New York Times",
                           "id":807095,
                           "id_str":"807095",
                           "indices":[
                              3,
                              11
                           ]
                        }
                     ],
                     "urls":[
                        {
                           "url":"http:\/\/t.co\/XbPnv9XVYM",
                           "expanded_url":"http:\/\/nyti.ms\/1FILUrm",
                           "display_url":"nyti.ms\/1FILUrm",
                           "indices":[
                              105,
                              127
                           ]
                        }
                     ]
                  },
                  "favorited":false,
                  "retweeted":true,
                  "possibly_sensitive":false,
                  "lang":"en"
               },
               "contributors_enabled":false,
               "is_translator":false,
               "is_translation_enabled":false,
               "profile_background_color":"C0DEED",
               "profile_background_image_url":"http:\/\/abs.twimg.com\/images\/themes\/theme1\/bg.png",
               "profile_background_image_url_https":"https:\/\/abs.twimg.com\/images\/themes\/theme1\/bg.png",
               "profile_background_tile":false,
               "profile_image_url":"http:\/\/pbs.twimg.com\/profile_images\/604728777617186816\/VNjb7JLB_normal.jpg",
               "profile_image_url_https":"https:\/\/pbs.twimg.com\/profile_images\/604728777617186816\/VNjb7JLB_normal.jpg",
               "profile_banner_url":"https:\/\/pbs.twimg.com\/profile_banners\/2442880182\/1433013631",
               "profile_link_color":"0084B4",
               "profile_sidebar_border_color":"C0DEED",
               "profile_sidebar_fill_color":"DDEEF6",
               "profile_text_color":"333333",
               "profile_use_background_image":true,
               "default_profile":true,
               "default_profile_image":false,
               "following":false,
               "follow_request_sent":false,
               "notifications":false
            }
             */

            Log.i("user_json", user.toString());
            this.id = user.getLong("id");
            this.id_str = user.getString("id_str");
            this.screen_name = "@" + user.getString("screen_name");
            this.name = user.getString("name");
            this.profile_image_url = user.getString("profile_image_url");
            this.location = user.getString("location");
            this.description = user.getString("description");
            this.url = user.getJSONObject("entities").getJSONObject("url").getJSONArray("urls").getJSONObject(0).getString("display_url");
            this.followers_count = user.getInt("followers_count");
            this.friends_count = user.getInt("friends_count");
            this.verified = user.getBoolean("verified");
            this.profile_banner_url = user.getString("profile_banner_url");
            this.following = user.getBoolean("following");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
