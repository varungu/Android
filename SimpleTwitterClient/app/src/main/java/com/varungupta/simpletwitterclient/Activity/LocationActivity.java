package com.varungupta.simpletwitterclient.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.varungupta.simpletwitterclient.Adapter.LocationAdapter;
import com.varungupta.simpletwitterclient.GPSTracker.GPSTracker;
import com.varungupta.simpletwitterclient.Model.Location;
import com.varungupta.simpletwitterclient.R;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
https://api.foursquare.com/v2/venues/search?client_id=RBQF024OHIT3B0KZVQRM0IK3GBY5LKG3POYQBLU0KPAU2KQR%20&client_secret=1045TISRYWPRSFJWKKOUWA5GGIVFA51WZLFZB3PIAZOHHJIG&ll=40.7,-74&v=20150501&m=foursquare
 */

public class LocationActivity extends ActionBarActivity {

    ArrayList<Location> locations;
    LocationAdapter locationAdapter;
    ListView lvLocations;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        locations = new ArrayList<>();
        locationAdapter = new LocationAdapter(this, locations);
        lvLocations = (ListView) findViewById(R.id.lvLocation);
        lvLocations.setAdapter(locationAdapter);
        lvLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("location",locations.get(position));
                setResult(RESULT_OK, returnIntent);
                finish();
                overridePendingTransition(R.layout.stay_in_place, R.layout.exit_to_right);
            }
        });

        ImageView iv_compose_actionbar_cancel = (ImageView) findViewById(R.id.iv_compose_actionbar_cancel);
        iv_compose_actionbar_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
                overridePendingTransition(R.layout.stay_in_place, R.layout.exit_to_right);
            }
        });

        // Get coordinates
        GPSTracker gpsTracker = new GPSTracker(this);
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();


        String url = "https://api.foursquare.com/v2/venues/search?client_id=RBQF024OHIT3B0KZVQRM0IK3GBY5LKG3POYQBLU0KPAU2KQR%20&client_secret=1045TISRYWPRSFJWKKOUWA5GGIVFA51WZLFZB3PIAZOHHJIG&v=20150501&m=foursquare&ll=" + String.format("%.2f,%.2f", latitude, longitude);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            // On Success
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i("location data", response.toString());
                    JSONArray venues = response.getJSONObject("response").getJSONArray("venues");
                    for (int i = 0; i < venues.length(); i++){
                        JSONObject locationJsonObject = venues.getJSONObject(i);

                        Location location = new Location();
                        location.id = locationJsonObject.getString("id");
                        location.name = locationJsonObject.getString("name");

                        JSONObject locObject = locationJsonObject.getJSONObject("location");
                        location.address = "";
                        if (locObject.has("address")) {
                            location.address = locObject.getString("address");
                        }
                        location.latitude = locationJsonObject.getJSONObject("location").getDouble("lat");
                        location.longitude = locationJsonObject.getJSONObject("location").getDouble("lng");

                        locations.add(location);
                    }
                    locationAdapter.notifyDataSetChanged();
                }
                catch (JSONException ex) {
                    Log.e("location error", ex.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("location error", errorResponse.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
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
