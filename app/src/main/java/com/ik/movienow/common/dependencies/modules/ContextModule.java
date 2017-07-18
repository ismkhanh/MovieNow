package com.ik.movienow.common.dependencies.modules;

import android.content.Context;

import com.ik.movienow.common.dependencies.scopes.ApplicationScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class ContextModule {

    private Context context;

    public ContextModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    public Context getContext(){
        return context.getApplicationContext();
    }
}
