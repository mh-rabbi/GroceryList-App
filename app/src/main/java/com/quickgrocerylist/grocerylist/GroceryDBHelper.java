package com.quickgrocerylist.grocerylist;

import android.content.Context;
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
        //String insertQuery = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME + ", " + COLUMN_QUANTITY + ") VALUES ('" + name + "', '" + quantity + "')";
       // db.execSQL(insertQuery);
        db.close();
    }

    public ArrayList<GroceryItem> getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        ArrayList<GroceryItem> list = new ArrayList<>();

        db.close();
        return list;
    }

    public void updateItem(int id, String name, String quantity, boolean isBought) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String updateQuery = "UPDATE " + TABLE_NAME + " SET " + COLUMN_NAME + " = '" + name + "', " + COLUMN_QUANTITY + " = '" + quantity + "', " + COLUMN_IS_BOUGHT + " = " + (isBought ? 1 : 0) + " WHERE " + COLUMN_ID + " = " + id;
        //db.execSQL(updateQuery);
        db.close();
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id;
        //db.execSQL(deleteQuery);
        db.close();
    }

}
