package com.example.g1_orderfoodonline.utils;

import com.example.g1_orderfoodonline.models.DeliveryAddress;

import java.util.ArrayList;
import java.util.List;

public class AddressManager {
    private static AddressManager instance;
    private List<DeliveryAddress> addresses;
    private static final String TAG = "AddressManager";

    private AddressManager() {
        addresses = new ArrayList<>();
        // Thêm một số địa chỉ mẫu
        addSampleAddresses();
    }

    public static synchronized AddressManager getInstance() {
        if (instance == null) {
            instance = new AddressManager();
        }
        return instance;
    }

    private void addSampleAddresses() {
        // Thêm một số địa chỉ mẫu
        addresses.add(new DeliveryAddress(1, "Nguyễn Văn A", "0901234567", "123 Nguyễn Văn Linh", "Quận 7", "TP. Hồ Chí Minh", true));
        addresses.add(new DeliveryAddress(2, "Nguyễn Văn A", "0901234567", "456 Lê Văn Việt", "Quận 9", "TP. Hồ Chí Minh", false));
        addresses.add(new DeliveryAddress(3, "Nguyễn Văn A", "0901234567", "789 Võ Văn Ngân", "Thủ Đức", "TP. Hồ Chí Minh", false));
    }

    public List<DeliveryAddress> getAddresses() {
        return addresses;
    }

    public DeliveryAddress getDefaultAddress() {
        for (DeliveryAddress address : addresses) {
            if (address.isDefault()) {
                return address;
            }
        }
        return addresses.isEmpty() ? null : addresses.get(0);
    }

    public void addAddress(DeliveryAddress address) {
        try {
            // Nếu địa chỉ mới là mặc định, hủy mặc định của các địa chỉ khác
            if (address.isDefault()) {
                for (DeliveryAddress existingAddress : addresses) {
                    existingAddress.setDefault(false);
                }
            }
            
            // Nếu là địa chỉ đầu tiên, đặt làm mặc định
            if (addresses.isEmpty()) {
                address.setDefault(true);
            }
            
            // Tạo ID mới
            int newId = addresses.isEmpty() ? 1 : addresses.get(addresses.size() - 1).getId() + 1;
            address.setId(newId);
            
            addresses.add(address);
            LogUtils.debug(TAG, "Address added: " + address.getId());
        } catch (Exception e) {
            LogUtils.error(TAG, "Error adding address", e);
        }
    }

    public void updateAddress(DeliveryAddress updatedAddress) {
        try {
            // Nếu địa chỉ cập nhật là mặc định, hủy mặc định của các địa chỉ khác
            if (updatedAddress.isDefault()) {
                for (DeliveryAddress address : addresses) {
                    if (address.getId() != updatedAddress.getId()) {
                        address.setDefault(false);
                    }
                }
            }
            
            // Cập nhật địa chỉ
            for (int i = 0; i < addresses.size(); i++) {
                if (addresses.get(i).getId() == updatedAddress.getId()) {
                    addresses.set(i, updatedAddress);
                    LogUtils.debug(TAG, "Address updated: " + updatedAddress.getId());
                    return;
                }
            }
            
            LogUtils.error(TAG, "Address not found for update: " + updatedAddress.getId());
        } catch (Exception e) {
            LogUtils.error(TAG, "Error updating address", e);
        }
    }

    public void deleteAddress(int addressId) {
        try {
            boolean wasDefault = false;
            
            // Xóa địa chỉ
            for (int i = 0; i < addresses.size(); i++) {
                if (addresses.get(i).getId() == addressId) {
                    wasDefault = addresses.get(i).isDefault();
                    addresses.remove(i);
                    LogUtils.debug(TAG, "Address deleted: " + addressId);
                    break;
                }
            }
            
            // Nếu địa chỉ bị xóa là mặc định và còn địa chỉ khác, đặt địa chỉ đầu tiên làm mặc định
            if (wasDefault && !addresses.isEmpty()) {
                addresses.get(0).setDefault(true);
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error deleting address", e);
        }
    }

    public DeliveryAddress getAddressById(int addressId) {
        try {
            for (DeliveryAddress address : addresses) {
                if (address.getId() == addressId) {
                    return address;
                }
            }
            LogUtils.error(TAG, "Address not found for ID: " + addressId);
            return null;
        } catch (Exception e) {
            LogUtils.error(TAG, "Error getting address by ID", e);
            return null;
        }
    }

    public void setDefaultAddress(int addressId) {
        try {
            for (DeliveryAddress address : addresses) {
                address.setDefault(address.getId() == addressId);
            }
            LogUtils.debug(TAG, "Default address set to: " + addressId);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting default address", e);
        }
    }
}
