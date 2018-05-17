package com.sonyamoisset.android.cake.ui.detail.fragment;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sonyamoisset.android.cake.R;
import com.sonyamoisset.android.cake.databinding.FragmentRecipeDetailBinding;
import com.sonyamoisset.android.cake.db.entity.Recipe;
import com.sonyamoisset.android.cake.ui.detail.RecipeDetailActivity;
import com.sonyamoisset.android.cake.ui.detail.RecipeDetailViewModel;
import com.sonyamoisset.android.cake.ui.detail.adapter.IngredientAdapter;
import com.sonyamoisset.android.cake.ui.detail.adapter.StepAdapter;
import com.sonyamoisset.android.cake.ui.widget.CakePreferenceUtils;
import com.sonyamoisset.android.cake.ui.widget.CakeWidget;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class RecipeDetailFragment extends Fragment {

    private static final String RECIPE_ID = "recipe_id";

    private FragmentRecipeDetailBinding fragmentRecipeDetailBinding;
    private RecipeDetailViewModel recipeDetailViewModel;
    private IngredientAdapter ingredientAdapter;
    private Recipe recipe;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentRecipeDetailBinding =
                DataBindingUtil.inflate(inflater,
                        R.layout.fragment_recipe_detail, container, false);
        fragmentRecipeDetailBinding.setHandler(this);

        fragmentRecipeDetailBinding
                .fragmentRecipeIngredientsDetailRecyclerView.setHasFixedSize(true);

        return fragmentRecipeDetailBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final int recipeId = Objects.requireNonNull(getArguments()).getInt(RECIPE_ID) - 1;

        ingredientAdapter = new IngredientAdapter();
        fragmentRecipeDetailBinding
                .fragmentRecipeIngredientsDetailRecyclerView.setAdapter(ingredientAdapter);

        recipeDetailViewModel =
                ViewModelProviders.of(Objects.requireNonNull(getActivity()), viewModelFactory)
                        .get(RecipeDetailViewModel.class);
        recipeDetailViewModel.setRecipeId(recipeId);

        recipeDetailViewModel.getRecipes().observe(this, recipesDetailViews -> {
            if (recipesDetailViews != null && recipesDetailViews.data != null) {
                recipe = recipesDetailViews.data.get(recipeId);
                populateUI();
            }
        });
    }


    public static RecipeDetailFragment recipeDetailFragmentFor(int recipeId) {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_ID, recipeId);
        recipeDetailFragment.setArguments(args);
        return recipeDetailFragment;
    }

    public void onClickNextStep() {
        int currentStep =
                fragmentRecipeDetailBinding
                        .fragmentRecipeDetailVerticalStepperView.getCurrentStep();
        fragmentRecipeDetailBinding
                .fragmentRecipeDetailVerticalStepperView.setCurrentStep(currentStep + 1);

        recipeDetailViewModel.nextStepId();
    }


    public void onClickAddWidgetToHomeScreen() {
        CakePreferenceUtils.setWidgetTitle(getContext(), recipe.getName());
        CakePreferenceUtils.setWidgetRecipeId(getContext(), recipe.getId());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(Objects.requireNonNull(getContext()), CakeWidget.class));

        appWidgetManager
                .notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredients_list_items);

        CakeWidget.updateCakeWidgetWith(getContext(), appWidgetManager, appWidgetIds);
    }

    private void populateUI() {
        Objects.requireNonNull(getActivity()).setTitle(recipe.getName());

        ingredientAdapter.setIngredientList(recipe.getIngredients());

        StepAdapter stepAdapter = new StepAdapter(recipe.getSteps(),
                (RecipeDetailActivity) getActivity(),
                this);

        fragmentRecipeDetailBinding
                .fragmentRecipeDetailVerticalStepperView.setStepperAdapter(stepAdapter);

        recipeDetailViewModel.getStepId().observe(this, fragmentRecipeDetailBinding
                .fragmentRecipeDetailVerticalStepperView::setCurrentStep);
    }
}
