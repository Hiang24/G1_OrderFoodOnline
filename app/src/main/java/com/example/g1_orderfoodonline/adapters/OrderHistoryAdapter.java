package com.example.g1_orderfoodonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.models.Order;
import com.example.g1_orderfoodonline.utils.LogUtils;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private static final String TAG = "OrderHistoryAdapter";
    
    private Context context;
    private List<Order> orders;
    private OrderClickListener listener;

    public OrderHistoryAdapter(Context context, List<Order> orders) {
    }

    public interface OrderClickListener {
        void onOrderClick(Order order);
    }
    
    public OrderHistoryAdapter(Context context, List<Order> orders, OrderClickListener listener) {
        this.context = context;
        this.orders = orders;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
            return new OrderViewHolder(view);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error creating view holder", e);
            throw e;
        }
    }
    
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        try {
            Order order = orders.get(position);
            
            holder.textViewOrderId.setText("Mã đơn hàng: #" + order.getId());
            holder.textViewOrderDate.setText("Ngày đặt: " + order.getOrderDate());
            holder.textViewOrderStatus.setText("Trạng thái: " + order.getStatus());
            holder.textViewOrderTotal.setText(String.format("%,.0fđ", order.getTotal()));
            
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onOrderClick(order);
                }
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error binding view holder", e);
        }
    }
    
    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }
    
    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderId, textViewOrderDate, textViewOrderStatus, textViewOrderTotal;
        
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderId = itemView.findViewById(R.id.textViewOrderId);
            textViewOrderDate = itemView.findViewById(R.id.textViewOrderDate);
            textViewOrderStatus = itemView.findViewById(R.id.textViewOrderStatus);
            textViewOrderTotal = itemView.findViewById(R.id.textViewOrderTotal);
        }
    }
}
