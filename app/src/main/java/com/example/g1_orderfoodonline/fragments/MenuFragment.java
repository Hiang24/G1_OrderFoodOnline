package com.example.g1_orderfoodonline.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        categories.add(new Category(4, "Ăn kèm", "ic_side_dishes"));


        return categories;
    }

    private List<Food> getAllFoods() {
        List<Food> foodList = new ArrayList<>();

        // Add all foods
        foodList.addAll(getFoodsByCategory("food"));
        foodList.addAll(getFoodsByCategory("drink"));
        foodList.addAll(getFoodsByCategory("side_dishes"));

        return foodList;
    }

    private List<Food> getFoodsByCategory(String category) {
        List<Food> foodList = new ArrayList<>();

        if (category.equals("food")) {
            // Sử dụng R.drawable trực tiếp
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


        } else if (category.equals("drink")) {
            // Sử dụng R.drawable trực tiếp
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
        } else if (category.equals("side_dishes")) {
            foodList.add(new Food(39, "Phô mai viên", "Bóng phô mai.", 29000, R.drawable.pho_mai_vien, "side_dishes"));
            foodList.add(new Food(40, "Khoai tây chiên", "Khoai tây.", 29000, R.drawable.khoai_tay_chien, "side_dishes"));
            foodList.add(new Food(41, "Phô Mai Que", "Phô Mai Que.", 39000, R.drawable.pho_mai_que, "side_dishes"));
            foodList.add(new Food(42, "Rong Biển Cuộn Fillet Cá Chiên", "Rong Biển Cuộn Fillet Cá Chiên.", 39000, R.drawable.rong_bien_cuon, "side_dishes"));
            foodList.add(new Food(43, "Chân Gà Xốt Hàn", "Chân Gà Xốt Hàn.", 49000, R.drawable.chan_ga_xot_han, "side_dishes"));
            foodList.add(new Food(44, "Gà Viên Chiên Giòn", "Gà Viên Chiên Giòn.", 49000, R.drawable.ga_vien_chien_gion, "side_dishes"));
            foodList.add(new Food(45, "Bánh Bạch Tuộc", "Bánh bạch tuộc.", 39000, R.drawable.banh_bach_tuoc, "side_dishes"));
            foodList.add(new Food(46, "Kimbap Chiên", "Kimbap Chiên.", 45000, R.drawable.kimbap_chien, "side_dishes"));
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
        } else if (category.getId() == 4) {
            // Side dishes category
            foodAdapter.updateData(getFoodsByCategory("side_dishes"));
        }
        else {
            // Other categories (for future expansion)
            foodAdapter.updateData(new ArrayList<>());
        }
    }
}
