package com.varungupta.googleimagesearch;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;


public class GoogleSearchActivity extends ActionBarActivity implements SettingsDialogListener {

    StaggeredGridView gvResults;
    ArrayList<ImageResult> imageResults;
    ImageResultsAdapter imageResultsAdapter;
    boolean loading;
    String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_search);

        loading = false;
        searchQuery = "";

        gvResults = (StaggeredGridView) findViewById(R.id.gvResults);

        imageResults = new ArrayList<>();
        imageResultsAdapter = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(imageResultsAdapter);

        gvResults.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!loading && imageResultsAdapter.getCount() < 64) {
                    if (firstVisibleItem + visibleItemCount + 4 >= totalItemCount) {
                        // User has less than 4 items left to scroll, load more
                        GetResults();
                    }
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_google_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                searchQuery = query;
                imageResultsAdapter.clear();
                GetResults();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

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
            showSettingsDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettingsDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SettingsDialog editNameDialog = SettingsDialog.getInstance(this);
        editNameDialog.show(fm, "Settings");
    }

    public void GetResults() {

        if (searchQuery != null && searchQuery.trim() != "") {
            loading = true;
            // Search google images with this query
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("ajax.googleapis.com")
                    .appendPath("ajax")
                    .appendPath("services")
                    .appendPath("search")
                    .appendPath("images")
                    .appendQueryParameter("v", "1.0")
                    .appendQueryParameter("q", searchQuery)
                    .appendQueryParameter("start", "" + imageResultsAdapter.getCount());
            SearchSettings.getInstance().appendParams(builder);

            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.get(builder.build().toString(), null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    imageResultsAdapter.addAll(ImageResult.ImageResults(response));
                    loading = false;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("DEBUG", errorResponse.toString());
                    loading = false;
                }
            });
        }
    }

    @Override
    public void onSaveSettingsClicked() {
        imageResultsAdapter.clear();
        GetResults();
    }
}
