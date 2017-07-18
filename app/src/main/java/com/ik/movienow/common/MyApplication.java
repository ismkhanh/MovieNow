package com.ik.movienow.common;

import android.app.Application;

import com.ik.movienow.BuildConfig;
import com.ik.movienow.common.data.MySharedPref;
import com.ik.movienow.common.dependencies.components.ContextComponent;
import com.ik.movienow.common.dependencies.components.DaggerContextComponent;
import com.ik.movienow.common.dependencies.components.DaggerNetworkComponent;
import com.ik.movienow.common.dependencies.components.NetworkComponent;
import com.ik.movienow.common.dependencies.modules.ContextModule;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import timber.log.Timber;

public class MyApplication extends Application {

    @Inject Picasso picasso;
    @Inject MovieApiService apiService;
    @Inject MySharedPref sharedPref;

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        //inject dependencies
        ContextComponent contextComponent = DaggerContextComponent.builder()
                .contextModule(new ContextModule(this)).build();
        NetworkComponent component = DaggerNetworkComponent.builder()
                .contextComponent(contextComponent)
                .build();
        component.inject(this);

    }

    public Picasso getPicasso(){
        return picasso;
    }

    public MovieApiService getApiService(){
        return apiService;
    }

    public MySharedPref getSharedPref(){
        return sharedPref;
    }
}
