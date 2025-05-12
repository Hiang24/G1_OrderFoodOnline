package com.example.g1_orderfoodonline.models;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Order implements Serializable {
    private String id;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private String orderDate;
    private List<CartItem> items;
    private double subtotal;
    private double deliveryFee;
    private double total;

    // Constructor không tham số cho Firebase
    public Order() {
        // Khởi tạo các giá trị mặc định
        this.items = new ArrayList<>();
    }

    public Order(String id, String customerName, String customerPhone, String customerAddress, 
                 String orderDate, List<CartItem> items, double subtotal, double deliveryFee, 
                 double total) {
        this.id = id;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.orderDate = orderDate;
        this.items = items;
        this.subtotal = subtotal;
        this.deliveryFee = deliveryFee;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

