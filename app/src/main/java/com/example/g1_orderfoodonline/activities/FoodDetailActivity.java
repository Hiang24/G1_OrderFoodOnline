package com.example.g1_orderfoodonline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.fragments.CartFragment;
import com.example.g1_orderfoodonline.fragments.HomeFragment;
import com.example.g1_orderfoodonline.fragments.MenuFragment;
import com.example.g1_orderfoodonline.fragments.ProfileFragment;
import com.example.g1_orderfoodonline.models.CartItem;
import com.example.g1_orderfoodonline.models.Food;
import com.example.g1_orderfoodonline.utils.CartManager;
import com.example.g1_orderfoodonline.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FoodDetailActivity extends AppCompatActivity {

    private static final String TAG = "FoodDetailActivity";

    private ImageView imageViewFood;
    private TextView textViewFoodName, textViewPrice, textViewDescription, textViewQuantity, textViewRating;
    private RatingBar ratingBar;
    private Button buttonMinus, buttonPlus, buttonAddToCart;
    private Toolbar toolbar;
    private ImageView backButton;
    private BottomNavigationView bottomNavigationView;

    private Food food;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        LogUtils.debug(TAG, "onCreate started");

        try {
            // Lấy thông tin món ăn từ intent
            if (getIntent() != null) {
                // Tạo đối tượng Food từ các thông tin được truyền qua intent
                int id = getIntent().getIntExtra("food_id", 0);
                String name = getIntent().getStringExtra("food_name");
                String description = getIntent().getStringExtra("food_description");
                double price = getIntent().getDoubleExtra("food_price", 0);
                float rating = getIntent().getFloatExtra("food_rating", 0);
                int imageResource = getIntent().getIntExtra("food_image", 0);
                String category = getIntent().getStringExtra("food_category");

                food = new Food(id, name, description, price, rating, imageResource, category);

                LogUtils.debug(TAG, "Food object created: " + (food != null ? food.getName() : "null"));
            } else {
                LogUtils.error(TAG, "Intent is null");
                Toast.makeText(this, "Lỗi: Không tìm thấy thông tin món ăn", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            if (food == null) {
                LogUtils.error(TAG, "Food object is null");
                Toast.makeText(this, "Lỗi: Không tìm thấy thông tin món ăn", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            initViews();
            setupToolbar();
            setupBottomNavigation();
            displayFoodDetails();
            setupQuantityButtons();
            setupAddToCartButton();

        } catch (Exception e) {
            LogUtils.error(TAG, "Error in onCreate", e);
            Toast.makeText(this, "Đã xảy ra lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        try {
            imageViewFood = findViewById(R.id.imageViewFood);
            textViewFoodName = findViewById(R.id.textViewFoodName);
            textViewPrice = findViewById(R.id.textViewPrice);
            textViewDescription = findViewById(R.id.textViewDescription);
            textViewQuantity = findViewById(R.id.textViewQuantity);
            textViewRating = findViewById(R.id.textViewRating);
            ratingBar = findViewById(R.id.ratingBar);
            buttonMinus = findViewById(R.id.buttonMinus);
            buttonPlus = findViewById(R.id.buttonPlus);
            buttonAddToCart = findViewById(R.id.buttonAddToCart);
            toolbar = findViewById(R.id.toolbar);
            backButton = findViewById(R.id.backButton);
            bottomNavigationView = findViewById(R.id.bottom_navigation);

            LogUtils.debug(TAG, "Views initialized successfully");
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
            backButton.setOnClickListener(v -> {
                finish(); // Sử dụng finish() thay vì onBackPressed() để đảm bảo hoạt động đúng
            });

            LogUtils.debug(TAG, "Toolbar setup successfully");
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

    private void displayFoodDetails() {
        try {
            textViewFoodName.setText(food.getName());
            textViewPrice.setText(String.format("%,.0fđ", food.getPrice()));
            textViewDescription.setText(food.getDescription());
            textViewRating.setText(String.valueOf(food.getRating()));
            ratingBar.setRating(food.getRating());

            // Sử dụng imageResource trực tiếp
            imageViewFood.setImageResource(food.getImageResource());

            LogUtils.debug(TAG, "Food details displayed successfully");
        } catch (Exception e) {
            LogUtils.error(TAG, "Error displaying food details", e);
        }
    }

    private void setupQuantityButtons() {
        try {
            buttonMinus.setOnClickListener(v -> {
                if (quantity > 1) {
                    quantity--;
                    textViewQuantity.setText(String.valueOf(quantity));
                }
            });

            buttonPlus.setOnClickListener(v -> {
                quantity++;
                textViewQuantity.setText(String.valueOf(quantity));
            });

            LogUtils.debug(TAG, "Quantity buttons setup successfully");
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up quantity buttons", e);
        }
    }

    private void setupAddToCartButton() {
        try {
            buttonAddToCart.setOnClickListener(v -> {
                CartItem cartItem = new CartItem(food, quantity);
                CartManager.getInstance().addToCart(cartItem);
                Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();

                // Chuyển đến giỏ hàng sau khi thêm
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragment", "cart");
                startActivity(intent);
                finish();
            });

            LogUtils.debug(TAG, "Add to cart button setup successfully");
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up add to cart button", e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
