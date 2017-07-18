package com.ik.movienow.movielist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.ik.movienow.common.MovieApiService;
import com.ik.movienow.common.Util;
import com.ik.movienow.common.data.DataContract;
import com.ik.movienow.common.data.MySharedPref;
import com.ik.movienow.common.models.Movie;
import com.ik.movienow.common.models.MovieList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * ViewModel for MovieListActivity
 */
class MainActivityViewModel extends AndroidViewModel implements LoaderManager.LoaderCallbacks<Cursor>{

    private volatile MutableLiveData<MovieList> mutableMovieListLiveData = new MutableLiveData<>();
    private volatile MutableLiveData<MovieList> mutableFavListLiveData = new MutableLiveData<>();
    {
        //initially set it to null
        mutableMovieListLiveData.setValue(null);
    }

    //this is injected via the factory class
    private MovieApiService apiService;
    private final LoaderManager loaderManager;

    private volatile MutableLiveData<Boolean> isOnline = new MutableLiveData<>();
    {
        //initial value
        isOnline.setValue(false);
    }

    private MainActivityViewModel(Application application, MovieApiService apiService, LoaderManager loaderManager) {
        super(application);
        this.apiService = apiService;
        this.loaderManager = loaderManager;
    }

    /**
     * Checks if mutableMovieListLiveData is null. If null fetches the result and returns it. If not null,
     * returns the previously fetched result.
     *
     * @param sortPreference popular or top_rated
     * @return fetched mutableMovieListLiveData
     *
     */
    LiveData<MovieList> getMovieListLiveData(int sortPreference) {

        // if live data value is null fetch it from web service
        if (mutableMovieListLiveData.getValue() == null) {

            //check for internet connection
            if (Util.isOnline(this.getApplication())) {
                isOnline.setValue(true);

                if (sortPreference == MySharedPref.SORT_FAVOURITES){
                    loaderManager.initLoader(0, null, this);
                }else {
                    Call<MovieList> request;
                    if (sortPreference == MySharedPref.SORT_POPULARITY)
                        request = apiService.getPopularMovies();
                    else
                        request = apiService.getTopRatedMovies();

                    request.enqueue(new Callback<MovieList>() {
                        @Override
                        public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                            //set the response as value to the live data
                            mutableMovieListLiveData.setValue(response.body());
                        }

                        @Override
                        public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                            //set the live data value to null on error
                            mutableMovieListLiveData.setValue(null);
                        }
                    });
                }
            }else{
                //if no internet connection
                isOnline.setValue(false);
            }
        }

        return mutableMovieListLiveData;
    }

    LiveData<Boolean> onlineStatus(){
        return isOnline;
    }

    LiveData<MovieList> favouriteMovieList(){
        return mutableFavListLiveData;
    }

    void fetchMovieList(int sortPreference) {
        if (sortPreference == MySharedPref.SORT_FAVOURITES){
            loaderManager.restartLoader(0, null, this);
        }else {
            //check for internet connection
            if (Util.isOnline(this.getApplication())) {
                isOnline.setValue(true);

                Call<MovieList> request;
                if (sortPreference == MySharedPref.SORT_POPULARITY)
                    request = apiService.getPopularMovies();
                else
                    request = apiService.getTopRatedMovies();

                request.enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                        //set the response as value to the live data
                        mutableMovieListLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                        //set the live data value to null on error
                        mutableMovieListLiveData.setValue(null);
                    }
                });
            }else{
                //if no internet connection
                isOnline.setValue(false);
            }
        }
    }

    private void convertCursorToMovieObject(Cursor cursor){
        MovieList movieList = new MovieList();
        Movie[] movies = new Movie[cursor.getCount()];
        int index = 0;
        if (cursor != null && cursor.moveToFirst()){
            Timber.i("cursor count: "+cursor.getCount());
            do{
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataContract.Favourites.COL_MOVIE_ID))));
                movie.setBackdrop_path(cursor.getString(cursor.getColumnIndex(DataContract.Favourites.COL_BACKDROP_PATH)));
                movie.setPoster_path(cursor.getString(cursor.getColumnIndex(DataContract.Favourites.COL_POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(DataContract.Favourites.COL_OVERVIEW)));
                movie.setRelease_date(cursor.getString(cursor.getColumnIndex(DataContract.Favourites.COL_RELEASE_DATE)));
                movie.setVote_average(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DataContract.Favourites.COL_VOTE_AVG))));
                movies[index] = movie;
                index++;
            }while(cursor.moveToNext());
            movieList.setResults(movies);
            mutableFavListLiveData.setValue(movieList);
        }else{
            Timber.i("cursor empty");
            mutableFavListLiveData.setValue(null);
        }
        Timber.i("cursor count: end");

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 0) {
            Uri uri = DataContract.Favourites.LIST_URI;
            return new CursorLoader(getApplication(), uri, null,null,null,null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        convertCursorToMovieObject(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Injects dependencies into MainActivityViewModel.class
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application application;

        private final MovieApiService apiService;
        private LoaderManager loaderManager;

        public Factory(@NonNull Application application,
                       @NonNull MovieApiService apiService,
                       @NonNull LoaderManager loaderManager) {
            this.application = application;
            this.apiService = apiService;
            this.loaderManager = loaderManager;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new MainActivityViewModel(application, apiService, loaderManager);
        }
    }
}
