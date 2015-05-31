package com.varungupta.simpletwitterclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.varungupta.simpletwitterclient.PhotoGallery.PhotoItem;
import com.varungupta.simpletwitterclient.R;

import java.util.List;

/**
 * Created by varungupta on 5/12/15.
 */
public class GalleryImageAdapter extends ArrayAdapter<PhotoItem> {
    public GalleryImageAdapter(Context context, List<PhotoItem> objects) {
        super(context, R.layout.gallery_image, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PhotoItem photoItem = getItem(position);
        GalleryImageViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gallery_image, parent, false);

            viewHolder = new GalleryImageViewHolder();
            viewHolder.ivImage = (ImageView)convertView.findViewById(R.id.ivImage);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (GalleryImageViewHolder)convertView.getTag();
        }

        if (position == 0) {
            // Camera
            viewHolder.ivImage.setImageResource(R.drawable.camera_128);
        }
        else {
            viewHolder.ivImage.setImageResource(0);
            Picasso.with(getContext()).load("file:" + photoItem.getThumbnailUri().toString()).into(viewHolder.ivImage);
        }

        return convertView;
    }
}
