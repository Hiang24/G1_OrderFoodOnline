package com.example.g1_orderfoodonline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
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
import com.example.g1_orderfoodonline.utils.OrderManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrderTrackingActivity extends AppCompatActivity {

    private static final String TAG = "OrderTrackingActivity";
    private static final int STATUS_UPDATE_DELAY = 30000; // 30 giây

    private RecyclerView recyclerViewOrderItems;
    private Toolbar toolbar;
    private TextView textViewOrderId, textViewOrderDate;
    private TextView textViewDeliveryName, textViewDeliveryPhone, textViewDeliveryAddress;
    private TextView textViewSubtotal, textViewDeliveryFee, textViewTotal;
    private TextView textViewConfirmedTime, textViewProcessingTime, textViewOnTheWayTime, textViewDeliveredTime;
    private ImageView imageViewConfirmed, imageViewProcessing, imageViewOnTheWay, imageViewDelivered;
    private ImageView backButton;
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

            // Lấy thông tin đơn hàng
            order = OrderManager.getInstance().getOrder(orderId);
            if (order == null) {
                LogUtils.error(TAG, "Order not found for ID: " + orderId);
                Toast.makeText(this, "Không tìm thấy thông tin đơn hàng", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            initViews();
            setupToolbar();
            setupBottomNavigation();
            displayOrderInfo();
            setupRecyclerView();
            updateOrderStatus();

            // Mô phỏng cập nhật trạng thái đơn hàng (trong thực tế sẽ được cập nhật từ server)
            simulateOrderProgress();

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
            backButton = findViewById(R.id.backButton);
            bottomNavigationView = findViewById(R.id.bottom_navigation);

            textViewOrderId = findViewById(R.id.textViewOrderId);
            textViewOrderDate = findViewById(R.id.textViewOrderDate);

            textViewDeliveryName = findViewById(R.id.textViewDeliveryName);
            textViewDeliveryPhone = findViewById(R.id.textViewDeliveryPhone);
            textViewDeliveryAddress = findViewById(R.id.textViewDeliveryAddress);

            textViewSubtotal = findViewById(R.id.textViewSubtotal);
            textViewDeliveryFee = findViewById(R.id.textViewDeliveryFee);
            textViewTotal = findViewById(R.id.textViewTotal);

            textViewConfirmedTime = findViewById(R.id.textViewConfirmedTime);
            textViewProcessingTime = findViewById(R.id.textViewProcessingTime);
            textViewOnTheWayTime = findViewById(R.id.textViewOnTheWayTime);
            textViewDeliveredTime = findViewById(R.id.textViewDeliveredTime);

            imageViewConfirmed = findViewById(R.id.imageViewConfirmed);
            imageViewProcessing = findViewById(R.id.imageViewProcessing);
            imageViewOnTheWay = findViewById(R.id.imageViewOnTheWay);
            imageViewDelivered = findViewById(R.id.imageViewDelivered);
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

            // Thiết lập nút quay lại
            if (backButton != null) {
                backButton.setOnClickListener(v -> {
                    navigateBack();
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

    // Phương thức mới để quay lại màn hình trước đó
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

    private void updateOrderStatus() {
        try {
            // Hiển thị thời gian cho từng trạng thái
            textViewConfirmedTime.setText(order.getConfirmedTime() != null ? order.getConfirmedTime() : "--:--");
            textViewProcessingTime.setText(order.getProcessingTime() != null ? order.getProcessingTime() : "--:--");
            textViewOnTheWayTime.setText(order.getOnTheWayTime() != null ? order.getOnTheWayTime() : "--:--");
            textViewDeliveredTime.setText(order.getDeliveredTime() != null ? order.getDeliveredTime() : "--:--");

            // Cập nhật trạng thái hiển thị
            String status = order.getStatus();

            // Mặc định tất cả đều là chưa hoàn thành
            imageViewConfirmed.setImageResource(R.drawable.ic_circle);
            imageViewProcessing.setImageResource(R.drawable.ic_circle);
            imageViewOnTheWay.setImageResource(R.drawable.ic_circle);
            imageViewDelivered.setImageResource(R.drawable.ic_circle);

            // Cập nhật theo trạng thái hiện tại
            if (status.equals("Đã xác nhận") || status.equals("Đang chuẩn bị") ||
                    status.equals("Đang giao hàng") || status.equals("Đã giao hàng")) {
                imageViewConfirmed.setImageResource(R.drawable.ic_check_circle);
                imageViewConfirmed.setColorFilter(getResources().getColor(R.color.green));
            }

            if (status.equals("Đang chuẩn bị") || status.equals("Đang giao hàng") ||
                    status.equals("Đã giao hàng")) {
                imageViewProcessing.setImageResource(R.drawable.ic_check_circle);
                imageViewProcessing.setColorFilter(getResources().getColor(R.color.green));
            }

            if (status.equals("Đang giao hàng") || status.equals("Đã giao hàng")) {
                imageViewOnTheWay.setImageResource(R.drawable.ic_check_circle);
                imageViewOnTheWay.setColorFilter(getResources().getColor(R.color.green));
            }

            if (status.equals("Đã giao hàng")) {
                imageViewDelivered.setImageResource(R.drawable.ic_check_circle);
                imageViewDelivered.setColorFilter(getResources().getColor(R.color.green));
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error updating order status", e);
        }
    }

    private void simulateOrderProgress() {
        try {
            // Mô phỏng cập nhật trạng thái đơn hàng
            // Trong thực tế, trạng thái sẽ được cập nhật từ server

            // Sau 30 giây, cập nhật trạng thái "Đang chuẩn bị"
            new android.os.Handler().postDelayed(() -> {
                try {
                    OrderManager.getInstance().updateOrderStatus(order.getId(), "Đang chuẩn bị");

                    order = OrderManager.getInstance().getOrder(order.getId());
                    if (order != null) {
                        updateOrderStatus();
                        Toast.makeText(OrderTrackingActivity.this, "Đơn hàng đang được chuẩn bị", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    LogUtils.error(TAG, "Error updating to processing status", e);
                }
            }, STATUS_UPDATE_DELAY);

            // Sau 60 giây, cập nhật trạng thái "Đang giao hàng"
            new android.os.Handler().postDelayed(() -> {
                try {
                    OrderManager.getInstance().updateOrderStatus(order.getId(), "Đang giao hàng");
                    order = OrderManager.getInstance().getOrder(order.getId());
                    if (order != null) {
                        updateOrderStatus();
                        Toast.makeText(OrderTrackingActivity.this, "Đơn hàng đang được giao", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    LogUtils.error(TAG, "Error updating to on the way status", e);
                }
            }, STATUS_UPDATE_DELAY * 2);

            // Sau 90 giây, cập nhật trạng thái "Đã giao hàng"
            new android.os.Handler().postDelayed(() -> {
                try {
                    OrderManager.getInstance().updateOrderStatus(order.getId(), "Đã giao hàng");
                    order = OrderManager.getInstance().getOrder(order.getId());
                    if (order != null) {
                        updateOrderStatus();
                        Toast.makeText(OrderTrackingActivity.this, "Đơn hàng đã được giao thành công", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    LogUtils.error(TAG, "Error updating to delivered status", e);
                }
            }, STATUS_UPDATE_DELAY * 3);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error simulating order progress", e);
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
        navigateBack();
    }
}
