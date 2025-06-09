package com.quickgrocerylist.grocerylist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
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
            holder.textViewItem.setTypeface(null, Typeface.BOLD);
            holder.textViewItem.setTextColor(context.getResources().getColor(R.color.teal_700));

            holder.checkBoxBought.setVisibility(View.GONE);
            holder.buttonDelete.setVisibility(View.GONE);
            holder.editTextPrice.setVisibility(View.GONE);
        }else if (item.getId() == -2) {
            holder.textViewItem.setText(item.getName() + ": ৳" + item.getPrice());
            holder.textViewItem.setTextSize(16);
            holder.textViewItem.setTypeface(null, Typeface.BOLD);
            holder.textViewItem.setTextColor(context.getResources().getColor(R.color.teal_700));

            holder.checkBoxBought.setVisibility(View.GONE);
            holder.buttonDelete.setVisibility(View.GONE);
            holder.editTextPrice.setVisibility(View.GONE);
        } else {
            holder.textViewItem.setText(item.getName() + " - " + item.getQuantity());
            holder.textViewItem.setTypeface(null, Typeface.NORMAL);
            holder.textViewItem.setTextColor(item.isBought()
                    ? context.getResources().getColor(R.color.gray)
                    : context.getResources().getColor(R.color.black));
            holder.checkBoxBought.setVisibility(View.VISIBLE);
            holder.buttonDelete.setVisibility(View.VISIBLE);
            holder.checkBoxBought.setOnCheckedChangeListener(null);
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

            if (item.isBought()) {
                holder.editTextPrice.setVisibility(View.VISIBLE);
                holder.editTextPrice.setText(item.getPrice() > 0 ? String.valueOf(item.getPrice()) : "");
               // holder.editTextPrice.setText(String.valueOf(item.getPrice()));

                // Remove any existing TextWatcher (recycled views issue)
                if (holder.editTextPrice.getTag() instanceof TextWatcher) {
                    holder.editTextPrice.removeTextChangedListener((TextWatcher) holder.editTextPrice.getTag());
                }
                holder.editTextPrice.setText(String.valueOf(item.getPrice())); // optional: sync with DB

                TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            double price = s.toString().isEmpty() ? 0 : Double.parseDouble(s.toString());
                            item.setPrice(price);
                            dbHelper.updateItemPrice(item.getId(), price);
                            // Trigger total update after a small delay to avoid constant updates while typing
                            holder.editTextPrice.postDelayed(() -> {
                                ((MainActivity) context).updateTotalOnly();
                            }, 500); // 500ms delay
                        } catch (NumberFormatException e) {
                            item.setPrice(0.0);
                        }
                    }
                };
//                holder.editTextPrice.setOnFocusChangeListener((v, hasFocus) -> {
//                    if (!hasFocus) {
//                        ((MainActivity) context).loadItems(); // Now it's safe to reload
//                    }
//                });

                holder.editTextPrice.addTextChangedListener(watcher);
                holder.editTextPrice.setTag(watcher); // save tag to remove later
            } else {
                holder.editTextPrice.setVisibility(View.GONE);
            }
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
        EditText editTextPrice;

        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewItem);
            checkBoxBought = itemView.findViewById(R.id.checkBoxBought);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            editTextPrice = itemView.findViewById(R.id.editTextPrice);
        }
    }
}
