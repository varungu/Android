package varungu.instagramclient;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {

    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter instagramPhotosAdapter;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        //Fetch popular photos
        photos = new ArrayList<InstagramPhoto>();
        instagramPhotosAdapter = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(instagramPhotosAdapter);
        fetchPopularPhotos();

        // Add swipe to refresh
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    public void fetchPopularPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id=8e2d2f9ccbdd4d8f81ce03ef317d6b53";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            // On Success
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /*
                    {
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
                        },
                        "type": "image",
                        "user": {
                            "username": "tameramowrytwo",
                            "profile_picture": "https:\/\/igcdn-photos-c-a.akamaihd.net\/hphotos-ak-xpf1\/t51.2885-19\/11005183_1544003252539498_2082156619_a.jpg",
                        }
                    }
                     */

                    // Remove old items before adding new ones
                    instagramPhotosAdapter.clear();
                    JSONArray dataJsonArray = response.getJSONArray("data");
                    for (int i = 0; i < dataJsonArray.length(); i++) {
                        JSONObject photoJson = dataJsonArray.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJson.getJSONObject("user").getString("username");
                        photo.profilePhotoUrl = photoJson.getJSONObject("user").getString("profile_picture");
                        photo.caption = photoJson.getJSONObject("caption").getString("text");
                        photo.imageUrl = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.imageWidth = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("width");
                        photo.likesCount = photoJson.getJSONObject("likes").getInt("count");

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

            }

            // OnFailure
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("DEBUG", errorResponse.toString());
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
