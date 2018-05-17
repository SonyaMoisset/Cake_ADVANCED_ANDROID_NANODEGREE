package com.sonyamoisset.android.cake.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.sonyamoisset.android.cake.db.dao.RecipeDao;
import com.sonyamoisset.android.cake.db.entity.Ingredient;
import com.sonyamoisset.android.cake.db.entity.Recipe;
import com.sonyamoisset.android.cake.db.entity.Step;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RecipeDao recipeDao();
}
