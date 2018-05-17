package com.sonyamoisset.android.cake.ui.detail.module;

import com.sonyamoisset.android.cake.ui.detail.fragment.RecipeVideoFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RecipeVideoFragmentModule {

    @ContributesAndroidInjector
    abstract RecipeVideoFragment contributeRecipeVideoFragment();
}
