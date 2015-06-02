package com.varungupta.simpletwitterclient.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.varungupta.simpletwitterclient.Adapter.GalleryImageAdapter;
import com.varungupta.simpletwitterclient.Model.Location;
import com.varungupta.simpletwitterclient.Model.Tweet;
import com.varungupta.simpletwitterclient.Model.User;
import com.varungupta.simpletwitterclient.PhotoGallery.PhotoGalleryAsyncLoader;
import com.varungupta.simpletwitterclient.PhotoGallery.PhotoItem;
import com.varungupta.simpletwitterclient.R;
import com.varungupta.simpletwitterclient.RandomStringUtils.RandomStringUtils;
import com.varungupta.simpletwitterclient.RestClient.TwitterClient;
import com.varungupta.simpletwitterclient.TwitterApplication;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ComposeActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<List<PhotoItem>> {

    TwitterClient twitterClient;
    EditText et_compose_tweet;
    long in_reply_to_status_id;
    ArrayList<PhotoItem> galleryImages;
    GalleryImageAdapter galleryImageAdapter;
    String base64Media;
    ImageView iv_selected_image;
    GridView gvGalleryImages;
    ImageView iv_remove_selected_image;
    Location selectedLocation;
    TextView tv_selected_location;

    public final String APP_TAG = "Simple Twitter Client";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1039;
    public String photoFileName;


    public void onLaunchCamera() {
        String ext = "jpg";
        photoFileName = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), ext);

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/tmp"))); // set the image file name
        // Start the image capture intent to take photo
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = Uri.parse("/sdcard/tmp");
                onPictureSelected(takenPhotoUri);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == 50) {
            if (resultCode == RESULT_OK) {
                // Add location info here
                selectedLocation = (Location)data.getSerializableExtra("location");
                tv_selected_location.setText(selectedLocation.name);
                tv_selected_location.setVisibility(View.VISIBLE);
            }
        }
    }

    public void onPictureSelected(Uri uri){
        iv_selected_image.setVisibility(View.VISIBLE);
        Picasso.with(getBaseContext()).load("file:" + uri.toString()).into(iv_selected_image);
        gvGalleryImages.setVisibility(View.GONE);
        iv_remove_selected_image.setVisibility(View.VISIBLE);
        base64Media = encodeImage2(uri);
    }
    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return Uri.parse(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private String encodeImage2(Uri path)  {
        try {
            InputStream inputStream = new FileInputStream(path.toString());//You can get an inputStream using any IO API
            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            bytes = output.toByteArray();
            String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
            return encodedString;
        }
        catch (FileNotFoundException ex){
            Log.e("image not found", ex.toString());
        }

        return null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        base64Media = null;
        tv_selected_location = (TextView) findViewById(R.id.tv_selected_location);
        iv_selected_image = (ImageView)findViewById(R.id.iv_selected_image);
        iv_remove_selected_image = (ImageView) findViewById(R.id.iv_cancel_selected_image);
        gvGalleryImages = (GridView) findViewById(R.id.glGallery);
        galleryImages = new ArrayList<>();
        galleryImageAdapter = new GalleryImageAdapter(this, galleryImages);
        gvGalleryImages.setAdapter(galleryImageAdapter);
        gvGalleryImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // Handle click on camera
                    onLaunchCamera();
                } else {
                    onPictureSelected(galleryImages.get(position).getFullImageUri());
                }
            }
        });

        in_reply_to_status_id = getIntent().getExtras().getLong("in_reply_to_status_id");
        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);

        twitterClient = TwitterApplication.getTwitterClient();
        User user = twitterClient.getAuthenticatedUser();

        ImageView iv_compose_user_image = (ImageView)findViewById(R.id.iv_compose_user_image);
        iv_compose_user_image.setImageResource(0);
        Picasso.with(this).load(user.profile_image_url).into(iv_compose_user_image);

        TextView tv_compose_user_name = (TextView) findViewById(R.id.tv_compose_user_name);
        tv_compose_user_name.setText(user.name);

        TextView tv_compose_user_handle = (TextView) findViewById(R.id.tv_compose_user_handle);
        tv_compose_user_handle.setText(user.screen_name);

        final TextView tv_compose_actionbar_char_count = (TextView)findViewById(R.id.tv_compose_actionbar_char_count);

        et_compose_tweet = (EditText) findViewById(R.id.et_compose_tweet);
        et_compose_tweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_compose_actionbar_char_count.setText(String.valueOf(140 - s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_compose_tweet.setText(getIntent().getExtras().getString("info"));
        et_compose_tweet.setSelection(et_compose_tweet.getText().length());

        ImageView iv_compose_actionbar_cancel = (ImageView) findViewById(R.id.iv_compose_actionbar_cancel);
        iv_compose_actionbar_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
                overridePendingTransition(R.layout.stay_in_place, R.layout.exit_to_bottom);
            }
        });

        Button btn_compose_tweet = (Button) findViewById(R.id.btn_compose_tweet);
        btn_compose_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = et_compose_tweet.getText().toString();
                if (base64Media == null) {
                    tweet(text, 0);
                } else {
                    twitterClient.uploadMedia(base64Media, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                long media_id = response.getLong("media_id");
                                tweet(text, media_id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Toast.makeText(getBaseContext(), "Failed to add tweet", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Toast.makeText(getBaseContext(), "Failed to add tweet", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        ImageView ivCamera = (ImageView) findViewById(R.id.ivCamera);
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gvGalleryImages.isShown()) {
                    gvGalleryImages.setVisibility(View.GONE);
                }
                else {
                    gvGalleryImages.setVisibility(View.VISIBLE);
                }
            }
        });

        ImageView ivLocation = (ImageView) findViewById(R.id.ivLocation);
        ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start location intent here
                Intent intent = new Intent(getBaseContext(), LocationActivity.class);
                startActivityForResult(intent, 50);

                overridePendingTransition(R.layout.enter_from_right, R.layout.stay_in_place);
            }
        });
        getSupportLoaderManager().initLoader(0, null, this);
    }


    private void tweet(String message, long media_id){
        twitterClient.addTweet(message, in_reply_to_status_id, media_id, selectedLocation == null? 0:selectedLocation.latitude, selectedLocation == null? 0: selectedLocation.longitude, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet newTweet = Tweet.CreateTweet(response);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("tweet",newTweet);
                setResult(RESULT_OK, returnIntent);
                finish();
                overridePendingTransition(R.layout.stay_in_place, R.layout.exit_to_bottom);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(), "Failed to add tweet", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getBaseContext(), "Failed to add tweet", Toast.LENGTH_LONG).show();
            }
        });
    }
    /**
     * Loader Handlers for loading the photos in the background.
     */
    @Override
    public Loader<List<PhotoItem>> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader with no arguments, so it is simple.
        return new PhotoGalleryAsyncLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<PhotoItem>> loader, List<PhotoItem> data) {

        // Set the new data in the mAdapter.
        galleryImages.clear();

        // Add item for camera
        galleryImages.add(new PhotoItem(null, null));

        // Add rest of the items
        for(int i = data.size() - 1; i >= 0; i--){
            PhotoItem item = data.get(i);
            galleryImages.add(item);
        }

        galleryImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<PhotoItem>> loader) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
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

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
        overridePendingTransition(R.layout.stay_in_place, R.layout.exit_to_bottom);
    }

    public void removeSelectedImage(View view) {
        iv_selected_image.setVisibility(View.GONE);
        iv_selected_image.setImageResource(0);
        gvGalleryImages.setVisibility(View.VISIBLE);
        iv_remove_selected_image.setVisibility(View.GONE);
        base64Media = null;
    }
}
