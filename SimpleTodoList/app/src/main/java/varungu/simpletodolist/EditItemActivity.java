package varungu.simpletodolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EditItemActivity extends ActionBarActivity {

    String itemText;
    int position;
    Date dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        position = getIntent().getIntExtra("position", 0);
        itemText = getIntent().getStringExtra("value");
        dueDate = (Date)getIntent().getSerializableExtra("dueDate");

        EditText editTextField = (EditText) findViewById(R.id.editTextView);
        editTextField.setText(itemText);

        EditText dueDateField = (EditText) findViewById(R.id.dueDateView);
        dueDateField.setText(new SimpleDateFormat("MM/dd/yyy").format(dueDate));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    public void onClickSave(View view)
    {
        try {
            EditText editTextField = (EditText) findViewById(R.id.editTextView);
            String updatedText = editTextField.getText().toString().trim();

            EditText dueDateField = (EditText) findViewById(R.id.dueDateView);
            dueDate = new SimpleDateFormat("MM/dd/yyy").parse(dueDateField.getText().toString().trim());
            if (!updatedText.equals("")) {
                Intent data = new Intent();
                data.putExtra("value", updatedText);
                data.putExtra("position", position);
                data.putExtra("dueDate", dueDate);
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
            }
        }
        catch (ParseException e) {
            // Show some error message here
        }
    }
}
