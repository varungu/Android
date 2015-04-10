package varungu.simpletodolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Varun on 4/10/2015.
 */
public class ItemsAdapter extends ArrayAdapter<Item> {
    public ItemsAdapter(Context context, ArrayList<Item> Items) {
        super(context, 0, Items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.itemValue);
        TextView tvHome = (TextView) convertView.findViewById(R.id.itemDueDate);
        // Populate the data into the template view using the data object
        tvName.setText(item.value);
        tvHome.setText(new SimpleDateFormat("E MM/dd/yyyy").format(item.dueDate));
        // Return the completed view to render on screen
        return convertView;
    }
}
