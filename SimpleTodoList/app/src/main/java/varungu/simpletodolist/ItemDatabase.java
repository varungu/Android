package varungu.simpletodolist;

/**
 * Created by Varun on 4/10/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ItemDatabase extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TodoAppDatabase1";

    // Contacts table name
    private static final String TABLE_CONTACTS = "TodoAppTable1";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_VALUE = "value";
    private static final String KEY_DUEDATE = "duedate";

    public ItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_VALUE + " TEXT, "
                + KEY_DUEDATE + " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    // Adding new item
    void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, item.id);
        values.put(KEY_VALUE, item.value);
        values.put(KEY_DUEDATE, new SimpleDateFormat("MM/dd/yyyy").format(item.dueDate));

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Items
    public ArrayList<Item> getAllItems() {
        ArrayList<Item> list = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String value = cursor.getString(1);
                Date dueDate;
                try {
                  dueDate = new SimpleDateFormat("MM/dd/yyyy").parse(cursor.getString(2));
                }
                catch (ParseException e) {
                    // TODO: Show error here
                    dueDate = new Date();
                }

                list.add(new Item(id, value, dueDate));
            } while (cursor.moveToNext());
        }
        return list;
    }

    // Delete Items
    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public void UpdateItem(Item item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, item.id);
        values.put(KEY_VALUE, item.value);
        values.put(KEY_DUEDATE, new SimpleDateFormat("MM/dd/yyyy").format(item.dueDate));

        db.update(TABLE_CONTACTS, values, KEY_ID +  " = ?",
                new String []{String.valueOf(item.id)});
    }
}

