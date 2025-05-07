package com.example.g1_orderfoodonline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.adapters.FoodAdapter;
import com.example.g1_orderfoodonline.models.Food;
import com.example.g1_orderfoodonline.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements FoodAdapter.OnFoodClickListener {

    private ImageView ivBack;
    private EditText etSearch;
    private RecyclerView rvSearchResults;
    private TextView textViewNoResults;
    private FoodAdapter foodAdapter;
    private List<Food> allFoods;
    private BottomNavigationView bottomNavigationView;
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        setupRecyclerView();
        setupSearchListener();
        setupBackButton();
        setupBottomNavigation();

        // Kiểm tra nếu có query từ intent
        String query = getIntent().getStringExtra("query");
        if (query != null && !query.isEmpty()) {
            etSearch.setText(query);
            searchFood(query);
        }
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        etSearch = findViewById(R.id.et_search);
        rvSearchResults = findViewById(R.id.rv_search_results);
        textViewNoResults = findViewById(R.id.textViewNoResults);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setupRecyclerView() {
        allFoods = getAllFoods();
        foodAdapter = new FoodAdapter(this, new ArrayList<>(), FoodAdapter.VIEW_TYPE_LIST, this);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setAdapter(foodAdapter);
    }

    private void setupSearchListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchFood(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupBackButton() {
        ivBack.setOnClickListener(v -> finish());
    }

    private void setupBottomNavigation() {
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
    }

    private void searchFood(String query) {
        List<Food> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            // If search query is empty, show nothing
            foodAdapter.updateData(filteredList);
            textViewNoResults.setVisibility(View.GONE);
            return;
        }

        // Filter foods based on search query
        for (Food food : allFoods) {
            if (food.getName().toLowerCase().contains(query.toLowerCase()) ||
                    food.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(food);
            }
        }

        foodAdapter.updateData(filteredList);

        // Show "No results" message if no foods match the query
        if (filteredList.isEmpty()) {
            textViewNoResults.setVisibility(View.VISIBLE);
        } else {
            textViewNoResults.setVisibility(View.GONE);
        }
    }

    private List<Food> getAllFoods() {
        List<Food> foodList = new ArrayList<>();

        // Sử dụng R.drawable trực tiếp
        foodList.add(new Food(1, "Mỳ Cay Thập Cẩm", "Mỳ cay thập cẩm với nhiều loại hải sản tươi ngon, cay nồng đậm đà hương vị Hàn Quốc.", 75000, R.drawable.mi_kim_chi_hai_san, "food"));
        foodList.add(new Food(2, "Cơm Trộn Hàn Quốc", "Cơm trộn Hàn Quốc với rau củ tươi ngon, thịt bò xào và trứng ốp la.", 65000, R.drawable.mi_kim_chi_hai_san, "food"));
        foodList.add(new Food(3, "Trà Đào Cam Sả", "Trà đào thơm ngon với vị cam sả thanh mát.", 35000, R.drawable.mi_kim_chi_hai_san, "drink"));
        foodList.add(new Food(4, "Gà Rán Sốt Cay", "Gà rán giòn rụm với sốt cay đặc biệt.", 85000, R.drawable.mi_kim_chi_hai_san, "food"));
        foodList.add(new Food(5, "Bánh Mì Thịt Nướng", "Bánh mì giòn với thịt nướng thơm ngon và rau sống.", 30000, R.drawable.mi_kim_chi_hai_san, "food"));
        foodList.add(new Food(6, "Cà Phê Sữa Đá", "Cà phê đậm đà hòa quyện với sữa đặc.", 25000, R.drawable.mi_kim_chi_hai_san, "drink"));
        foodList.add(new Food(7, "Bún Bò Huế", "Bún bò Huế cay nồng với nhiều loại gia vị đặc trưng.", 55000, R.drawable.mi_kim_chi_hai_san, "food"));
        foodList.add(new Food(8, "Sinh Tố Xoài", "Sinh tố xoài ngọt mát với vị chua nhẹ.", 30000, R.drawable.mi_kim_chi_hai_san, "drink"));
        foodList.add(new Food(9, "Phở Bò", "Phở bò thơm ngon với nước dùng đậm đà và thịt bò mềm.", 60000, R.drawable.mi_kim_chi_hai_san, "food"));
        foodList.add(new Food(10, "Nước Ép Cam", "Nước ép cam tươi nguyên chất, giàu vitamin C.", 25000, R.drawable.mi_kim_chi_hai_san, "drink"));

        // Thêm món ăn mới ở đây
        foodList.add(new Food(11, "Bún Chả Hà Nội", "Bún chả Hà Nội với thịt nướng thơm lừng, nước mắm chua ngọt đặc trưng.", 50000, R.drawable.mi_kim_chi_hai_san, "food"));
        foodList.add(new Food(12, "Cơm Tấm Sườn Bì", "Cơm tấm với sườn nướng, bì heo và trứng ốp la, kèm nước mắm đặc biệt.", 45000, R.drawable.mi_kim_chi_hai_san, "food"));
        foodList.add(new Food(13, "Bánh Xèo", "Bánh xèo giòn rụm với nhân tôm, thịt và giá đỗ, ăn kèm rau sống.", 40000, R.drawable.mi_kim_chi_hai_san, "food"));
        foodList.add(new Food(14, "Trà Sữa Trân Châu", "Trà sữa thơm ngon với trân châu đen dẻo.", 30000, R.drawable.mi_kim_chi_hai_san, "drink"));
        foodList.add(new Food(15, "Nước Ép Dưa Hấu", "Nước ép dưa hấu tươi mát, giải nhiệt mùa hè.", 28000, R.drawable.mi_kim_chi_hai_san, "drink"));
        foodList.add(new Food(16, "Sữa Chua Đá", "Sữa chua đá thơm ngon với topping trái cây tươi.", 32000, R.drawable.mi_kim_chi_hai_san, "drink"));

        return foodList;
    }

    @Override
    public void onFoodClick(Food food) {
        try {
            // Đảm bảo food không null trước khi chuyển màn hình
            if (food != null) {
                LogUtils.debug(TAG, "Clicked on food: " + food.getName());

                Intent intent = new Intent(this, FoodDetailActivity.class);
                // Truyền từng thuộc tính riêng lẻ thay vì truyền cả đối tượng
                intent.putExtra("food_id", food.getId());
                intent.putExtra("food_name", food.getName());
                intent.putExtra("food_description", food.getDescription());
                intent.putExtra("food_price", food.getPrice());
                intent.putExtra("food_image", food.getImageResource());
                intent.putExtra("food_category", food.getCategory());

                startActivity(intent);
            } else {
                LogUtils.error(TAG, "Food object is null");
                Toast.makeText(this, "Lỗi: Không tìm thấy thông tin món ăn", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error navigating to food detail", e);
            Toast.makeText(this, "Đã xảy ra lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
