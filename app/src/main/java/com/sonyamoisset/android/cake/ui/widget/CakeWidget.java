package com.sonyamoisset.android.cake.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.sonyamoisset.android.cake.R;
import com.sonyamoisset.android.cake.ui.main.RecipeActivity;

public class CakeWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateCakeWidgetWith(context, appWidgetManager, appWidgetIds);
    }

    public static void updateCakeWidgetWith(Context context,
                                            AppWidgetManager appWidgetManager,
                                            int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, RecipeActivity.class), 0);

            RemoteViews remoteViews =
                    new RemoteViews(context.getPackageName(), R.layout.widget_cake_layout);

            remoteViews.setTextViewText(R.id.widget_ingredients_list_title,
                    CakePreferenceUtils.getWidgetTitle(context));
            remoteViews.setRemoteAdapter(R.id.widget_ingredients_list_items,
                    CakeRemoteViewsService.getIntent(context));
            remoteViews.setPendingIntentTemplate(R.id.widget_ingredients_list_items, pendingIntent);
            remoteViews.setOnClickPendingIntent(R.id.widget_cake_layout, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
