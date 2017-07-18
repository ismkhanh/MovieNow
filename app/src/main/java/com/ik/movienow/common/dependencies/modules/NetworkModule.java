package com.ik.movienow.common.dependencies.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ik.movienow.common.Config;
import com.ik.movienow.common.HostSelectionInterceptor;
import com.ik.movienow.common.MovieApiService;
import com.ik.movienow.common.dependencies.scopes.ApplicationScope;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module
public class NetworkModule {

    @Provides
    @ApplicationScope
    MovieApiService getMovieApiService(Retrofit retrofit) {
        return  retrofit.create(MovieApiService.class);
    }

    @Provides
    @ApplicationScope
    Retrofit getRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(Config.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)

                .build();
    }

    @Provides
    @ApplicationScope
    OkHttpClient getOkHttpClient(HttpLoggingInterceptor interceptor,
                                 HostSelectionInterceptor hostInterceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .addInterceptor(hostInterceptor)
                .addInterceptor(interceptor)
                .cache(cache)
                .build();
    }


    @Provides
    @ApplicationScope
    HttpLoggingInterceptor getInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                Timber.d(message);
            }

        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    @ApplicationScope
    HostSelectionInterceptor getNetworkInterceptor() {
        return new HostSelectionInterceptor();
    }


    @Provides
    @ApplicationScope
    Cache getCache(File cacheFile) {
        return new Cache(cacheFile, 10 * 1024 * 1024); //10MB size
    }

    @Provides
    @ApplicationScope
    File getCacheFile(Context context) {
        File cacheFile = new File(context.getCacheDir(), "movienow_cache");
        return cacheFile.mkdirs() ? cacheFile : cacheFile;
    }

    @Provides
    @ApplicationScope
    Picasso getPicasso(Context context, OkHttpClient okHttpClient) {
        return new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
    }

}
