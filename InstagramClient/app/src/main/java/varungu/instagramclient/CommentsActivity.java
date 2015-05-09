package varungu.instagramclient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CommentsActivity extends ActionBarActivity {

    ArrayList<Comment> comments;
    ArrayAdapter<Comment> commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        comments = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(this, comments);

        ListView lvComments = (ListView) findViewById(R.id.lvComments);
        lvComments.setAdapter(commentsAdapter);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");

        //Add caption as a top comment
        Comment comment = new Comment();
        comment.profileUrl = extras.getString("profile");
        comment.username = extras.getString("username");
        comment.text = extras.getString("caption");
        comments.add(comment);
        commentsAdapter.notifyDataSetChanged();

        // Fetch other comments
        fetchComments(id);
    }

    private void fetchComments(String id) {
        String url = String.format("https://api.instagram.com/v1/media/%s/comments?client_id=8e2d2f9ccbdd4d8f81ce03ef317d6b53", id);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            // On Success
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    /*
                    {
                        "meta": {
                            "code": 200
                        },
                        "data": [
                            {
                                "created_time": "1280780324",
                                "text": "Really amazing photo!",
                                "from": {
                                    "username": "snoopdogg",
                                    "profile_picture": "http://images.instagram.com/profiles/profile_16_75sq_1305612434.jpg",
                                    "id": "1574083",
                                    "full_name": "Snoop Dogg"
                                },
                                "id": "420"
                            },
                            ...
                        ]
                    }
                     */

                    JSONArray dataJsonArray = response.getJSONArray("data");
                    for (int i = 0; i < dataJsonArray.length(); i++) {
                        JSONObject commentJson = dataJsonArray.getJSONObject(i);
                        Comment comment = new Comment();
                        comment.text = commentJson.getString("text");
                        comment.username = commentJson.getJSONObject("from").getString("username");
                        comment.profileUrl = commentJson.getJSONObject("from").getString("profile_picture");
                        comments.add(comment);

                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                commentsAdapter.notifyDataSetChanged();
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
        getMenuInflater().inflate(R.menu.menu_comments, menu);
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
