package com.varungupta.googleimagesearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;


public class GoogleSearchActivity extends ActionBarActivity implements SettingsDialogListener {

    StaggeredGridView gvResults;
    TextView tvEmpty;
    ArrayList<ImageResult> imageResults;
    ImageResultsAdapter imageResultsAdapter;
    boolean loading;
    String searchQuery;
    MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_search);

        loading = false;
        searchQuery = "";

        gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
        tvEmpty = (TextView) findViewById(R.id.tvEmpty);

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

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Start NewActivity.class
                ImageResult imageResult = imageResultsAdapter.getItem(position);
                ImageView ivImage = (ImageView) view.findViewById(R.id.ivImage);
                Intent ImageIntent = new Intent(GoogleSearchActivity.this, PhotoActivity.class);
                ImageIntent.putExtra("url", imageResult.url);
                ImageIntent.putExtra("thumbnail", getLocalBitmapUri(ivImage));
                GoogleSearchActivity.this.startActivity(ImageIntent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_google_search, menu);

        miActionProgressItem = menu.findItem(R.id.miActionProgress);

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
            miActionProgressItem.setVisible(true);

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
                    tvEmpty.setVisibility(View.GONE);
                    imageResultsAdapter.addAll(ImageResult.ImageResults(response));
                    miActionProgressItem.setVisible(false);
                    loading = false;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("DEBUG", errorResponse.toString());
                    miActionProgressItem.setVisible(false);
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

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public String getLocalBitmapUri(ImageView ivImage) {
        Drawable mDrawable = ivImage.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                mBitmap, "Image Description", null);

        return path;
    }

}
