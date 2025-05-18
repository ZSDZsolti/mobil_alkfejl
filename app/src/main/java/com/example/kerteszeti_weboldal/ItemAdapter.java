package com.example.kerteszeti_weboldal;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, description, quantity;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemName);
            price = itemView.findViewById(R.id.itemPrice);
            description = itemView.findViewById(R.id.itemDescription);
            quantity = itemView.findViewById(R.id.itemQuantity);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item current = itemList.get(position);
        holder.name.setText(current.getName());
        String priceText = String.valueOf(current.getPrice()) + "Ft/db";
        holder.price.setText(priceText);
        holder.description.setText(current.getDescription());
        String quantityText = String.valueOf(current.getQuantity()) + " db készleten";
        holder.quantity.setText(quantityText);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<Item> items) {
        this.itemList = items;
        notifyDataSetChanged();
    }
}
