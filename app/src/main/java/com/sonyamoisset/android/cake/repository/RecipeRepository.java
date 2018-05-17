package com.sonyamoisset.android.cake.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sonyamoisset.android.cake.AppExecutors;
import com.sonyamoisset.android.cake.api.CakeWebService;
import com.sonyamoisset.android.cake.db.dao.RecipeDao;
import com.sonyamoisset.android.cake.db.entity.FullRecipe;
import com.sonyamoisset.android.cake.db.entity.Recipe;
import com.sonyamoisset.android.cake.api.ApiResponse;
import com.sonyamoisset.android.cake.network.NetworkBoundResource;
import com.sonyamoisset.android.cake.vo.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RecipeRepository {

    private final CakeWebService cakeWebService;
    private final AppExecutors appExecutors;
    private final RecipeDao recipeDao;

    private MediatorLiveData<List<Recipe>> recipesLiveData;

    @Inject
    RecipeRepository(CakeWebService cakeWebService,
                     RecipeDao recipeDao,
                     AppExecutors appExecutors) {
        this.cakeWebService = cakeWebService;
        this.recipeDao = recipeDao;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<List<Recipe>>> getRecipes() {
        return new NetworkBoundResource<List<Recipe>, List<Recipe>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Recipe> item) {
                recipeDao.insertRecipes(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                return data == null || data.size() == 0;
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                LiveData<List<FullRecipe>> fullRecipeLiveData = recipeDao.getAllRecipes();
                recipesLiveData = new MediatorLiveData<>();
                recipesLiveData.addSource(fullRecipeLiveData, fullRecipes -> {
                    List<Recipe> recipes = new ArrayList<>();

                    if (fullRecipes != null) {
                        for (FullRecipe fullRecipe : fullRecipes) {
                            fullRecipe.recipe.setIngredients(fullRecipe.ingredients);
                            fullRecipe.recipe.setSteps(fullRecipe.steps);
                            recipes.add(fullRecipe.recipe);
                        }
                    }

                    recipesLiveData.setValue(recipes);
                });

                return recipesLiveData;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Recipe>>> createCall() {
                return cakeWebService.getRecipes();
            }
        }.getAsLiveData();
    }
}


