package com.sonyamoisset.android.cake;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppExecutors {

    private final Executor diskIO;
    private final Executor mainThread;

    private AppExecutors(Executor diskIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
    }

    @Inject
    AppExecutors() {
        this(Executors.newSingleThreadExecutor(), new MainThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler handler;

        MainThreadExecutor() {
            handler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void execute(@NonNull Runnable runnable) {
            handler.post(runnable);
        }
    }
}
