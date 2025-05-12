package com.example.g1_orderfoodonline.utils;

import com.example.g1_orderfoodonline.models.DeliveryAddress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class AddressDatabaseHelper {
    private final DatabaseReference addressesRef;
    private final String userId;

    public AddressDatabaseHelper() {
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        addressesRef = FirebaseDatabase.getInstance().getReference("addresses").child(userId);
    }

    // Thêm địa chỉ mới
    public void addAddress(DeliveryAddress address, DatabaseReference.CompletionListener listener) {
        String addressId = addressesRef.push().getKey();
        address.setId(addressId);
        address.setCreatedAt(System.currentTimeMillis());
        addressesRef.child(addressId).setValue(address, listener);
    }

    // Lấy danh sách địa chỉ
    public void getAddresses(ValueEventListener listener) {
        addressesRef.addValueEventListener(listener);
    }

    // Cập nhật địa chỉ
    public void updateAddress(DeliveryAddress address, DatabaseReference.CompletionListener listener) {
        addressesRef.child(address.getId()).setValue(address, listener);
    }

    // Xóa địa chỉ
    public void deleteAddress(String addressId, DatabaseReference.CompletionListener listener) {
        addressesRef.child(addressId).removeValue(listener);
    }

    // Đặt địa chỉ mặc định
    public void setDefaultAddress(String addressId, DatabaseReference.CompletionListener listener) {
        addressesRef.get().addOnSuccessListener(snapshot -> {
            for (DataSnapshot child : snapshot.getChildren()) {
                addressesRef.child(child.getKey()).child("isDefault").setValue(false);
            }
            addressesRef.child(addressId).child("isDefault").setValue(true, listener);
        });
    }

    // Lấy địa chỉ mặc định
    public void getDefaultAddress(ValueEventListener listener) {
        addressesRef.orderByChild("default").equalTo(true).limitToFirst(1).addListenerForSingleValueEvent(listener);
    }

    // Lấy địa chỉ theo id
    public void getAddressById(String addressId, ValueEventListener listener) {
        addressesRef.child(addressId).addListenerForSingleValueEvent(listener);
    }
} 