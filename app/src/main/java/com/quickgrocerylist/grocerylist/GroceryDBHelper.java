package com.quickgrocerylist.grocerylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GroceryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GroceryDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "grocery_items";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_IS_BOUGHT = "isBought";

    public GroceryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_QUANTITY + " TEXT," +
                COLUMN_IS_BOUGHT + " INTEGER DEFAULT 0)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // for now i am just dropping existing table and recreate for now new one
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertItem(String name, String quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_IS_BOUGHT, 0);
        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public ArrayList<GroceryItem> getAllItems() {
        ArrayList<GroceryItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_ID);

        if(c != null && c.moveToFirst()) {
            do {
                GroceryItem item = new GroceryItem();
                item.setId(c.getInt(c.getColumnIndexOrThrow(COLUMN_ID)));
                item.setName(c.getString(c.getColumnIndexOrThrow(COLUMN_NAME)));
                item.setQuantity(c.getString(c.getColumnIndexOrThrow(COLUMN_QUANTITY)));
                item.setBought(c.getInt(c.getColumnIndexOrThrow(COLUMN_IS_BOUGHT)) == 1);
                list.add(item);
            } while (c.moveToNext());
            c.close();
        }

        db.close();
        return list;
    }

    public void updateItem(int id, boolean isBought) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_BOUGHT, isBought ? 1 : 0);
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

}
