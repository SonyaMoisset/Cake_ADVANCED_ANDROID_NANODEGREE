package com.sonyamoisset.android.cake.di.module;

import com.sonyamoisset.android.cake.ui.detail.RecipeDetailActivity;
import com.sonyamoisset.android.cake.ui.detail.fragment.RecipeDetailFragment;
import com.sonyamoisset.android.cake.ui.detail.fragment.RecipeVideoFragment;
import com.sonyamoisset.android.cake.ui.detail.module.RecipeDetailActivityModule;
import com.sonyamoisset.android.cake.ui.detail.module.RecipeDetailFragmentModule;
import com.sonyamoisset.android.cake.ui.detail.module.RecipeVideoFragmentModule;
import com.sonyamoisset.android.cake.ui.main.RecipeActivity;
import com.sonyamoisset.android.cake.ui.main.RecipeActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BuildersModule {

    @ContributesAndroidInjector(modules = RecipeActivityModule.class)
    abstract RecipeActivity bindRecipeActivity();

    @ContributesAndroidInjector(modules = RecipeDetailActivityModule.class)
    abstract RecipeDetailActivity bindRecipeDetailActivity();

    @ContributesAndroidInjector(modules = RecipeDetailFragmentModule.class)
    abstract RecipeDetailFragment bindRecipeDetailFragment();

    @ContributesAndroidInjector(modules = RecipeVideoFragmentModule.class)
    abstract RecipeVideoFragment bindRecipeVideoFragment();
}
