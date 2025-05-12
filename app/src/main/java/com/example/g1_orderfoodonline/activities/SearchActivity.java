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
        foodList.add(new Food(1, "Mì Cay Thập Cẩm", "Mì Chinnoo, bò Mỹ, tôm, mực, chả cá Hàn Quốc, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 69000, R.drawable.mi_kim_chi_thap_cam, "food"));
        foodList.add(new Food(2, "Mì Kim Chi Đùi Gà", "Mì Chinnoo, đùi gà, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 55000, R.drawable.mi_kim_chi_dui_ga, "food"));
        foodList.add(new Food(3, "Mì Kim Chi Hải Sản", "Mì Chinnoo, tôm, mực, chả cá Hàn Quốc, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 62000, R.drawable.mi_kim_chi_hai_san, "food"));
        foodList.add(new Food(4, "Mì Kim Chi Cá", "Mì Chinnoo, cá, tôm, mực, chả cá Hàn Quốc, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 49000, R.drawable.mi_kim_chi_ca, "food"));

        foodList.add(new Food(5, "Mì Soyum Hải Sản", "Mì Chinnoo, tôm, mực, chả cá Hàn Quốc, cá viên, kim chi, cài thìa, nấm, bắp cải tím.", 62000, R.drawable.mi_soyum_hai_san, "food"));
        foodList.add(new Food(6, "Mì Soyum Đùi Gà", "Mì Chinnoo, đùi gà, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 59000, R.drawable.mi_kim_chi_hai_san, "food"));
        foodList.add(new Food(7, "Mì Soyum Thập Cẩm", "Mì Chinnoo, bò Mỹ, tôm, mực, chả cá Hàn Quốc, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 69000, R.drawable.mi_soyum_thap_cam, "food"));
        foodList.add(new Food(8, "Mì Soyum Bò Mỹ", "Mì Chinnoo, bò Mỹ, xúc xích, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 59000, R.drawable.mi_soyum_bo_my, "food"));

        foodList.add(new Food(9, "Mì Sincay Hải Sản", "Mì Chinnoo, tôm, mực, chả cá Hàn Quốc, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 62000, R.drawable.mi_sincay_hai_san, "food"));
        foodList.add(new Food(10, "Mì Sincay Bò Mỹ", "Mì Chinnoo, bò Mỹ, xúc xích, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 59000, R.drawable.mi_sincay_bo_my, "food"));
        foodList.add(new Food(11, "Mì Sincay Đùi Gà", "Mì Chinnoo, đùi gà, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 59000, R.drawable.mi_sincay_dui_ga, "food"));

        foodList.add(new Food(12, "Mì Trộn Tương Đen Bò Mỹ", "Mì Chinnoo, bò, cá viên, hành tây, ớt chuông, cà rốt, hành baro.", 65000, R.drawable.mi_tron_tuong_den_bo_my, "food"));
        foodList.add(new Food(13, "Mì Trộn Tương Đen Heo Cuộn", "Mì Chinnoo, heo cuộn, cá viên, hành tây, ớt chuông, cà rốt, hành baro.", 65000, R.drawable.mi_tron_tuong_den_heo_cuon, "food"));
        foodList.add(new Food(14, "Mì Trộn Tương Đen Gà", "Mì Chinnoo, gà, cá viên, hành tây, ớt chuông, cà rốt, hành baro.", 59000, R.drawable.mi_tron_tuong_den_ga, "food"));
        foodList.add(new Food(15, "Mì Trộn Tương Mandu", "Mì Chinnoo, mandu, cá viên, hành tây, ớt chuông, cà rốt, hành baro.", 55000, R.drawable.mi_tron_tuong_den_mandu, "food"));

        foodList.add(new Food(16, "Mì Tương Hàn Thịt Heo Cuộn", "Mì Chinnoo, heo cuộn, trứng ngâm tương, cải thìa, cải thảo, nấm mèo.", 49000, R.drawable.mi_tuong_han_thit_heo_cuon, "food"));
        foodList.add(new Food(17, "Mì Tương Hàn Mandu", "Mì Chinnoo, mandu, xúc xích, cải thìa, cải thảo, nấm.", 59000, R.drawable.mi_tuong_han_mandu, "food"));

        foodList.add(new Food(18, "Cơm Trộn Thịt Bò Mỹ", "Cơm, bò Mỹ, trứng, nấm đông cô, kim chi, rong biển, cà rốt, cải ngọt, mè.", 59000, R.drawable.com_tron_thit_bo_my, "food"));
        foodList.add(new Food(19, "Cơm và Canh Kim Chi", "Cơm trắng, thịt heo, chả cá Hàn Quốc, cá viên, kim chi, nấm, bắp hạt, ớt chuông,.", 55000, R.drawable.com_canh_kim_chi, "food"));

        foodList.add(new Food(20, "Miến Trộn Ngũ Sắc Hàn Quốc", "Miến Trộn Ngũ Sắc Hàn Quốc", 65000, R.drawable.mien_tron_ngu_sac, "food"));
        foodList.add(new Food(21, "Mì Xào Sasin", "Mì Chinnoo, thịt heo, xúc xích, cá viên, cải thìa, ớt chuông, hành tây.", 55000, R.drawable.mi_xao_sasin, "food"));

        foodList.add(new Food(22, "Tokbok-cheese Bò Mỹ", "Tokbokki, bò, xúc xích, cá viên, phô mai, hành baro.", 62000, R.drawable.tok_chee_bo_my, "food"));
        foodList.add(new Food(23, "Tokbok-Cheese Heo Xào", "Tokbokki, heo, xúc xích, cá viên, phô mai, hành baro.", 59000, R.drawable.tok_chee_heo_xao, "food"));

        foodList.add(new Food(24, "Lẩu Tokbokki Bò Mỹ (2 người)", "Mì Chinnoo, tokbokki, bò Mỹ, heo, chả cá Hàn Quốc, cá viên, xúc xích, cải thìa", 199000, R.drawable.lau_tokbokki_bo_my_2_nguoi, "food"));
        foodList.add(new Food(25, "Lẩu Tokbokki Hải Sản (2 người)", "Mì Chinnoo, tokbokki, tôm, dồi sụn, chả cá Hàn Quốc, cá viên, xúc xích, cải thìa", 199000, R.drawable.lau_tokbokki_hai_san_2_nguoi, "food"));

        foodList.add(new Food(26, "Nước Gạo Hàn Quốc", "Đồ uống gạo ngọt.", 35000, R.drawable.nuoc_gao_han_quoc, "drink"));
        foodList.add(new Food(27, "Nước Gạo Hoa Anh Đào", "Đồ uống gạo nếp sakura.", 35000, R.drawable.nuoc_gao_hoa_anh_dao, "drink"));
        foodList.add(new Food(28, "Soda Dâu Dưa Lưới", "Dâu tây soda.", 35000, R.drawable.soda_dau_dua_luoi, "drink"));
        foodList.add(new Food(29, "Soda Dừa Dứa Đác Thơm", "Dứa nước soda và hạt cọ kẹo,", 35000, R.drawable.soda_dua_dua_dac_thom, "drink"));
        foodList.add(new Food(30, "Soda Thơm Lừng", "Dưa dứa soda.", 35000, R.drawable.soda_thom_lung, "drink"));
        foodList.add(new Food(32, "Trà Dâu Hoa Hồng", "Trà đen Hibiscus.", 35000, R.drawable.nuoc_gao_han_quoc, "drink"));
        foodList.add(new Food(33, "Trà Đào Sasin", "Trà đào.", 35000, R.drawable.nuoc_gao_hoa_anh_dao, "drink"));
        foodList.add(new Food(34, "Trà Sữa Trân Châu Sasin", "Trà sữa.", 35000, R.drawable.soda_dau_dua_luoi, "drink"));
        foodList.add(new Food(35, "Trà Sữa Matcha Trân Châu Sasin.", "Trà sữa matcha.", 35000, R.drawable.soda_dua_dua_dac_thom, "drink"));
        foodList.add(new Food(36, "Sprite", "Sprite.", 23000, R.drawable.sprite, "drink"));
        foodList.add(new Food(37, "Coca Cola", "Coca Cola.", 23000, R.drawable.coca, "drink"));
        foodList.add(new Food(31, "Sting", "Sting lon.", 29000, R.drawable.sting, "drink"));

        foodList.add(new Food(39, "Phô mai viên", "Bóng phô mai.", 29000, R.drawable.pho_mai_vien, "side_dishes"));
        foodList.add(new Food(40, "Khoai tây chiên", "Khoai tây.", 29000, R.drawable.khoai_tay_chien, "side_dishes"));
        foodList.add(new Food(41, "Phô Mai Que", "Phô Mai Que.", 39000, R.drawable.pho_mai_que, "side_dishes"));
        foodList.add(new Food(42, "Rong Biển Cuộn Fillet Cá Chiên", "Rong Biển Cuộn Fillet Cá Chiên.", 39000, R.drawable.rong_bien_cuon, "side_dishes"));
        foodList.add(new Food(43, "Chân Gà Xốt Hàn", "Chân Gà Xốt Hàn.", 49000, R.drawable.chan_ga_xot_han, "side_dishes"));
        foodList.add(new Food(44, "Gà Viên Chiên Giòn", "Gà Viên Chiên Giòn.", 49000, R.drawable.ga_vien_chien_gion, "side_dishes"));
        foodList.add(new Food(45, "Bánh Bạch Tuộc", "Bánh bạch tuộc.", 39000, R.drawable.banh_bach_tuoc, "side_dishes"));
        foodList.add(new Food(46, "Kimbap Chiên", "Kimbap Chiên.", 45000, R.drawable.kimbap_chien, "side_dishes"));
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
