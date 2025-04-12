package com.example.g1_orderfoodonline.utils;

import com.example.g1_orderfoodonline.models.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderManager {
    private static OrderManager instance;
    private Map<String, Order> orders;
    private static final String TAG = "OrderManager";

    private OrderManager() {
        orders = new HashMap<>();
    }

    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public void addOrder(Order order) {
        try {
            if (order != null && order.getId() != null) {
                orders.put(order.getId(), order);
                LogUtils.debug(TAG, "Order added: " + order.getId());
            } else {
                LogUtils.error(TAG, "Cannot add null order or order with null ID");
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error adding order", e);
        }
    }

    public Order getOrder(String orderId) {
        try {
            if (orderId != null) {
                Order order = orders.get(orderId);
                if (order == null) {
                    LogUtils.error(TAG, "Order not found for ID: " + orderId);
                } else {
                    LogUtils.debug(TAG, "Order found for ID: " + orderId);
                }
                return order;
            } else {
                LogUtils.error(TAG, "Order ID is null");
                return null;
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error getting order", e);
            return null;
        }
    }

    public List<Order> getAllOrders() {
        try {
            return new ArrayList<>(orders.values());
        } catch (Exception e) {
            LogUtils.error(TAG, "Error getting all orders", e);
            return new ArrayList<>();
        }
    }

    public void updateOrderStatus(String orderId, String status) {
        try {
            Order order = orders.get(orderId);
            if (order != null) {
                order.setStatus(status);

                // Cập nhật thời gian cho trạng thái
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String currentTime = sdf.format(new Date());

                switch (status) {
                    case "Đã xác nhận":
                        order.setConfirmedTime(currentTime);
                        break;
                    case "Đang chuẩn bị":
                        order.setProcessingTime(currentTime);
                        break;
                    case "Đang giao hàng":
                        order.setOnTheWayTime(currentTime);
                        break;
                    case "Đã giao hàng":
                        order.setDeliveredTime(currentTime);
                        break;
                }

                LogUtils.debug(TAG, "Order status updated: " + orderId + " -> " + status);
            } else {
                LogUtils.error(TAG, "Cannot update status for non-existent order: " + orderId);
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error updating order status", e);
        }
    }
}

