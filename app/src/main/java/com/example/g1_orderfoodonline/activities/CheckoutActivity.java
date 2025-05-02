package com.example.g1_orderfoodonline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.adapters.OrderSummaryAdapter;
import com.example.g1_orderfoodonline.fragments.AddressListFragment;
import com.example.g1_orderfoodonline.models.CartItem;
import com.example.g1_orderfoodonline.models.DeliveryAddress;
import com.example.g1_orderfoodonline.models.Order;
import com.example.g1_orderfoodonline.utils.AddressManager;
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

import androidx.annotation.Nullable;

public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG = "CheckoutActivity";
    private static final double DELIVERY_FEE = 15000;
    private static final int REQUEST_CODE_SELECT_ADDRESS = 1001;

    private CardView cardViewAddress;
    private TextView textViewName, textViewPhone, textViewAddress;
    private TextView textViewChangeAddress;
    private TextView textViewSubtotal, textViewDeliveryFee, textViewTotal, textViewTotalCheckout;
    private Button buttonPlaceOrder, buttonCancel;
    private RecyclerView recyclerViewOrderSummary;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    private List<CartItem> cartItems;
    private double subtotal, total;
    private DeliveryAddress selectedAddress;

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
        loadDefaultAddress();
        setupPlaceOrderButton();
        setupCancelButton();
    }

    private void initViews() {
        try {
            cardViewAddress = findViewById(R.id.cardViewAddress);
            textViewName = findViewById(R.id.textViewName);
            textViewPhone = findViewById(R.id.textViewPhone);
            textViewAddress = findViewById(R.id.textViewAddress);
            textViewChangeAddress = findViewById(R.id.textViewChangeAddress);
            textViewSubtotal = findViewById(R.id.textViewSubtotal);
            textViewDeliveryFee = findViewById(R.id.textViewDeliveryFee);
            textViewTotal = findViewById(R.id.textViewTotal);
            textViewTotalCheckout = findViewById(R.id.textViewTotalCheckout);
            buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder);
            buttonCancel = findViewById(R.id.buttonCancel);
            recyclerViewOrderSummary = findViewById(R.id.recyclerViewOrderSummary);
            toolbar = findViewById(R.id.toolbar);
            bottomNavigationView = findViewById(R.id.bottom_navigation);

            textViewChangeAddress.setOnClickListener(v -> {
                navigateToAddressList();
            });

            cardViewAddress.setOnClickListener(v -> {
                navigateToAddressList();
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error initializing views", e);
        }
    }

    private void navigateToAddressList() {
        Intent intent = new Intent(this, AddressListActivity.class);
        intent.putExtra("selection_mode", true);
        startActivityForResult(intent, REQUEST_CODE_SELECT_ADDRESS);
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

    private void navigateToCartActivity() {
        try {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("fragment", "cart");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            LogUtils.error(TAG, "Error navigating to cart", e);
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

    private void loadDefaultAddress() {
        try {
            selectedAddress = AddressManager.getInstance().getDefaultAddress();
            updateAddressUI();
        } catch (Exception e) {
            LogUtils.error(TAG, "Error loading default address", e);
        }
    }

    private void updateAddressUI() {
        if (selectedAddress != null) {
            textViewName.setText(selectedAddress.getName());
            textViewPhone.setText(selectedAddress.getPhone());
            textViewAddress.setText(selectedAddress.getFullAddress());
            textViewChangeAddress.setVisibility(View.VISIBLE);
        } else {
            textViewName.setText("Chưa có địa chỉ giao hàng");
            textViewPhone.setText("");
            textViewAddress.setText("Nhấn vào đây để thêm địa chỉ giao hàng");
            textViewChangeAddress.setVisibility(View.GONE);
        }
    }

    private void setupPlaceOrderButton() {
        try {
            buttonPlaceOrder.setOnClickListener(v -> {
                if (validateOrder()) {
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

    private boolean validateOrder() {
        if (selectedAddress == null) {
            Toast.makeText(this, "Vui lòng chọn địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void placeOrder() {
        try {
            // Tạo đơn hàng mới
            String orderId = UUID.randomUUID().toString().substring(0, 8);
            String customerName = selectedAddress.getName();
            String customerPhone = selectedAddress.getPhone();
            String customerAddress = selectedAddress.getFullAddress();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_ADDRESS && resultCode == RESULT_OK && data != null) {
            int selectedAddressId = data.getIntExtra("selected_address_id", -1);
            if (selectedAddressId != -1) {
                selectedAddress = AddressManager.getInstance().getAddressById(selectedAddressId);
                updateAddressUI();
            }
        }
    }
}
