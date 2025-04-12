package com.example.g1_orderfoodonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categories;
    private OnCategoryClickListener listener;
    private int selectedPosition = 0;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(Context context, List<Category> categories, OnCategoryClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        
        holder.textViewCategory.setText(category.getName());
        
        try {
            int resourceId = context.getResources().getIdentifier(
                    category.getIconUrl(),
                    "drawable",
                    context.getPackageName()
            );
            holder.imageViewCategory.setImageResource(resourceId);
        } catch (Exception e) {
            holder.imageViewCategory.setImageResource(R.drawable.ic_food);
        }
        
        // Set selected state
        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.bg_category_selected);
            holder.textViewCategory.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_category_unselected);
            holder.textViewCategory.setTextColor(context.getResources().getColor(R.color.text_primary));
        }
        
        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            
            // Update the previous selected and newly selected items
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
            
            if (listener != null) {
                listener.onCategoryClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCategory;
        TextView textViewCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCategory = itemView.findViewById(R.id.imageViewCategory);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
        }
    }
}

