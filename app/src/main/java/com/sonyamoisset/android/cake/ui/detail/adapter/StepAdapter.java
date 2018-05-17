package com.sonyamoisset.android.cake.ui.detail.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.sonyamoisset.android.cake.R;
import com.sonyamoisset.android.cake.databinding.ActivityRecipeDetailVerticalStepperBinding;
import com.sonyamoisset.android.cake.db.entity.Step;
import com.sonyamoisset.android.cake.ui.detail.RecipeDetailActivity;
import com.sonyamoisset.android.cake.ui.detail.fragment.RecipeDetailFragment;

import java.util.List;

import moe.feng.common.stepperview.IStepperAdapter;
import moe.feng.common.stepperview.VerticalStepperItemView;

public class StepAdapter implements IStepperAdapter {

    private final List<Step> steps;
    private final RecipeDetailActivity recipeDetailActivity;
    private final RecipeDetailFragment recipeDetailFragment;

    public StepAdapter(List<Step> steps,
                       RecipeDetailActivity recipeDetailActivity,
                       RecipeDetailFragment recipeDetailFragment) {
        this.steps = steps;
        this.recipeDetailActivity = recipeDetailActivity;
        this.recipeDetailFragment = recipeDetailFragment;
    }

    @NonNull
    @Override
    public CharSequence getTitle(int index) {
        return steps.get(index).getShortDescription();
    }

    @Nullable
    @Override
    public CharSequence getSummary(int index) {
        return steps.get(index).getDescription();
    }

    @Override
    public int size() {
        return steps.size();
    }

    @Override
    public View onCreateCustomView(int index, Context context, VerticalStepperItemView parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        ActivityRecipeDetailVerticalStepperBinding activityRecipeDetailVerticalStepperBinding =
                DataBindingUtil.inflate(layoutInflater,
                        R.layout.activity_recipe_detail_vertical_stepper,
                        parent, false);

        activityRecipeDetailVerticalStepperBinding.setWatchStepHandler(recipeDetailActivity);
        activityRecipeDetailVerticalStepperBinding.setNextStepHandler(recipeDetailFragment);
        activityRecipeDetailVerticalStepperBinding.setStep(steps.get(index));
        activityRecipeDetailVerticalStepperBinding.setStepSize(steps.size());

        return activityRecipeDetailVerticalStepperBinding.getRoot();
    }

    @Override
    public void onShow(int index) {
    }

    @Override
    public void onHide(int index) {
    }
}
