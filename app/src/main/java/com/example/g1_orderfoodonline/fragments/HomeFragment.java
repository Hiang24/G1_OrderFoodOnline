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
            // Sử dụng R.drawable trực tiếp
            foodList.add(new Food(1, "Mỳ Cay Thập Cẩm", "Mỳ cay thập cẩm với nhiều loại hải sản tươi ngon, cay nồng đậm đà hương vị Hàn Quốc.", 75000, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(2, "Cơm Trộn Hàn Quốc", "Cơm trộn Hàn Quốc với rau củ tươi ngon, thịt bò xào và trứng ốp la.", 65000, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(3, "Trà Đào Cam Sả", "Trà đào thơm ngon với vị cam sả thanh mát.", 35000, R.drawable.mi_kim_chi_hai_san, "drink"));
            foodList.add(new Food(4, "Gà Rán Sốt Cay", "Gà rán giòn rụm với sốt cay đặc biệt.", 85000, R.drawable.mi_kim_chi_hai_san, "food"));

            // Thêm món ăn phổ biến mới ở đây
            foodList.add(new Food(11, "Bún Chả Hà Nội", "Bún chả Hà Nội với thịt nướng thơm lừng, nước mắm chua ngọt đặc trưng.", 50000, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(14, "Trà Sữa Trân Châu", "Trà sữa thơm ngon với trân châu đen dẻo.", 30000, R.drawable.mi_kim_chi_hai_san, "drink"));
        } catch (Exception e) {
            LogUtils.error(TAG, "Error getting popular foods", e);
        }

        return foodList;
    }

    private List<Food> getRecommendedFoods() {
        List<Food> foodList = new ArrayList<>();

        try {
            // Sử dụng R.drawable trực tiếp
            foodList.add(new Food(5, "Bánh Mì Thịt Nướng", "Bánh mì giòn với thịt nướng thơm ngon và rau sống.", 30000, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(6, "Cà Phê Sữa Đá", "Cà phê đậm đà hòa quyện với sữa đặc.", 25000, R.drawable.mi_kim_chi_hai_san, "drink"));
            foodList.add(new Food(7, "Bún Bò Huế", "Bún bò Huế cay nồng với nhiều loại gia vị đặc trưng.", 55000, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(8, "Sinh Tố Xoài", "Sinh tố xoài ngọt mát với vị chua nhẹ.", 30000, R.drawable.mi_kim_chi_hai_san, "drink"));
            foodList.add(new Food(9, "Phở Bò", "Phở bò thơm ngon với nước dùng đậm đà và thịt bò mềm.", 60000, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(10, "Nước Ép Cam", "Nước ép cam tươi nguyên chất, giàu vitamin C.", 25000, R.drawable.mi_kim_chi_hai_san, "drink"));

            // Thêm món ăn được đề xuất mới ở đây
            foodList.add(new Food(12, "Cơm Tấm Sườn Bì", "Cơm tấm với sườn nướng, bì heo và trứng ốp la, kèm nước mắm đặc biệt.", 45000, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(13, "Bánh Xèo", "Bánh xèo giòn rụm với nhân tôm, thịt và giá đỗ, ăn kèm rau sống.", 40000, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(15, "Nước Ép Dưa Hấu", "Nước ép dưa hấu tươi mát, giải nhiệt mùa hè.", 28000, R.drawable.mi_kim_chi_hai_san, "drink"));
            foodList.add(new Food(16, "Sữa Chua Đá", "Sữa chua đá thơm ngon với topping trái cây tươi.", 32000, R.drawable.mi_kim_chi_hai_san, "drink"));
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
