package com.example.g1_orderfoodonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.models.Food;
import com.example.g1_orderfoodonline.utils.LogUtils;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "FoodAdapter";

    public static final int VIEW_TYPE_HORIZONTAL = 1;
    public static final int VIEW_TYPE_GRID = 2;
    public static final int VIEW_TYPE_LIST = 3;

    private Context context;
    private List<Food> foodList;
    private int viewType;
    private OnFoodClickListener listener;

    public interface OnFoodClickListener {
        void onFoodClick(Food food);
    }

    public FoodAdapter(Context context, List<Food> foodList, int viewType, OnFoodClickListener listener) {
        this.context = context;
        this.foodList = foodList;
        this.viewType = viewType;
        this.listener = listener;
    }

    public void updateData(List<Food> newFoodList) {
        this.foodList = newFoodList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (this.viewType == VIEW_TYPE_HORIZONTAL) {
            View view = inflater.inflate(R.layout.item_food_horizontal, parent, false);
            return new HorizontalViewHolder(view);
        } else if (this.viewType == VIEW_TYPE_GRID) {
            View view = inflater.inflate(R.layout.item_food_grid, parent, false);
            return new GridViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_food_list, parent, false);
            return new ListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            if (position < 0 || position >= foodList.size()) {
                LogUtils.error(TAG, "Invalid position: " + position);
                return;
            }

            Food food = foodList.get(position);
            if (food == null) {
                LogUtils.error(TAG, "Food at position " + position + " is null");
                return;
            }

            LogUtils.debug(TAG, "Binding food: " + food.getName() + " at position: " + position);

            if (viewType == VIEW_TYPE_HORIZONTAL) {
                HorizontalViewHolder horizontalHolder = (HorizontalViewHolder) holder;
                bindHorizontalViewHolder(horizontalHolder, food, position);
            } else if (viewType == VIEW_TYPE_GRID) {
                GridViewHolder gridHolder = (GridViewHolder) holder;
                bindGridViewHolder(gridHolder, food, position);
            } else {
                ListViewHolder listHolder = (ListViewHolder) holder;
                bindListViewHolder(listHolder, food, position);
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error binding view holder", e);
        }
    }

    private void bindHorizontalViewHolder(HorizontalViewHolder holder, final Food food, final int position) {
        try {
            holder.textViewFoodName.setText(food.getName());
            holder.textViewFoodPrice.setText(String.format("%,.0fđ", food.getPrice()));


            // Sử dụng imageResource trực tiếp
            holder.imageViewFood.setImageResource(food.getImageResource());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.debug(TAG, "Horizontal item clicked: " + food.getName() + " at position: " + position);
                    if (listener != null) {
                        listener.onFoodClick(food);
                    }
                }
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error binding horizontal view holder", e);
        }
    }

    private void bindGridViewHolder(GridViewHolder holder, final Food food, final int position) {
        try {
            holder.textViewFoodName.setText(food.getName());
            holder.textViewFoodPrice.setText(String.format("%,.0fđ", food.getPrice()));


            // Sử dụng imageResource trực tiếp
            holder.imageViewFood.setImageResource(food.getImageResource());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.debug(TAG, "Grid item clicked: " + food.getName() + " at position: " + position);
                    if (listener != null) {
                        listener.onFoodClick(food);
                    }
                }
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error binding grid view holder", e);
        }
    }

    private void bindListViewHolder(ListViewHolder holder, final Food food, final int position) {
        try {
            holder.textViewFoodName.setText(food.getName());
            holder.textViewFoodPrice.setText(String.format("%,.0fđ", food.getPrice()));
            holder.textViewDescription.setText(food.getDescription());

            // Sử dụng imageResource trực tiếp
            holder.imageViewFood.setImageResource(food.getImageResource());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.debug(TAG, "List item clicked: " + food.getName() + " at position: " + position);
                    if (listener != null) {
                        listener.onFoodClick(food);
                    }
                }
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error binding list view holder", e);
        }
    }

    @Override
    public int getItemCount() {
        return foodList != null ? foodList.size() : 0;
    }

    static class HorizontalViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewFood;
        TextView textViewFoodName;
        TextView textViewFoodPrice;
        RatingBar ratingBar;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFood = itemView.findViewById(R.id.imageViewFood);
            textViewFoodName = itemView.findViewById(R.id.textViewFoodName);
            textViewFoodPrice = itemView.findViewById(R.id.textViewFoodPrice);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewFood;
        TextView textViewFoodName;
        TextView textViewFoodPrice;
        RatingBar ratingBar;
        TextView textViewRating;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFood = itemView.findViewById(R.id.imageViewFood);
            textViewFoodName = itemView.findViewById(R.id.textViewFoodName);
            textViewFoodPrice = itemView.findViewById(R.id.textViewFoodPrice);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            textViewRating = itemView.findViewById(R.id.textViewRating);
        }
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewFood;
        TextView textViewFoodName;
        TextView textViewFoodPrice;
        TextView textViewDescription;
        RatingBar ratingBar;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFood = itemView.findViewById(R.id.imageViewFood);
            textViewFoodName = itemView.findViewById(R.id.textViewFoodName);
            textViewFoodPrice = itemView.findViewById(R.id.textViewFoodPrice);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
