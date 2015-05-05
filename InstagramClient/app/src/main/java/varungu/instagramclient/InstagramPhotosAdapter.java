package varungu.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by varungupta on 5/4/2015.
 */
public class InstagramPhotosAdapter  extends ArrayAdapter<InstagramPhoto>{
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_expandable_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        TextView tvCaption = (TextView) convertView.findViewById(R.id.text);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.image);

        tvCaption.setText(photo.caption);

        // Clear last image
        ivPhoto.setImageResource(0);

        // Insert image view using picasso
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);

        return convertView;
    }
}
