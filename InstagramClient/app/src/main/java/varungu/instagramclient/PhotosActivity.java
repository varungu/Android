package varungu.instagramclient;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class PhotosActivity extends ActionBarActivity {

    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter instagramPhotosAdapter;
    private SwipeRefreshLayout swipeContainer;
    private boolean loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        loading = false;

        //Fetch popular photos
        photos = new ArrayList<InstagramPhoto>();
        instagramPhotosAdapter = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(instagramPhotosAdapter);
        fetchPopularPhotos(true);

        // Add infinite scroll
        lvPhotos.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!loading) {
                    if (firstVisibleItem + visibleItemCount + 5 >= totalItemCount) {
                        // User has less than 5 items left to scroll, load more
                        fetchPopularPhotos(false);
                    }
                }
            }
        });

        // Add swipe to refresh
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPopularPhotos(true);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    public void fetchPopularPhotos(final boolean clearList) {
        loading = true;
        String url = "https://api.instagram.com/v1/media/popular?client_id=8e2d2f9ccbdd4d8f81ce03ef317d6b53";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            // On Success
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /*
                    {
                    "videos": {
                            "standard_resolution": {
                                "url": "https:\/\/scontent.cdninstagram.com\/hphotos-xaf1\/t50.2886-16\/11214991_1576091376005726_1798705119_n.mp4",
                                "width": 640,
                                "height": 640
                            },
                        },
                        "created_time": "1430696025",
                        "likes": {
                            "count": 36044,
                        },
                        "images": {
                            "standard_resolution": {
                                "url": "https:\/\/scontent.cdninstagram.com\/hphotos-xpa1\/t51.2885-15\/e15\/11184458_854129917956150_670458359_n.jpg",
                                "width": 640,
                                "height": 640
                            }
                        },
                        "caption": {
                            "text": "Love this city!! \ud83d\ude18\ud83d\ude0d\ud83d\ude09 NYC \nBlazer @hatchgal\nShirt @hatchgal\nLeggings @motherhoodmaternity\nShoes @barneysnyofficial\nHat @reissfashion",
                            "from":{
                               "username":"dr_m9rg3",
                               "profile_picture":"https:\/\/igcdn-photos-e-a.akamaihd.net\/hphotos-ak-xpa1\/t51.2885-19\/1739594_964471120265012_1511729204_a.jpg",
                               "id":"357267124",
                               "full_name":"\u062f\u0643\u062a\u0648\u0631 \u0645\u0635\u0631\u0642\u0639 \ud83c\udfab"
                            },
                        },
                        "type": "image",
                        "user": {
                            "username": "tameramowrytwo",
                            "profile_picture": "https:\/\/igcdn-photos-c-a.akamaihd.net\/hphotos-ak-xpf1\/t51.2885-19\/11005183_1544003252539498_2082156619_a.jpg",
                        }
                        "comments": {
                            "count": 506,
                            "data": [
                                {
                                    "created_time": "1431130641",
                                    "text": "some comment"
                                    "from": {
                                        "username": "xl_qtab",
                                        "profile_picture": "https://igcdn-photos-g-a.akamaihd.net/hphotos-ak-xpf1/t51.2885-19/11137949_557861774353606_550126288_a.jpg",
                                        "id": "1646018556",
                                        "full_name": ""
                                    },
                                    "id": "980717358462487707"
                                },
                                {
                                    "created_time": "1431130646",
                                    "text": "some comment"
                                    "from": {
                                        "username": "nasser_bin_mansour",
                                        "profile_picture": "https://scontent.cdninstagram.com/hphotos-xpf1/t51.2885-19/11008213_1422086488088524_399973610_a.jpg",
                                        "id": "917397038",
                                        "full_name": ""
                                    },
                                    "id": "980717400665574563"
                                },
                            ]
                        },
                    }
                     */

                    if (clearList) {
                        // Remove old items before adding new ones
                        instagramPhotosAdapter.clear();
                    }

                    JSONArray dataJsonArray = response.getJSONArray("data");
                    for (int i = 0; i < dataJsonArray.length(); i++) {
                        JSONObject photoJson = dataJsonArray.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.type = photoJson.getString("type");
                        photo.username = photoJson.getJSONObject("user").getString("username");
                        photo.profilePhotoUrl = photoJson.getJSONObject("user").getString("profile_picture");
                        photo.caption = photoJson.getJSONObject("caption").getString("text");
                        photo.captionUsername = photoJson.getJSONObject("caption").getJSONObject("from").getString("username");
                        photo.imageUrl = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.createdTime = new Date(photoJson.getLong("created_time") * 1000);
                        photo.imageHeight = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.imageWidth = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("width");
                        photo.likesCount = photoJson.getJSONObject("likes").getInt("count");

                        if (photo.type.equals("video")) {
                            photo.videoUrl = photoJson.getJSONObject("videos").getJSONObject("standard_resolution").getString("url");
                        }

                        // TODO: Add checks that atleast 2 comments are there and we are taking the latest comments
                        photo.commentsCount = photoJson.getJSONObject("comments").getInt("count");
                        JSONArray comments = photoJson.getJSONObject("comments").getJSONArray("data");
                        photo.comment1 = comments.getJSONObject(0).getString("text");
                        photo.comment1User = comments.getJSONObject(0).getJSONObject("from").getString("username");
                        photo.comment2 = comments.getJSONObject(1).getString("text");
                        photo.comment2User = comments.getJSONObject(1).getJSONObject("from").getString("username");

                        // Add to array list
                        photos.add(photo);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                instagramPhotosAdapter.notifyDataSetChanged();
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
                loading = false;

            }

            // OnFailure
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("DEBUG", errorResponse.toString());
                swipeContainer.setRefreshing(false);
                loading = false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
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
}
