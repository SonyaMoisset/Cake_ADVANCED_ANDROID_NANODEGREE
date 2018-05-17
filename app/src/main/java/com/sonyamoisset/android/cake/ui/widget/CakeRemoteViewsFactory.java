package com.sonyamoisset.android.cake.ui.widget;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sonyamoisset.android.cake.R;
import com.sonyamoisset.android.cake.db.AppDatabase;
import com.sonyamoisset.android.cake.db.entity.FullRecipe;
import com.sonyamoisset.android.cake.db.entity.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CakeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private final AppDatabase appDatabase;
    private List<String> ingredients;

    CakeRemoteViewsFactory(Context context) {
        this.context = context;
        appDatabase = Room.databaseBuilder(context,
                AppDatabase.class,
                "cake.db").build();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        int recipeId = CakePreferenceUtils.getWidgetRecipeId(context);

        if (recipeId != -1) {
            ingredients = new ArrayList<>();

            FullRecipe fullRecipe =
                    appDatabase.recipeDao().getSingleRecipe(recipeId);


            if (fullRecipe != null) {
                for (Ingredient ingredient : fullRecipe.ingredients) {
                    ingredients.add(String.format(
                            Locale.getDefault(),
                            "%.1f %s %s",
                            ingredient.getQuantity(),
                            ingredient.getMeasure(),
                            ingredient.getIngredient()));
                }
            }
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        if (ingredients == null) {
            return 0;
        }
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || ingredients == null) {
            return null;
        }

        RemoteViews remoteViews =
                new RemoteViews(context.getPackageName(), R.layout.widget_ingredients_list);
        remoteViews.setTextViewText(R.id.widget_ingredients_items, ingredients.get(position));

        Intent fillInIntent = new Intent();
        remoteViews.setOnClickFillInIntent(R.id.widget_ingredients_items, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
