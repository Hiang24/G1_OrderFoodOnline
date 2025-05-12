package com.example.g1_orderfoodonline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.adapters.OrderSummaryAdapter;
import com.example.g1_orderfoodonline.models.Order;
import com.example.g1_orderfoodonline.utils.LogUtils;
import com.example.g1_orderfoodonline.utils.OrderDatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class OrderTrackingActivity extends AppCompatActivity {

    private static final String TAG = "OrderTrackingActivity";

    private RecyclerView recyclerViewOrderItems;
    private Toolbar toolbar;
    private TextView textViewOrderId, textViewOrderDate;
    private TextView textViewDeliveryName, textViewDeliveryPhone, textViewDeliveryAddress;
    private TextView textViewSubtotal, textViewDeliveryFee, textViewTotal;

    private BottomNavigationView bottomNavigationView;

    private Order order;
    private String orderId;
    private String sourceActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        try {
            // Lấy order_id từ intent
            orderId = getIntent().getStringExtra("order_id");
            if (orderId == null) {
                LogUtils.error(TAG, "Order ID is null");
                Toast.makeText(this, "Không tìm thấy thông tin đơn hàng", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Xác định activity nguồn (nếu có)
            sourceActivity = getIntent().getStringExtra("source");
            if (sourceActivity == null) {
                // Mặc định là từ OrderHistoryActivity nếu không có thông tin
                sourceActivity = "OrderHistoryActivity";
            }

            LogUtils.debug(TAG, "Order ID: " + orderId + ", Source: " + sourceActivity);

            initViews();
            setupToolbar();
            setupBottomNavigation();

            // Lấy thông tin đơn hàng từ Firebase
            OrderDatabaseHelper.getInstance().getOrder(orderId, new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    order = snapshot.getValue(Order.class);
                    if (order == null) {
                        LogUtils.error(TAG, "Order not found for ID: " + orderId);
                        Toast.makeText(OrderTrackingActivity.this, "Không tìm thấy thông tin đơn hàng", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    displayOrderInfo();
                    setupRecyclerView();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    LogUtils.error(TAG, "Error loading order from Firebase", error.toException());
                    Toast.makeText(OrderTrackingActivity.this, "Lỗi khi tải đơn hàng", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

        } catch (Exception e) {
            LogUtils.error(TAG, "Error in onCreate", e);
            Toast.makeText(this, "Đã xảy ra lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        try {
            recyclerViewOrderItems = findViewById(R.id.recyclerViewOrderItems);
            toolbar = findViewById(R.id.toolbar);

            bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_menu);

            textViewOrderId = findViewById(R.id.textViewOrderId);
            textViewOrderDate = findViewById(R.id.textViewOrderDate);

            textViewDeliveryName = findViewById(R.id.textViewDeliveryName);
            textViewDeliveryPhone = findViewById(R.id.textViewDeliveryPhone);
            textViewDeliveryAddress = findViewById(R.id.textViewDeliveryAddress);

            textViewSubtotal = findViewById(R.id.textViewSubtotal);
            textViewDeliveryFee = findViewById(R.id.textViewDeliveryFee);
            textViewTotal = findViewById(R.id.textViewTotal);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error initializing views", e);
            throw e;
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

    private void navigateBack() {
        try {
            if ("CheckoutActivity".equals(sourceActivity)) {
                // Nếu từ CheckoutActivity, quay về CartActivity
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragment", "cart");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if ("OrderHistoryActivity".equals(sourceActivity)) {
                // Nếu từ OrderHistoryActivity, quay về OrderHistoryActivity
                Intent intent = new Intent(this, OrderHistoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                // Mặc định quay về MainActivity với ProfileFragment
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragment", "profile");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            finish();
        } catch (Exception e) {
            LogUtils.error(TAG, "Error navigating back", e);
            finish(); // Đảm bảo activity được đóng ngay cả khi có lỗi
        }
    }

    private void displayOrderInfo() {
        try {
            // Hiển thị thông tin đơn hàng
            textViewOrderId.setText("Mã đơn hàng: #" + order.getId());
            textViewOrderDate.setText("Ngày đặt: " + order.getOrderDate());

            // Hiển thị thông tin giao hàng
            textViewDeliveryName.setText(order.getCustomerName());
            textViewDeliveryPhone.setText(order.getCustomerPhone());
            textViewDeliveryAddress.setText(order.getCustomerAddress());

            // Hiển thị thông tin thanh toán
            textViewSubtotal.setText(String.format("%,.0fđ", order.getSubtotal()));
            textViewDeliveryFee.setText(String.format("%,.0fđ", order.getDeliveryFee()));
            textViewTotal.setText(String.format("%,.0fđ", order.getTotal()));
        } catch (Exception e) {
            LogUtils.error(TAG, "Error displaying order info", e);
        }
    }

    private void setupRecyclerView() {
        try {
            if (order.getItems() != null) {
                OrderSummaryAdapter adapter = new OrderSummaryAdapter(this, order.getItems());
                recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewOrderItems.setAdapter(adapter);
            } else {
                LogUtils.error(TAG, "Order items is null");
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up recycler view", e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigateBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("fragment", "cart");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error handling back press", e);
            super.onBackPressed();
        }
    }
}
