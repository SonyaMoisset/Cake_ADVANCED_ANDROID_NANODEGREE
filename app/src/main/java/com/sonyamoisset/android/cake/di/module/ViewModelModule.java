package com.sonyamoisset.android.cake.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.sonyamoisset.android.cake.di.key.ViewModelKey;
import com.sonyamoisset.android.cake.ui.detail.RecipeDetailViewModel;
import com.sonyamoisset.android.cake.ui.main.RecipeViewModel;
import com.sonyamoisset.android.cake.viewmodel.CakeViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RecipeViewModel.class)
    abstract ViewModel bindRecipeViewModel(RecipeViewModel recipeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeDetailViewModel.class)
    abstract ViewModel bindRecipeDetailViewModel(RecipeDetailViewModel recipeDetailViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(CakeViewModelFactory cakeViewModelFactory);
}
