package com.varungupta.googleimagesearch;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by varungupta on 5/12/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {
    public ImageResultsAdapter(Context context, List<ImageResult> objects) {
        super(context, R.layout.google_result, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageResult imageResult = getItem(position);
        ImageResultViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.google_result, parent, false);

            viewHolder = new ImageResultViewHolder();
            viewHolder.ivImage = (ImageView)convertView.findViewById(R.id.ivImage);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ImageResultViewHolder)convertView.getTag();
        }

        viewHolder.ivImage.setImageResource(0);
        Picasso.with(getContext()).load(imageResult.thumbnailUrl).placeholder(R.drawable.loader).into(viewHolder.ivImage);

        viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent ImageIntent = new Intent(getContext(), PhotoActivity.class);
                ImageIntent.putExtra("url", imageResult.url);
                getContext().startActivity(ImageIntent);
            }
        });

        return convertView;
    }
}
