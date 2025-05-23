package com.example.g1_orderfoodonline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.adapters.OrderHistoryAdapter;
import com.example.g1_orderfoodonline.models.Order;
import com.example.g1_orderfoodonline.utils.LogUtils;
import com.example.g1_orderfoodonline.utils.OrderDatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity implements OrderHistoryAdapter.OnOrderClickListener {

    private static final String TAG = "OrderHistoryActivity";

    private RecyclerView recyclerViewOrders;
    private TextView textViewNoOrders;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    private OrderHistoryAdapter adapter;
    private OrderDatabaseHelper orderDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        try {
            initViews();
            setupToolbar();
            setupBottomNavigation();
            setupRecyclerView();
            loadOrders();
        } catch (Exception e) {
            LogUtils.error(TAG, "Error in onCreate", e);
        }
    }

    private void initViews() {
        try {
            recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
            textViewNoOrders = findViewById(R.id.textViewNoOrders);
            toolbar = findViewById(R.id.toolbar);
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
            orderDatabaseHelper = OrderDatabaseHelper.getInstance();
        } catch (Exception e) {
            LogUtils.error(TAG, "Error initializing views", e);
        }
    }

    private void setupToolbar() {
        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }

        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up toolbar", e);
        }
    }

    private void setupBottomNavigation() {
        try {
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_menu) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("fragment", "menu");
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_cart) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("fragment", "cart");
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("fragment", "profile");
                    startActivity(intent);
                    finish();
                    return true;
                }

                return false;
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up bottom navigation", e);
        }
    }

    private void setupRecyclerView() {
        adapter = new OrderHistoryAdapter(this);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrders.setAdapter(adapter);
    }

    private void loadOrders() {
        try {
            orderDatabaseHelper.getOrders(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Order> orders = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Order order = snapshot.getValue(Order.class);
                        if (order != null) {
                            orders.add(order);
                        }
                    }

                    if (orders.isEmpty()) {
                        textViewNoOrders.setVisibility(View.VISIBLE);
                        recyclerViewOrders.setVisibility(View.GONE);
                    } else {
                        textViewNoOrders.setVisibility(View.GONE);
                        recyclerViewOrders.setVisibility(View.VISIBLE);
                        adapter.setOrders(orders);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    LogUtils.error(TAG, "Error loading orders", databaseError.toException());
                }
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error loading orders", e);
        }
    }

    @Override
    public void onOrderClick(Order order) {
        try {
            Intent intent = new Intent(this, OrderTrackingActivity.class);
            intent.putExtra("order_id", order.getId());
            intent.putExtra("source", "OrderHistoryActivity");
            startActivity(intent);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error navigating to order tracking", e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigateToProfileFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        navigateToProfileFragment();
    }

    private void navigateToProfileFragment() {
        try {
            // Quay về MainActivity và hiển thị ProfileFragment
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("fragment", "profile");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            LogUtils.error(TAG, "Error navigating to profile fragment", e);
            onBackPressed();
        }
    }
}
