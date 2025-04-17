package com.quickgrocerylist.grocerylist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editTextItemName, editTextQuantity;
    Button buttonAddItem;
    RecyclerView recyclerViewItems;

    GroceryDBHelper dbHelper;
    GroceryAdapter adapter;
    ArrayList<GroceryItem> groceryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editTextItemName = findViewById(R.id.editTextItemName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        buttonAddItem = findViewById(R.id.buttonAddItem);
        recyclerViewItems = findViewById(R.id.recyclerViewItems);

        dbHelper = new GroceryDBHelper(this);

        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        loadItems(); // Load items from the database when the activity is created
       /* groceryList = dbHelper.getAllItems();
        adapter = new GroceryAdapter(this, groceryList, dbHelper);
        recyclerViewItems.setAdapter(adapter);*/

        buttonAddItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = editTextItemName.getText().toString().trim();
                String quantity = editTextQuantity.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(quantity)) {
                    Toast.makeText(MainActivity.this, "Please enter both name and quantity", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHelper.insertItem(name, quantity);
                editTextItemName.setText("");
                editTextQuantity.setText("");
                loadItems(); // Refresh the list after adding a new item
            }
        });
    }

        private void loadItems() {
            groceryList = dbHelper.getAllItems();
            adapter = new GroceryAdapter(this, groceryList, dbHelper);
            recyclerViewItems.setAdapter(adapter);
        }
}