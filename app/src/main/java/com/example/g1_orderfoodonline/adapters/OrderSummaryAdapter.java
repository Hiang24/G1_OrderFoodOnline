package com.example.g1_orderfoodonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.models.CartItem;

import java.util.List;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.OrderSummaryViewHolder> {

    private Context context;
    private List<CartItem> cartItems;

    public OrderSummaryAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public OrderSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_summary, parent, false);
        return new OrderSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSummaryViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        
        holder.textViewQuantity.setText(String.format("%dx", cartItem.getQuantity()));
        holder.textViewFoodName.setText(cartItem.getFood().getName());
        holder.textViewPrice.setText(String.format("%,.0fđ", cartItem.getSubtotal()));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class OrderSummaryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuantity, textViewFoodName, textViewPrice;

        public OrderSummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewFoodName = itemView.findViewById(R.id.textViewFoodName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
        }
    }
}

