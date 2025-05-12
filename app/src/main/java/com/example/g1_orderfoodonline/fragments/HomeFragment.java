package com.example.g1_orderfoodonline.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.activities.FoodDetailActivity;
import com.example.g1_orderfoodonline.activities.SearchActivity;
import com.example.g1_orderfoodonline.adapters.FoodAdapter;
import com.example.g1_orderfoodonline.models.Food;
import com.example.g1_orderfoodonline.utils.LogUtils;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements FoodAdapter.OnFoodClickListener {

    private static final String TAG = "HomeFragment";

    private RecyclerView recyclerViewPopular;
    private RecyclerView recyclerViewRecommended;
    private FoodAdapter popularAdapter;
    private FoodAdapter recommendedAdapter;
    private Chip chipAll, chipFood, chipDrinks;
    private androidx.appcompat.widget.SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupRecyclerViews();
        setupChips();
        setupSearchView();

        return view;
    }

    private void initViews(View view) {
        try {
            recyclerViewPopular = view.findViewById(R.id.recyclerViewPopular);
            recyclerViewRecommended = view.findViewById(R.id.recyclerViewRecommended);
            chipAll = view.findViewById(R.id.chipAll);
            chipFood = view.findViewById(R.id.chipFood);
            chipDrinks = view.findViewById(R.id.chipDrinks);
            searchView = view.findViewById(R.id.searchView);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error initializing views", e);
        }
    }

    private void setupRecyclerViews() {
        try {
            // Setup Popular RecyclerView
            recyclerViewPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            popularAdapter = new FoodAdapter(getContext(), getPopularFoods(), FoodAdapter.VIEW_TYPE_HORIZONTAL, this);
            recyclerViewPopular.setAdapter(popularAdapter);

            // Setup Recommended RecyclerView - Hiển thị ngang
            recyclerViewRecommended.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recommendedAdapter = new FoodAdapter(getContext(), getRecommendedFoods(), FoodAdapter.VIEW_TYPE_HORIZONTAL, this);
            recyclerViewRecommended.setAdapter(recommendedAdapter);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up RecyclerViews", e);
        }
    }

    private void setupSearchView() {
        try {
            if (searchView != null) {
                searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        intent.putExtra("query", query);
                        startActivity(intent);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up SearchView", e);
        }
    }

    private void setupChips() {
        try {
            if (chipAll != null && chipFood != null && chipDrinks != null) {
                chipAll.setOnClickListener(v -> {
                    chipAll.setChipBackgroundColorResource(R.color.primary);
                    chipAll.setTextColor(getResources().getColor(R.color.white));
                    chipFood.setChipBackgroundColorResource(R.color.white);
                    chipFood.setTextColor(getResources().getColor(R.color.text_primary));
                    chipDrinks.setChipBackgroundColorResource(R.color.white);
                    chipDrinks.setTextColor(getResources().getColor(R.color.text_primary));

                    // Update recycler views with all items
                    popularAdapter.updateData(getPopularFoods());
                    recommendedAdapter.updateData(getRecommendedFoods());
                });

                chipFood.setOnClickListener(v -> {
                    chipAll.setChipBackgroundColorResource(R.color.white);
                    chipAll.setTextColor(getResources().getColor(R.color.text_primary));
                    chipFood.setChipBackgroundColorResource(R.color.primary);
                    chipFood.setTextColor(getResources().getColor(R.color.white));
                    chipDrinks.setChipBackgroundColorResource(R.color.white);
                    chipDrinks.setTextColor(getResources().getColor(R.color.text_primary));

                    // Filter for food items
                    List<Food> filteredPopular = new ArrayList<>();
                    List<Food> filteredRecommended = new ArrayList<>();

                    for (Food food : getPopularFoods()) {
                        if (food.getCategory().equals("food")) {
                            filteredPopular.add(food);
                        }
                    }

                    for (Food food : getRecommendedFoods()) {
                        if (food.getCategory().equals("food")) {
                            filteredRecommended.add(food);
                        }
                    }

                    popularAdapter.updateData(filteredPopular);
                    recommendedAdapter.updateData(filteredRecommended);
                });

                chipDrinks.setOnClickListener(v -> {
                    chipAll.setChipBackgroundColorResource(R.color.white);
                    chipAll.setTextColor(getResources().getColor(R.color.text_primary));
                    chipFood.setChipBackgroundColorResource(R.color.white);
                    chipFood.setTextColor(getResources().getColor(R.color.text_primary));
                    chipDrinks.setChipBackgroundColorResource(R.color.primary);
                    chipDrinks.setTextColor(getResources().getColor(R.color.white));

                    // Filter for drink items
                    List<Food> filteredPopular = new ArrayList<>();
                    List<Food> filteredRecommended = new ArrayList<>();

                    for (Food food : getPopularFoods()) {
                        if (food.getCategory().equals("drink")) {
                            filteredPopular.add(food);
                        }
                    }

                    for (Food food : getRecommendedFoods()) {
                        if (food.getCategory().equals("drink")) {
                            filteredRecommended.add(food);
                        }
                    }

                    popularAdapter.updateData(filteredPopular);
                    recommendedAdapter.updateData(filteredRecommended);
                });
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up chips", e);
        }
    }

    private List<Food> getPopularFoods() {
        List<Food> foodList = new ArrayList<>();

        try {
            foodList.add(new Food(1, "Mì Cay Thập Cẩm", "Mì Chinnoo, bò Mỹ, tôm, mực, chả cá Hàn Quốc, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 69000, R.drawable.mi_kim_chi_thap_cam, "food"));
            foodList.add(new Food(2, "Mì Kim Chi Đùi Gà", "Mì Chinnoo, đùi gà, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 55000, R.drawable.mi_kim_chi_dui_ga, "food"));
            foodList.add(new Food(3, "Mì Kim Chi Hải Sản", "Mì Chinnoo, tôm, mực, chả cá Hàn Quốc, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 62000, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(4, "Mì Kim Chi Cá", "Mì Chinnoo, cá, tôm, mực, chả cá Hàn Quốc, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 49000, R.drawable.mi_kim_chi_ca, "food"));
            foodList.add(new Food(5, "Mì Soyum Hải Sản", "Mì Chinnoo, tôm, mực, chả cá Hàn Quốc, cá viên, kim chi, cài thìa, nấm, bắp cải tím.", 62000, R.drawable.mi_soyum_hai_san, "food"));
            foodList.add(new Food(6, "Mì Soyum Đùi Gà", "Mì Chinnoo, đùi gà, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 59000, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(7, "Mì Soyum Thập Cẩm", "Mì Chinnoo, bò Mỹ, tôm, mực, chả cá Hàn Quốc, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 69000, R.drawable.mi_soyum_thap_cam, "food"));
            foodList.add(new Food(8, "Mì Soyum Bò Mỹ", "Mì Chinnoo, bò Mỹ, xúc xích, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 59000, R.drawable.mi_soyum_bo_my, "food"));


            foodList.add(new Food(28, "Soda Dâu Dưa Lưới", "Dâu tây soda.", 35000, R.drawable.soda_dau_dua_luoi, "drink"));
            foodList.add(new Food(29, "Soda Dừa Dứa Đác Thơm", "Dứa nước soda và hạt cọ kẹo,", 35000, R.drawable.soda_dua_dua_dac_thom, "drink"));
            foodList.add(new Food(30, "Soda Thơm Lừng", "Dưa dứa soda.", 35000, R.drawable.soda_thom_lung, "drink"));
            foodList.add(new Food(32, "Trà Dâu Hoa Hồng", "Trà đen Hibiscus.", 35000, R.drawable.nuoc_gao_han_quoc, "drink"));
            foodList.add(new Food(26, "Nước Gạo Hàn Quốc", "Đồ uống gạo ngọt.", 35000, R.drawable.nuoc_gao_han_quoc, "drink"));
            foodList.add(new Food(27, "Nước Gạo Hoa Anh Đào", "Đồ uống gạo nếp sakura.", 35000, R.drawable.nuoc_gao_hoa_anh_dao, "drink"));
        } catch (Exception e) {
            LogUtils.error(TAG, "Error getting popular foods", e);
        }

        return foodList;
    }

    private List<Food> getRecommendedFoods() {
        List<Food> foodList = new ArrayList<>();

        try {
            // Đồ ăn
            foodList.add(new Food(9, "Mì Sincay Hải Sản", "Mì Chinnoo, tôm, mực, chả cá Hàn Quốc, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 62000, R.drawable.mi_sincay_hai_san, "food"));
            foodList.add(new Food(10, "Mì Sincay Bò Mỹ", "Mì Chinnoo, bò Mỹ, xúc xích, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 59000, R.drawable.mi_sincay_bo_my, "food"));
            foodList.add(new Food(11, "Mì Sincay Đùi Gà", "Mì Chinnoo, đùi gà, cá viên, kim chi, cải thìa, nấm, bắp cải tím.", 59000, R.drawable.mi_sincay_dui_ga, "food"));
            foodList.add(new Food(12, "Mì Trộn Tương Đen Bò Mỹ", "Mì Chinnoo, bò, cá viên, hành tây, ớt chuông, cà rốt, hành baro.", 65000, R.drawable.mi_tron_tuong_den_bo_my, "food"));
            foodList.add(new Food(13, "Mì Trộn Tương Đen Heo Cuộn", "Mì Chinnoo, heo cuộn, cá viên, hành tây, ớt chuông, cà rốt, hành baro.", 65000, R.drawable.mi_tron_tuong_den_heo_cuon, "food"));
            foodList.add(new Food(14, "Mì Trộn Tương Đen Gà", "Mì Chinnoo, gà, cá viên, hành tây, ớt chuông, cà rốt, hành baro.", 59000, R.drawable.mi_tron_tuong_den_ga, "food"));
            foodList.add(new Food(15, "Mì Trộn Tương Mandu", "Mì Chinnoo, mandu, cá viên, hành tây, ớt chuông, cà rốt, hành baro.", 55000, R.drawable.mi_tron_tuong_den_mandu, "food"));
            // Đồ uống
            foodList.add(new Food(32, "Trà Dâu Hoa Hồng", "Trà đen Hibiscus.", 35000, R.drawable.nuoc_gao_han_quoc, "drink"));
            foodList.add(new Food(33, "Trà Đào Sasin", "Trà đào.", 35000, R.drawable.nuoc_gao_hoa_anh_dao, "drink"));
            foodList.add(new Food(34, "Trà Sữa Trân Châu Sasin", "Trà sữa.", 35000, R.drawable.soda_dau_dua_luoi, "drink"));
            foodList.add(new Food(35, "Trà Sữa Matcha Trân Châu Sasin.", "Trà sữa matcha.", 35000, R.drawable.soda_dua_dua_dac_thom, "drink"));
            foodList.add(new Food(36, "Sprite", "Sprite.", 23000, R.drawable.sprite, "drink"));
            foodList.add(new Food(37, "Coca Cola", "Coca Cola.", 23000, R.drawable.coca, "drink"));
            foodList.add(new Food(31, "Sting", "Sting lon.", 29000, R.drawable.sting, "drink"));
        } catch (Exception e) {
            LogUtils.error(TAG, "Error getting recommended foods", e);
        }

        return foodList;
    }

    @Override
    public void onFoodClick(Food food) {
        try {
            // Đảm bảo food không null trước khi chuyển màn hình
            if (food != null) {
                LogUtils.debug(TAG, "Clicked on food: " + food.getName());

                Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
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
                Toast.makeText(getContext(), "Lỗi: Không tìm thấy thông tin món ăn", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error navigating to food detail", e);
            Toast.makeText(getContext(), "Đã xảy ra lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
