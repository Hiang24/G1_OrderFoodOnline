package com.example.g1_orderfoodonline.models;

import java.io.Serializable;

public class DeliveryAddress implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String address;
    private String district;
    private String city;
    private boolean isDefault;
    private long createdAt;

    public DeliveryAddress() {}

    public DeliveryAddress(String id, String name, String phone, String address, String district, String city, boolean isDefault, long createdAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.district = district;
        this.city = city;
        this.isDefault = isDefault;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getFullAddress() {
        return address + ", " + district + ", " + city;
    }
}
