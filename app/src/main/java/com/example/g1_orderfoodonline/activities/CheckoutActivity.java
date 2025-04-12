package com.example.g1_orderfoodonline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.adapters.OrderSummaryAdapter;
import com.example.g1_orderfoodonline.models.CartItem;
import com.example.g1_orderfoodonline.models.Order;
import com.example.g1_orderfoodonline.utils.CartManager;
import com.example.g1_orderfoodonline.utils.LogUtils;
import com.example.g1_orderfoodonline.utils.OrderManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG = "CheckoutActivity";
    private static final double DELIVERY_FEE = 15000;

    private EditText editTextName, editTextPhone, editTextAddress;
    private TextView textViewSubtotal, textViewDeliveryFee, textViewTotal, textViewTotalCheckout;
    private Button buttonPlaceOrder, buttonCancel;
    private RecyclerView recyclerViewOrderSummary;
    private Toolbar toolbar;
    private ImageView backButton;
    private BottomNavigationView bottomNavigationView;

    private List<CartItem> cartItems;
    private double subtotal, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();
        setupToolbar();
        setupBottomNavigation();
        setupRecyclerView();
        calculateTotals();
        updateUI();
        setupPlaceOrderButton();
        setupCancelButton();
    }

    private void initViews() {
        try {
            editTextName = findViewById(R.id.editTextName);
            editTextPhone = findViewById(R.id.editTextPhone);
            editTextAddress = findViewById(R.id.editTextAddress);
            textViewSubtotal = findViewById(R.id.textViewSubtotal);
            textViewDeliveryFee = findViewById(R.id.textViewDeliveryFee);
            textViewTotal = findViewById(R.id.textViewTotal);
            textViewTotalCheckout = findViewById(R.id.textViewTotalCheckout);
            buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder);
            buttonCancel = findViewById(R.id.buttonCancel);
            recyclerViewOrderSummary = findViewById(R.id.recyclerViewOrderSummary);
            toolbar = findViewById(R.id.toolbar);
            backButton = findViewById(R.id.backButton);
            bottomNavigationView = findViewById(R.id.bottom_navigation);
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

            // Thiết lập nút quay lại
            if (backButton != null) {
                backButton.setOnClickListener(v -> {
                    navigateToCartActivity();
                });
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

    private void navigateToCartActivity() {
        try {
            Intent intent = new Intent(this, CartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            LogUtils.error(TAG, "Error navigating to cart activity", e);
            onBackPressed();
        }
    }

    private void setupRecyclerView() {
        try {
            cartItems = new ArrayList<>(CartManager.getInstance().getCartItems());
            OrderSummaryAdapter adapter = new OrderSummaryAdapter(this, cartItems);
            recyclerViewOrderSummary.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewOrderSummary.setAdapter(adapter);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up recycler view", e);
        }
    }

    private void calculateTotals() {
        try {
            subtotal = CartManager.getInstance().getTotal();
            total = subtotal + DELIVERY_FEE;
        } catch (Exception e) {
            LogUtils.error(TAG, "Error calculating totals", e);
            subtotal = 0;
            total = DELIVERY_FEE;
        }
    }

    private void updateUI() {
        try {
            textViewSubtotal.setText(String.format("%,.0fđ", subtotal));
            textViewDeliveryFee.setText(String.format("%,.0fđ", DELIVERY_FEE));
            textViewTotal.setText(String.format("%,.0fđ", total));
            textViewTotalCheckout.setText(String.format("%,.0fđ", total));
        } catch (Exception e) {
            LogUtils.error(TAG, "Error updating UI", e);
        }
    }

    private void setupPlaceOrderButton() {
        try {
            buttonPlaceOrder.setOnClickListener(v -> {
                if (validateInputs()) {
                    placeOrder();
                }
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up place order button", e);
        }
    }

    private void setupCancelButton() {
        try {
            buttonCancel.setOnClickListener(v -> {
                navigateToCartActivity();
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up cancel button", e);
        }
    }

    private void placeOrder() {
        try {
            // Tạo đơn hàng mới
            String orderId = UUID.randomUUID().toString().substring(0, 8);
            String customerName = editTextName.getText().toString().trim();
            String customerPhone = editTextPhone.getText().toString().trim();
            String customerAddress = editTextAddress.getText().toString().trim();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String orderDate = sdf.format(new Date());

            // Tạo bản sao của danh sách cartItems để tránh lỗi ConcurrentModificationException
            List<CartItem> orderItems = new ArrayList<>(cartItems);

            Order order = new Order(
                    orderId,
                    customerName,
                    customerPhone,
                    customerAddress,
                    orderDate,
                    orderItems,
                    subtotal,
                    DELIVERY_FEE,
                    total,
                    "Đã xác nhận" // Trạng thái ban đầu
            );

            // Lưu đơn hàng
            OrderManager.getInstance().addOrder(order);

            // Hiển thị thông báo thành công
            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();

            // Chuyển đến màn hình theo dõi đơn hàng
            Intent intent = new Intent(CheckoutActivity.this, OrderTrackingActivity.class);
            intent.putExtra("order_id", orderId);
            intent.putExtra("source", "CheckoutActivity");
            startActivity(intent);

            // Xóa giỏ hàng
            CartManager.getInstance().clearCart();

            // Đóng màn hình hiện tại
            finish();
        } catch (Exception e) {
            LogUtils.error(TAG, "Error placing order", e);
            Toast.makeText(this, "Lỗi khi đặt hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs() {
        try {
            String name = editTextName.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();
            String address = editTextAddress.getText().toString().trim();

            if (name.isEmpty()) {
                editTextName.setError("Vui lòng nhập họ tên");
                return false;
            }

            if (phone.isEmpty()) {
                editTextPhone.setError("Vui lòng nhập số điện thoại");
                return false;
            }

            if (address.isEmpty()) {
                editTextAddress.setError("Vui lòng nhập địa chỉ giao hàng");
                return false;
            }

            return true;
        } catch (Exception e) {
            LogUtils.error(TAG, "Error validating inputs", e);
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigateToCartActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        navigateToCartActivity();
    }
}
