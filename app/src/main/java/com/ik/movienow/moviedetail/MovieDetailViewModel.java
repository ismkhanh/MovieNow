package com.ik.movienow.moviedetail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ik.movienow.common.MovieApiService;
import com.ik.movienow.common.models.ReviewList;
import com.ik.movienow.common.models.TrailerList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MovieDetailViewModel extends AndroidViewModel {

    private final MovieApiService apiService;
    private final String movieId;

    private volatile MutableLiveData<TrailerList> mutableTrailerList = new MutableLiveData<>();
    private volatile MutableLiveData<ReviewList> mutableReviewList = new MutableLiveData<>();
    {
        mutableTrailerList.setValue(null);
        mutableReviewList.setValue(null);
    }

    private MovieDetailViewModel(Application application, MovieApiService apiService,
                                 String movieId) {
        super(application);
        this.apiService = apiService;
        this.movieId = movieId;
    }

    LiveData<TrailerList> getTrailers(){
        if (mutableTrailerList.getValue() == null) {
            Call<TrailerList> trailersRequest = apiService.getMovieTrailers(movieId);
            trailersRequest.enqueue(new Callback<TrailerList>() {
                @Override
                public void onResponse(@NonNull Call<TrailerList> call,
                                       @NonNull Response<TrailerList> response) {
                    mutableTrailerList.setValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<TrailerList> call, @NonNull Throwable t) {
                }
            });
        }

        return mutableTrailerList;
    }

    LiveData<ReviewList> getReviews(){
        if (mutableReviewList.getValue() == null) {
            Call<ReviewList> reviewRequest = apiService.getMovieReviews(movieId);
            reviewRequest.enqueue(new Callback<ReviewList>() {
                @Override
                public void onResponse(@NonNull Call<ReviewList> call,
                                       @NonNull Response<ReviewList> response) {
                    mutableReviewList.setValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<ReviewList> call, @NonNull Throwable t) {
                }
            });
        }

        return mutableReviewList;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        @NonNull
        private final Application application;
        @NonNull
        private final MovieApiService apiService;
        @NonNull
        private final String movieId;

        public Factory(@NonNull Application application, @NonNull MovieApiService apiService,
                       @NonNull String movieId){
            this.application = application;
            this.apiService = apiService;
            this.movieId = movieId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new MovieDetailViewModel(application, apiService, movieId);
        }
    }
}
