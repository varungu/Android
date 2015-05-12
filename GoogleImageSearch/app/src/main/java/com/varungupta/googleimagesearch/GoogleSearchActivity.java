package com.varungupta.googleimagesearch;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;


public class GoogleSearchActivity extends ActionBarActivity {

    EditText etSearch;
    GridView gvResults;
    ArrayList<ImageResult> imageResults;
    ArrayAdapter<ImageResult> imageResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_search);

        etSearch = (EditText) findViewById(R.id.etSearch);
        gvResults = (GridView) findViewById(R.id.gvResults);

        imageResults = new ArrayList<>();
        imageResultAdapter = new ArrayAdapter<ImageResult>(this, android.R.layout.simple_expandable_list_item_1, imageResults);
        gvResults.setAdapter(imageResultAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_google_search, menu);
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

    public void executeSearch(View view){
        String searchQuery = etSearch.getText().toString();
        imageResultAdapter.clear();
        GetResults(searchQuery);
    }

    public void GetResults(final String searchQuery) {

        // Search google images with this query
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + searchQuery + "&start=" + imageResults.size();
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                /*
                {
                    responseData: {
                        results: [
                            {
                                GsearchResultClass: "GimageSearch",
                                width: "2956",
                                height: "2000",
                                imageId: "ANd9GcQGUmNpgU3iH6wod2p5ooaJkQlvAVLMmc4f1olZPp9A8nWX8HM9vTdPdwg",
                                tbWidth: "150",
                                tbHeight: "101",
                                unescapedUrl: "http://www.grandfather.com/wp-content/uploads/2012/08/fall-road.jpg",
                                url: "http://www.grandfather.com/wp-content/uploads/2012/08/fall-road.jpg",
                                visibleUrl: "www.grandfather.com",
                                title: "Hi Res Photos-",
                                titleNoFormatting: "Hi Res Photos-",
                                originalContextUrl: "http://www.grandfather.com/about-grandfather-mountain/media/hi-res-photos-fall/",
                                content: "<b>fall</b>-road.jpg",
                                contentNoFormatting: "fall-road.jpg",
                                tbUrl: "http://t2.gstatic.com/images?q=tbn:ANd9GcQGUmNpgU3iH6wod2p5ooaJkQlvAVLMmc4f1olZPp9A8nWX8HM9vTdPdwg"
                            },
                            .
                            .
                            .
                        ],
                        cursor: {
                            resultCount: "326,000,000",
                            pages: [
                                {
                                    start: "0",
                                    label: 1
                                },
                                {
                                    start: "4",
                                    label: 2
                                },
                                .
                                .
                                .
                            ],
                            estimatedResultCount: "326000000",
                            currentPageIndex: 0,
                            moreResultsUrl: "http://www.google.com/images?oe=utf8&ie=utf8&source=uds&start=0&hl=en&q=fall",
                            searchResultTime: "0.37"
                        }
                    },
                    responseDetails: null,
                    responseStatus: 200
                }
                */

                imageResultAdapter.addAll(ImageResult.ImageResults(response));

                if (imageResults.size() < 16) {
                    GetResults(searchQuery);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("DEBUG", errorResponse.toString());
            }
        });
    }
}
