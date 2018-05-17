package com.sonyamoisset.android.cake.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.sonyamoisset.android.cake.api.CakeWebService;
import com.sonyamoisset.android.cake.db.AppDatabase;
import com.sonyamoisset.android.cake.db.dao.RecipeDao;
import com.sonyamoisset.android.cake.network.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    @Provides
    @Singleton
    AppDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application,
                AppDatabase.class, "cake.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    RecipeDao provideRecipeDao(AppDatabase appDatabase) {
        return appDatabase.recipeDao();
    }

    @Singleton
    @Provides
    CakeWebService provideWebService() {
        return new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(CakeWebService.class);
    }

}