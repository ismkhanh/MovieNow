package com.ik.movienow.movielist;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ik.movienow.R;
import com.ik.movienow.common.ConnectionListener;
import com.ik.movienow.common.MovieApiService;
import com.ik.movienow.common.MyApplication;
import com.ik.movienow.common.data.MySharedPref;
import com.ik.movienow.common.models.Movie;
import com.ik.movienow.common.models.MovieList;
import com.ik.movienow.moviedetail.MovieDetailActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MovieListActivity extends AppCompatActivity implements LifecycleRegistryOwner{

    @BindView(R.id.gv_movie_list) GridView gridView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.layout_offline) LinearLayout llOffline;
    @BindView(R.id.tv_no_fav) TextView tvNoFavourites;

    MovieApiService apiService;
    MySharedPref sharedPref;
    Picasso picasso;

    private LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private MovieListAdapter adapter;
    private Movie[] movies;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        picasso = ((MyApplication)getApplicationContext()).getPicasso();
        sharedPref = ((MyApplication)getApplicationContext()).getSharedPref();
        apiService = ((MyApplication)getApplicationContext()).getApiService();

        //initializing the factory class which injects dependencies into MainActivityViewModel.class
        MainActivityViewModel.Factory factory =
                new MainActivityViewModel.Factory(getApplication(), apiService, getSupportLoaderManager());

        //get the view model of main activity
        viewModel = ViewModelProviders.of(this, factory)
                .get(MainActivityViewModel.class);

        //get the live data movie list
        LiveData<MovieList> movieListLiveData = viewModel.getMovieListLiveData(sharedPref.getSortPreference());

        //observe the live data movie list
        movieListLiveData.observe(this, new Observer<MovieList>() {
                    @Override
                    public void onChanged(@Nullable MovieList movieList) {

                        updateUI(movieList, false);
                    }
                });

        viewModel.favouriteMovieList().observe(this, new Observer<MovieList>() {
            @Override
            public void onChanged(@Nullable MovieList movieList) {
                updateUI(movieList, true);
            }
        });
        //observe online connection status when executing api
        LiveData<Boolean> isOnline = viewModel.onlineStatus();
        isOnline.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean online) {
                progressBar.setVisibility(View.GONE);

                if (online){ //hide the offline layout
                    llOffline.setVisibility(View.GONE);
                }else{ //show offline layout
                    llOffline.setVisibility(View.VISIBLE);
                    showMsg(getString(R.string.no_internet));
                }
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie selectedMovie = movies[position];
                Intent intent = new Intent(MovieListActivity.this, MovieDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(MovieDetailActivity.KEY_SELECTED_MOVIE, selectedMovie);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //dynamically listening for network connection
        ConnectionListener connectionListener = ConnectionListener.getInstance(getApplicationContext());
        connectionListener.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isOnline) {
                Timber.i("isOnline: "+isOnline);
                if (isOnline){
                    llOffline.setVisibility(View.GONE);
                    viewModel.fetchMovieList(sharedPref.getSortPreference());
                }else{
                    llOffline.setVisibility(View.VISIBLE);
                    showMsg(getString(R.string.no_internet));
                }
            }
        });

    }

    private void updateUI(MovieList movieList, boolean isFavouriteList){
        if (movieList!= null) {
            //if movieList is not null, populate the gridview
            progressBar.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);

            Movie results[] = movieList.getResults();
            movies = results;

            if (adapter == null) {
                //if adapter is null create a new instance and set it to gridview
                adapter = new MovieListAdapter(results, picasso);
                gridView.setAdapter(adapter);
            } else {
                //if adapter is not null, update the list
                adapter.updateList(results);
            }
        } else {
            //showing loading icon since movie list is null i.e its still fetching
            progressBar.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            if (isFavouriteList) {
                //but if the list is favourite and the value is still null, then the user has not
                // marked any movie as favourite
                tvNoFavourites.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_settings_sort:
                showSortDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showSortDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.sort_dialog_title);

        ViewGroup viewGroup = null;
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_sort_option, viewGroup);
        final RadioButton popularity = (RadioButton) view.findViewById(R.id.rb_sort_popularity);
        final RadioButton ratings = (RadioButton) view.findViewById(R.id.rb_sort_ratings);
        final RadioButton favourites = (RadioButton) view.findViewById(R.id.rb_sort_fav);

        if(sharedPref.getSortPreference() == MySharedPref.SORT_POPULARITY){
            popularity.setChecked(true);
        }else if(sharedPref.getSortPreference() == MySharedPref.SORT_RATINGS){
            ratings.setChecked(true);
        }else{
            favourites.setChecked(true);
        }

        builder.setView(view);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(popularity.isChecked()){
                    sharedPref.saveSortPreference(MySharedPref.SORT_POPULARITY);
                }else if(ratings.isChecked()){
                    sharedPref.saveSortPreference(MySharedPref.SORT_RATINGS);
                }else if(favourites.isChecked()){
                    sharedPref.saveSortPreference(MySharedPref.SORT_FAVOURITES);
                }
                if (progressBar != null)
                    progressBar.setVisibility(View.VISIBLE);

                if (tvNoFavourites != null)
                    tvNoFavourites.setVisibility(View.GONE);

                viewModel.fetchMovieList(sharedPref.getSortPreference());
                dialog.cancel();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showMsg(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
