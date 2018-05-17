package com.sonyamoisset.android.cake.ui.detail.module;

import com.sonyamoisset.android.cake.ui.detail.fragment.RecipeDetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RecipeDetailFragmentModule {

    @ContributesAndroidInjector
    abstract RecipeDetailFragment contributeRecipeDetailFragment();
}
