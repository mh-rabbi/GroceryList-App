package com.quickgrocerylist.grocerylist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {

    private final Context context;
    private final ArrayList<GroceryItem> groceryList;
    private final GroceryDBHelper dbHelper;

    public GroceryAdapter(Context context, ArrayList<GroceryItem> groceryList, GroceryDBHelper dbHelper) {
        this.context = context;
        this.groceryList = groceryList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public GroceryAdapter.GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grocery, parent, false);
        return new GroceryViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int position) {
        GroceryItem item = groceryList.get(position);

        if (item.getId() == -1) {  // Section header
            holder.textViewItem.setText(item.getName());
            holder.textViewItem.setTextSize(18);
            holder.textViewItem.setTextColor(context.getResources().getColor(R.color.teal_700));
            holder.checkBoxBought.setVisibility(View.GONE);
            holder.buttonDelete.setVisibility(View.GONE);
        } else {
            holder.textViewItem.setText(item.getName() + " - " + item.getQuantity());
            holder.textViewItem.setTextColor(item.isBought()
                    ? context.getResources().getColor(R.color.gray)
                    : context.getResources().getColor(R.color.black));
            holder.checkBoxBought.setVisibility(View.VISIBLE);
            holder.buttonDelete.setVisibility(View.VISIBLE);
            holder.checkBoxBought.setChecked(item.isBought());

            holder.checkBoxBought.setOnCheckedChangeListener((buttonView, isChecked) -> {
                dbHelper.updateItem(item.getId(), isChecked);
                item.setBought(isChecked);
                ((MainActivity) context).loadItems(); // Refresh UI
            });

            holder.buttonDelete.setOnClickListener(v -> {
                dbHelper.deleteItem(item.getId());
                groceryList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, groceryList.size());
                ((MainActivity) context).loadItems(); // Refresh UI
            });
        }
    }


    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public static class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItem;
        CheckBox checkBoxBought;
        ImageView buttonDelete;
        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewItem);
            checkBoxBought = itemView.findViewById(R.id.checkBoxBought);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
