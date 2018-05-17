package com.sonyamoisset.android.cake.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

public class CakeRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CakeRemoteViewsFactory(getApplicationContext());
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, CakeRemoteViewsService.class);
    }
}
