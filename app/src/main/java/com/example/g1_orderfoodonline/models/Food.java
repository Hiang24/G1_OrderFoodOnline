package com.example.g1_orderfoodonline.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Food implements Serializable, Parcelable {
    private int id;
    private String name;
    private String description;
    private double price;
    private int imageResource; // Thay đổi từ String sang int
    private String category; // "food" or "drink"

    // Constructor mặc định cho Firebase
    public Food() {
    }

    public Food(int id, String name, String description, double price, int imageResource, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
        this.category = category;
    }

    // Constructor thứ hai với thứ tự tham số khác
    public Food(int id, String name, String description, double price, String category, int imageResource) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageResource = imageResource;
    }

    // Parcelable implementation
    protected Food(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        imageResource = in.readInt();
        category = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeInt(imageResource);
        dest.writeString(category);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
