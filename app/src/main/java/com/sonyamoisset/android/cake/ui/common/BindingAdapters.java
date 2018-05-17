package com.sonyamoisset.android.cake.ui.common;

import android.databinding.BindingAdapter;
import android.view.View;

public class BindingAdapters {

    @BindingAdapter("set_visibility")
    public static void setVisibility(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
