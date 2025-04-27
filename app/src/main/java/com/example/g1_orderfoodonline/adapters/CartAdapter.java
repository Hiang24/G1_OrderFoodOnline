package com.example.g1_orderfoodonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.models.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private CartItemListener listener;

    public interface CartItemListener {
        void onQuantityChanged(CartItem cartItem, int quantity);
        void onRemoveItem(CartItem cartItem);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, CartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.textViewFoodName.setText(cartItem.getFood().getName());
        holder.textViewPrice.setText(String.format("%,.0fđ", cartItem.getFood().getPrice()));
        holder.textViewQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.textViewSubtotal.setText(String.format("%,.0fđ", cartItem.getSubtotal()));

        // Sử dụng imageResource trực tiếp
        holder.imageViewFood.setImageResource(cartItem.getFood().getImageResource());

        holder.buttonMinus.setOnClickListener(v -> {
            int quantity = cartItem.getQuantity();
            if (quantity > 1) {
                listener.onQuantityChanged(cartItem, quantity - 1);
            }
        });

        holder.buttonPlus.setOnClickListener(v -> {
            int quantity = cartItem.getQuantity();
            listener.onQuantityChanged(cartItem, quantity + 1);
        });

        holder.buttonRemove.setOnClickListener(v -> {
            listener.onRemoveItem(cartItem);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewFood;
        TextView textViewFoodName, textViewPrice, textViewQuantity, textViewSubtotal;
        TextView buttonMinus, buttonPlus;
        ImageButton buttonRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFood = itemView.findViewById(R.id.imageViewFood);
            textViewFoodName = itemView.findViewById(R.id.textViewFoodName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewSubtotal = itemView.findViewById(R.id.textViewSubtotal);
            buttonMinus = itemView.findViewById(R.id.buttonMinus);
            buttonPlus = itemView.findViewById(R.id.buttonPlus);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }
}
