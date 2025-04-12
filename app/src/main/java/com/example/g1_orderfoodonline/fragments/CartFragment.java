package com.example.g1_orderfoodonline.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.activities.CheckoutActivity;
import com.example.g1_orderfoodonline.adapters.CartAdapter;
import com.example.g1_orderfoodonline.models.CartItem;
import com.example.g1_orderfoodonline.utils.CartManager;
import com.example.g1_orderfoodonline.utils.LogUtils;

import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.CartItemListener {

    private static final String TAG = "CartFragment";

    private RecyclerView recyclerViewCart;
    private TextView textViewTotal, textViewEmptyCart;
    private Button buttonCheckout;

    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        try {
            initViews(view);
            setupRecyclerView();
            updateUI();
            setupCheckoutButton();
        } catch (Exception e) {
            LogUtils.error(TAG, "Error in onCreateView", e);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh cart data when fragment becomes visible
        try {
            setupRecyclerView();
            updateUI();
        } catch (Exception e) {
            LogUtils.error(TAG, "Error in onResume", e);
        }
    }

    private void initViews(View view) {
        try {
            recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
            textViewTotal = view.findViewById(R.id.textViewTotal);
            textViewEmptyCart = view.findViewById(R.id.textViewEmptyCart);
            buttonCheckout = view.findViewById(R.id.buttonCheckout);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error initializing views", e);
        }
    }

    private void setupRecyclerView() {
        try {
            cartItems = CartManager.getInstance().getCartItems();
            cartAdapter = new CartAdapter(getContext(), cartItems, this);
            recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewCart.setAdapter(cartAdapter);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up recycler view", e);
        }
    }

    private void updateUI() {
        try {
            if (cartItems.isEmpty()) {
                recyclerViewCart.setVisibility(View.GONE);
                textViewEmptyCart.setVisibility(View.VISIBLE);
                buttonCheckout.setEnabled(false);
            } else {
                recyclerViewCart.setVisibility(View.VISIBLE);
                textViewEmptyCart.setVisibility(View.GONE);
                buttonCheckout.setEnabled(true);
                textViewTotal.setText(String.format("%,.0fđ", CartManager.getInstance().getTotal()));
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error updating UI", e);
        }
    }

    private void setupCheckoutButton() {
        try {
            buttonCheckout.setOnClickListener(v -> {
                if (!cartItems.isEmpty()) {
                    Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up checkout button", e);
        }
    }

    @Override
    public void onQuantityChanged(CartItem cartItem, int quantity) {
        try {
            CartManager.getInstance().updateCartItemQuantity(cartItem, quantity);
            cartAdapter.notifyDataSetChanged();
            textViewTotal.setText(String.format("%,.0fđ", CartManager.getInstance().getTotal()));
        } catch (Exception e) {
            LogUtils.error(TAG, "Error changing quantity", e);
        }
    }

    @Override
    public void onRemoveItem(CartItem cartItem) {
        try {
            CartManager.getInstance().removeFromCart(cartItem);
            cartAdapter.notifyDataSetChanged();
            updateUI();
        } catch (Exception e) {
            LogUtils.error(TAG, "Error removing item", e);
        }
    }
}

