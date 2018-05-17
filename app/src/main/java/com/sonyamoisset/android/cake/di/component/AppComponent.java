package com.sonyamoisset.android.cake.di.component;

import android.app.Application;

import com.sonyamoisset.android.cake.CakeApp;
import com.sonyamoisset.android.cake.di.module.AppModule;
import com.sonyamoisset.android.cake.di.module.BuildersModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        BuildersModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(CakeApp cakeApp);
}
