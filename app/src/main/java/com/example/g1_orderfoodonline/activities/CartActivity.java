package com.example.g1_orderfoodonline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.adapters.CartAdapter;
import com.example.g1_orderfoodonline.models.CartItem;
import com.example.g1_orderfoodonline.utils.CartManager;
import com.example.g1_orderfoodonline.utils.LogUtils;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartItemListener {

    private static final String TAG = "CartActivity";

    private RecyclerView recyclerViewCart;
    private TextView textViewTotal, textViewEmptyCart;
    private Button buttonCheckout;
    private Toolbar toolbar;

    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        try {
            initViews();
            setupToolbar();
            setupRecyclerView();
            updateUI();
            setupCheckoutButton();
        } catch (Exception e) {
            LogUtils.error(TAG, "Error in onCreate", e);
        }
    }

    private void initViews() {
        try {
            recyclerViewCart = findViewById(R.id.recyclerViewCart);
            textViewTotal = findViewById(R.id.textViewTotal);
            textViewEmptyCart = findViewById(R.id.textViewEmptyCart);
            buttonCheckout = findViewById(R.id.buttonCheckout);
            toolbar = findViewById(R.id.toolbar);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error initializing views", e);
        }
    }

    private void setupToolbar() {
        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up toolbar", e);
        }
    }

    private void setupRecyclerView() {
        try {
            cartItems = CartManager.getInstance().getCartItems();
            cartAdapter = new CartAdapter(this, cartItems, this);
            recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
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
                try {
                    if (!cartItems.isEmpty()) {
                        Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    LogUtils.error(TAG, "Error navigating to checkout", e);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

