package com.varungupta.googleimagesearch;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class PhotoActivity extends ActionBarActivity {

    ShareActionProvider miShareAction;
    MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        final ImageView ivImage = (ImageView) findViewById(R.id.ivImage);

        Bundle extras = getIntent().getExtras();
        String url = extras.getString("url");
        String thumbnail = extras.getString("thumbnail");
        Drawable yourDrawable = getResources().getDrawable(R.drawable.loader);
        try {
            InputStream inputStream = getContentResolver().openInputStream(Uri.parse(thumbnail));
            yourDrawable = Drawable.createFromStream(inputStream, thumbnail );
        } catch (FileNotFoundException e) {

        }

        ivImage.setImageResource(0);
        Picasso.with(this).load(url).placeholder(yourDrawable).into(ivImage, new Callback() {
            @Override
            public void onSuccess() {
                miActionProgressItem.setVisible(false);
                setupShareIntent(ivImage, miShareAction);
            }

            @Override
            public void onError() {
                miActionProgressItem.setVisible(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        MenuItem item = menu.findItem(R.id.miShare);
        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        miActionProgressItem.setVisible(true);

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

    // Gets the image URI and setup the associated share intent to hook into the provider
    public void setupShareIntent(ImageView ivImage, ShareActionProvider miShareAction) {
        // Fetch Bitmap Uri locally
        Uri bmpUri = getLocalBitmapUri(ivImage); // see previous remote images section
        // Create share intent as described above
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        // Attach share event to the menu item provider
        miShareAction.setShareIntent(shareIntent);
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView ivImage) {
        Drawable mDrawable = ivImage.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

        String path = Images.Media.insertImage(getContentResolver(),
                mBitmap, "Image Description", null);

        Uri uri = Uri.parse(path);
        return uri;
    }
}
