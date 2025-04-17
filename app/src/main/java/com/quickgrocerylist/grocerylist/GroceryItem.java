package com.quickgrocerylist.grocerylist;

public class GroceryItem {
    private int id;
    private String name;
    private String quantity;
    private boolean isBought;

    public GroceryItem() {}

    public GroceryItem(int id, String name, String quantity, boolean isBought) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.isBought = isBought;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public boolean isBought() { return isBought; }
    public void setBought(boolean bought) { isBought = bought; }
}