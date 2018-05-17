package com.sonyamoisset.android.cake.ui.detail.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sonyamoisset.android.cake.R;
import com.sonyamoisset.android.cake.databinding.ActivityRecipeDetailIngredientListItemBinding;
import com.sonyamoisset.android.cake.db.entity.Ingredient;

import java.util.List;

public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredients;

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        final ActivityRecipeDetailIngredientListItemBinding
                activityRecipeDetailIngredientListItemBinding;

        IngredientViewHolder(ActivityRecipeDetailIngredientListItemBinding binding) {
            super(binding.getRoot());
            this.activityRecipeDetailIngredientListItemBinding = binding;
        }

        void bind(Ingredient ingredient) {
            activityRecipeDetailIngredientListItemBinding.setIngredient(ingredient);
        }
    }

    public void setIngredientList(List<Ingredient> list) {
        this.ingredients = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ActivityRecipeDetailIngredientListItemBinding binding =
                DataBindingUtil.inflate(layoutInflater,
                        R.layout.activity_recipe_detail_ingredient_list_item,
                        parent, false);
        return new IngredientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients != null ? ingredients.size() : 0;
    }
}
