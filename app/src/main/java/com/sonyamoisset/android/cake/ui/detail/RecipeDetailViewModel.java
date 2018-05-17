package com.sonyamoisset.android.cake.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.sonyamoisset.android.cake.db.entity.Recipe;
import com.sonyamoisset.android.cake.db.entity.Step;
import com.sonyamoisset.android.cake.repository.RecipeRepository;
import com.sonyamoisset.android.cake.vo.Resource;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class RecipeDetailViewModel extends ViewModel {

    private final RecipeRepository recipeRepository;
    private MediatorLiveData<Step> steps;
    private MutableLiveData<Integer> stepId;
    private LiveData<Resource<List<Recipe>>> recipes;
    private int recipeId;

    @Inject
    RecipeDetailViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void nextStepId() {
        stepId.setValue(getStepId().getValue() + 1);
    }

    public void previousStepId() {
        stepId.setValue(getStepId().getValue() - 1);
    }

    public int getStepSize() {
        return Objects.requireNonNull(Objects.requireNonNull(recipes.getValue()).data)
                .get(recipeId).getSteps().size();
    }

    public LiveData<Resource<List<Recipe>>> getRecipes() {
        if (recipes == null) {
            recipes = recipeRepository.getRecipes();
        }

        return recipes;
    }

    public LiveData<Step> getSteps() {
        if (steps == null) {
            setStep();
        }
        return steps;
    }

    public LiveData<Integer> getStepId() {
        if (stepId == null) {
            stepId = new MutableLiveData<>();
            stepId.setValue(0);
        }

        return stepId;
    }

    private void setStep() {
        if (steps == null) {
            steps = new MediatorLiveData<>();
        }

        LiveData<Resource<List<Recipe>>> recipesLiveData = getRecipes();
        LiveData<Integer> stepIdLiveData = getStepId();

        steps.addSource(recipesLiveData, recipes -> {
            if (recipes != null &&
                    recipes.data != null) {
                steps.setValue(recipes.data.get(recipeId).getSteps().get(getStepId().getValue()));
            }
        });

        steps.addSource(stepIdLiveData, stepId -> {
            Resource<List<Recipe>> recipesResource = getRecipes().getValue();
            if (recipesResource != null &&
                    stepId != null &&
                    recipesResource.data != null) {
                steps.setValue(recipesResource.data.get(recipeId).getSteps().get(stepId));
            }
        });
    }
}
