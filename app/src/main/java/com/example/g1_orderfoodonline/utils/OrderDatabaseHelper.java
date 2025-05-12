package com.example.g1_orderfoodonline.utils;

import com.example.g1_orderfoodonline.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDatabaseHelper {
    private static final String TAG = "OrderDatabaseHelper";
    private static OrderDatabaseHelper instance;
    private final DatabaseReference ordersRef;
    private final String userId;

    private OrderDatabaseHelper() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");
    }

    public static synchronized OrderDatabaseHelper getInstance() {
        if (instance == null) {
            instance = new OrderDatabaseHelper();
        }
        return instance;
    }

    public void saveOrder(Order order, DatabaseReference.CompletionListener listener) {
        if (userId == null) {
            LogUtils.error(TAG, "User not logged in");
            if (listener != null) {
                listener.onComplete(DatabaseError.fromCode(DatabaseError.DISCONNECTED), null);
            }
            return;
        }

        String orderId = ordersRef.child(userId).push().getKey();
        order.setId(orderId);
        ordersRef.child(userId).child(orderId).setValue(order, listener);
    }

    public void getOrders(ValueEventListener listener) {
        if (userId == null) {
            LogUtils.error(TAG, "User not logged in");
            return;
        }

        ordersRef.child(userId).addValueEventListener(listener);
    }

    public void getOrder(String orderId, ValueEventListener listener) {
        if (userId == null) {
            LogUtils.error(TAG, "User not logged in");
            return;
        }

        ordersRef.child(userId).child(orderId).addValueEventListener(listener);
    }

    public void deleteOrder(String orderId, DatabaseReference.CompletionListener listener) {
        if (userId == null) {
            LogUtils.error(TAG, "User not logged in");
            if (listener != null) {
                listener.onComplete(DatabaseError.fromCode(DatabaseError.DISCONNECTED), null);
            }
            return;
        }

        ordersRef.child(userId).child(orderId).removeValue(listener);
    }
} 