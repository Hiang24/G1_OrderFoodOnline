package com.example.g1_orderfoodonline.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.activities.FoodDetailActivity;
import com.example.g1_orderfoodonline.activities.SearchActivity;
import com.example.g1_orderfoodonline.adapters.CategoryAdapter;
import com.example.g1_orderfoodonline.adapters.FoodAdapter;
import com.example.g1_orderfoodonline.models.Category;
import com.example.g1_orderfoodonline.models.Food;
import com.example.g1_orderfoodonline.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment implements FoodAdapter.OnFoodClickListener, CategoryAdapter.OnCategoryClickListener {

    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewFoods;
    private CategoryAdapter categoryAdapter;
    private FoodAdapter foodAdapter;
    private ImageView imageViewSearch;
    private static final String TAG = "MenuFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        initViews(view);
        setupRecyclerViews();
        setupSearchButton();

        return view;
    }

    private void initViews(View view) {
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewFoods = view.findViewById(R.id.recyclerViewFoods);
        imageViewSearch = view.findViewById(R.id.imageViewSearch);
    }

    private void setupRecyclerViews() {
        // Setup Categories RecyclerView
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(getContext(), getCategories(), this);
        recyclerViewCategories.setAdapter(categoryAdapter);

        // Setup Foods RecyclerView
        recyclerViewFoods.setLayoutManager(new GridLayoutManager(getContext(), 2));
        foodAdapter = new FoodAdapter(getContext(), getAllFoods(), FoodAdapter.VIEW_TYPE_GRID, this);
        recyclerViewFoods.setAdapter(foodAdapter);
    }

    private void setupSearchButton() {
        imageViewSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });
    }

    private List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();

        categories.add(new Category(1, "Tất cả", "ic_all"));
        categories.add(new Category(2, "Món ăn", "ic_food"));
        categories.add(new Category(3, "Đồ uống", "ic_drink"));
        categories.add(new Category(4, "Tráng miệng", "ic_dessert"));
        categories.add(new Category(5, "Combo", "ic_combo"));

        return categories;
    }

    private List<Food> getAllFoods() {
        List<Food> foodList = new ArrayList<>();

        // Add all foods
        foodList.addAll(getFoodsByCategory("food"));
        foodList.addAll(getFoodsByCategory("drink"));

        return foodList;
    }

    private List<Food> getFoodsByCategory(String category) {
        List<Food> foodList = new ArrayList<>();

        if (category.equals("food")) {
            // Sử dụng R.drawable trực tiếp
            foodList.add(new Food(1, "Mỳ Cay Thập Cẩm", "Mỳ cay thập cẩm với nhiều loại hải sản tươi ngon, cay nồng đậm đà hương vị Hàn Quốc.", 75000, 4.5f, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(2, "Cơm Trộn Hàn Quốc", "Cơm trộn Hàn Quốc với rau củ tươi ngon, thịt bò xào và trứng ốp la.", 65000, 4.3f, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(4, "Gà Rán Sốt Cay", "Gà rán giòn rụm với sốt cay đặc biệt.", 85000, 4.6f, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(5, "Bánh Mì Thịt Nướng", "Bánh mì giòn với thịt nướng thơm ngon và rau sống.", 30000, 4.4f, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(7, "Bún Bò Huế", "Bún bò Huế cay nồng với nhiều loại gia vị đặc trưng.", 55000, 4.5f, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(9, "Phở Bò", "Phở bò thơm ngon với nước dùng đậm đà và thịt bò mềm.", 60000, 4.7f, R.drawable.mi_kim_chi_hai_san, "food"));

            // Thêm món ăn mới ở đây
            foodList.add(new Food(11, "Bún Chả Hà Nội", "Bún chả Hà Nội với thịt nướng thơm lừng, nước mắm chua ngọt đặc trưng.", 50000, 4.8f, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(12, "Cơm Tấm Sườn Bì", "Cơm tấm với sườn nướng, bì heo và trứng ốp la, kèm nước mắm đặc biệt.", 45000, 4.6f, R.drawable.mi_kim_chi_hai_san, "food"));
            foodList.add(new Food(13, "Bánh Xèo", "Bánh xèo giòn rụm với nhân tôm, thịt và giá đỗ, ăn kèm rau sống.", 40000, 4.5f, R.drawable.mi_kim_chi_hai_san, "food"));

        } else if (category.equals("drink")) {
            // Sử dụng R.drawable trực tiếp
            foodList.add(new Food(3, "Trà Đào Cam Sả", "Trà đào thơm ngon với vị cam sả thanh mát.", 35000, 4.7f, R.drawable.mi_kim_chi_hai_san, "drink"));
            foodList.add(new Food(6, "Cà Phê Sữa Đá", "Cà phê đậm đà hòa quyện với sữa đặc.", 25000, 4.8f, R.drawable.mi_kim_chi_hai_san, "drink"));
            foodList.add(new Food(8, "Sinh Tố Xoài", "Sinh tố xoài ngọt mát với vị chua nhẹ.", 30000, 4.6f, R.drawable.mi_kim_chi_hai_san, "drink"));
            foodList.add(new Food(10, "Nước Ép Cam", "Nước ép cam tươi nguyên chất, giàu vitamin C.", 25000, 4.5f, R.drawable.mi_kim_chi_hai_san, "drink"));

            // Thêm đồ uống mới ở đây
            foodList.add(new Food(14, "Trà Sữa Trân Châu", "Trà sữa thơm ngon với trân châu đen dẻo.", 30000, 4.7f, R.drawable.mi_kim_chi_hai_san, "drink"));
            foodList.add(new Food(15, "Nước Ép Dưa Hấu", "Nước ép dưa hấu tươi mát, giải nhiệt mùa hè.", 28000, 4.4f, R.drawable.mi_kim_chi_hai_san, "drink"));
            foodList.add(new Food(16, "Sữa Chua Đá", "Sữa chua đá thơm ngon với topping trái cây tươi.", 32000, 4.6f, R.drawable.mi_kim_chi_hai_san, "drink"));
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
                intent.putExtra("food_rating", food.getRating());
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

    @Override
    public void onCategoryClick(Category category) {
        if (category.getId() == 1) {
            // All categories
            foodAdapter.updateData(getAllFoods());
        } else if (category.getId() == 2) {
            // Food category
            foodAdapter.updateData(getFoodsByCategory("food"));
        } else if (category.getId() == 3) {
            // Drink category
            foodAdapter.updateData(getFoodsByCategory("drink"));
        } else {
            // Other categories (for future expansion)
            foodAdapter.updateData(new ArrayList<>());
        }
    }
}
