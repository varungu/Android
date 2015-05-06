package varungu.instagramclient;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

/**
 * Created by varungupta on 5/4/2015.
 */
public class InstagramPhotosAdapter  extends ArrayAdapter<InstagramPhoto>{

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, R.layout.item_photo, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final InstagramPhoto photo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        // Load profile photo
        // ivProfilePhoto.setImageResource(0) will clear the last image in case convertView is reused
        ImageView ivProfilePhoto = (ImageView) convertView.findViewById(R.id.ivProfilePhoto);
        ivProfilePhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.profilePhotoUrl).into(ivProfilePhoto);

        // Set username
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        tvUsername.setText(photo.username);

        // Set image
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivImage);
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.loader).into(ivPhoto);

        if (photo.type.equals("image")){
            ivPhoto.setOnClickListener(null);
        }
        else {
            ivPhoto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {

                    // Start NewActivity.class
                    Intent videoIntent = new Intent(getContext(), VideoViewActivity.class);
                    videoIntent.putExtra("url", photo.videoUrl);
                    videoIntent.putExtra("caption", photo.caption);
                    getContext().startActivity(videoIntent);
                }
            });
        }
        // Set caption
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        tvCaption.setText(photo.caption);

        // Convert CreatedTime to Relative time and set the same
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        tvTime.setText(getRelativeTime(photo.createdTime));

        return convertView;
    }

    private String getRelativeTime(Date time)
    {
        Date currentTime = new Date();
        long timeDiff = (currentTime.getTime() - time.getTime())/1000; // Time difference in seconds

        // Convert seconds to string
        long minute = 60;
        long hour = minute * 60;
        long day = hour * 24;
        long week = day * 7;

        if (timeDiff < minute){
            return String.format("%ds", timeDiff);
        }
        else if (timeDiff < hour){
            return String.format("%dm", timeDiff/minute);
        }
        if (timeDiff < day){
            return String.format("%dh", timeDiff/hour);
        }
        if (timeDiff < week){
            return String.format("%dm", timeDiff/day);
        }
        return String.format("%dw", timeDiff/week);
    }
}
