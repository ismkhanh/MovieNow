package com.ik.movienow.common.dependencies.components;

import android.content.Context;

import com.ik.movienow.common.dependencies.modules.ContextModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = ContextModule.class
)
public interface ContextComponent {
    Context getContext();
}
