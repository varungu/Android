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
 * Created by varungupta on 5/8/15.
 */
public class CommentsAdapter extends ArrayAdapter<Comment> {
    public CommentsAdapter(Context context, List<Comment> objects) {
        super(context, R.layout.item_comment, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Comment comment = getItem(position);
        CommentViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
            viewHolder = new CommentViewHolder();
            viewHolder.ivProfilePhoto = (ImageView) convertView.findViewById(R.id.ivProfilePhoto);
            viewHolder.tvUsername = (TextView)convertView.findViewById(R.id.tvUsername);
            viewHolder.tvComment = (TextView) convertView.findViewById(R.id.tvComment);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (CommentViewHolder)convertView.getTag();
        }

        // Load profile photo
        // ivProfilePhoto.setImageResource(0) will clear the last image in case convertView is reused
        viewHolder.ivProfilePhoto.setImageResource(0);
        Picasso.with(getContext()).load(comment.profileUrl).into(viewHolder.ivProfilePhoto);

        // Set username
        viewHolder.tvUsername.setText(comment.username);

        // SetText
        viewHolder.tvComment.setText(comment.text);

        return convertView;
    }
}
