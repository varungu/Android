package com.varungupta.simpletwitterclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.varungupta.simpletwitterclient.Model.Location;
import com.varungupta.simpletwitterclient.R;

import java.util.List;

/**
 * Created by varungupta on 6/1/15.
 */
public class LocationAdapter extends ArrayAdapter<Location> {
    public LocationAdapter(Context context, List<Location> objects) {
        super(context, R.layout.location_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Location location = getItem(position);

        LocationViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.location_item, parent, false);
            viewHolder = new LocationViewHolder();
            viewHolder.tvLocationItem = (TextView) convertView.findViewById(R.id.tvLocationItem);
            viewHolder.tvLocationAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (LocationViewHolder)convertView.getTag();
        }

        viewHolder.tvLocationItem.setText(location.name);
        viewHolder.tvLocationAddress.setText(location.address);

        return convertView;
    }
}
