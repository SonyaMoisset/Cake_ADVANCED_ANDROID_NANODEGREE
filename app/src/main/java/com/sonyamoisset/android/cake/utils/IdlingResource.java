package com.sonyamoisset.android.cake.utils;


import java.util.concurrent.atomic.AtomicBoolean;

public class IdlingResource implements android.support.test.espresso.IdlingResource {

    private final AtomicBoolean isIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
    }
}
