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

    void loadItems() {
        ArrayList<GroceryItem> allItems = dbHelper.getAllItems();
        ArrayList<GroceryItem> unboughtItems = new ArrayList<>();
        ArrayList<GroceryItem> boughtItems = new ArrayList<>();

        for (GroceryItem item : allItems) {
            if (item.isBought()) {
                boughtItems.add(item);
            } else {
                unboughtItems.add(item);
            }
        }

        groceryList = new ArrayList<>();
        groceryList.addAll(unboughtItems);
        // Add a dummy "section" header item
        if (!boughtItems.isEmpty()) {
            GroceryItem sectionHeader = new GroceryItem(-1, "Bought Already", "", false);
            groceryList.add(sectionHeader);
        }
        groceryList.addAll(boughtItems);

        double totalCost = 0;
        for (GroceryItem item : boughtItems) {
            totalCost += item.getPrice();
        }
        // Add dummy total item
        GroceryItem totalItem = new GroceryItem(-2, "Total Cost", "", false);
        totalItem.setPrice(totalCost);
        groceryList.add(totalItem);

        adapter = new GroceryAdapter(this, groceryList, dbHelper);
        recyclerViewItems.setAdapter(adapter);
    }
    public void updateTotalOnly() {
        if (groceryList.isEmpty()) return;

        // Calculate new total
        double totalCost = 0;
        for (GroceryItem item : groceryList) {
            if (item.isBought() && item.getId() > 0) { // Only count actual bought items
                totalCost += item.getPrice();
            }
        }

        // Update the total item
        for (int i = groceryList.size() - 1; i >= 0; i--) {
            if (groceryList.get(i).getId() == -2) { // Find the total item
                groceryList.get(i).setPrice(totalCost);
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }

}