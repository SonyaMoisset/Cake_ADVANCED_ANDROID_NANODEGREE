package com.sonyamoisset.android.cake.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.sonyamoisset.android.cake.db.entity.Recipe;
import com.sonyamoisset.android.cake.repository.RecipeRepository;
import com.sonyamoisset.android.cake.vo.Resource;

import java.util.List;

import javax.inject.Inject;

public class RecipeViewModel extends ViewModel {

    private final RecipeRepository recipeRepository;
    private LiveData<Resource<List<Recipe>>> recipes;

    @Inject
    RecipeViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public LiveData<Resource<List<Recipe>>> getRecipes() {

        if (recipes == null) {
            recipes = recipeRepository.getRecipes();
        }

        return recipes;
    }
}
