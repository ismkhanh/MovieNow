package com.ik.movienow.common.dependencies.components;

import com.ik.movienow.common.MyApplication;
import com.ik.movienow.common.dependencies.modules.NetworkModule;
import com.ik.movienow.common.dependencies.scopes.ApplicationScope;
import com.ik.movienow.moviedetail.MovieDetailActivity;
import com.ik.movienow.movielist.MovieListActivity;

import dagger.Component;


@ApplicationScope
@Component(
        dependencies = ContextComponent.class,
        modules = NetworkModule.class
)
public interface NetworkComponent {
    void inject(MyApplication target);
}
