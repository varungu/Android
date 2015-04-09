package varungu.simpletodolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private final int REQUEST_CODE = 20;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView itemsListView;
    TodoItemDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemsListView = (ListView) findViewById(R.id.itemsListView);
        database = new TodoItemDatabase(this);
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        itemsListView.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener(){
        itemsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("value", items.get(position));
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }
        );
        itemsListView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        database.deleteValue(items.get(position));
                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onClickAdd(View view)
    {
        EditText addNewItemTextbox = (EditText) findViewById(R.id.addTextField);
        String textToAdd = addNewItemTextbox.getText().toString().trim();
        if (!textToAdd.equals(""))
        {
            itemsAdapter.add(textToAdd);
            database.addValue(textToAdd);
        }
        addNewItemTextbox.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String value = data.getExtras().getString("value");
            int position = data.getExtras().getInt("position", 0);

            //Update item
            database.deleteValue(items.get(position));
            items.remove(position);
            itemsAdapter.notifyDataSetChanged();
            itemsAdapter.insert(value,position);
            database.addValue(value);
        }
    }

    private void readItems()
    {
        try {
            items = database.getAllValues();
        }
        catch (Exception e) {
            items = new ArrayList<String>();
        }
    }
}
