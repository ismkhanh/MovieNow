package com.ik.movienow.moviedetail;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.ik.movienow.R;
import com.ik.movienow.common.Config;
import com.ik.movienow.common.MovieApiService;
import com.ik.movienow.common.MyApplication;
import com.ik.movienow.common.models.Movie;
import com.ik.movienow.common.models.ReviewList;
import com.ik.movienow.common.models.TrailerList;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements LifecycleRegistryOwner{

    public static final String KEY_SELECTED_MOVIE = "movie_selected";

    @BindView(R.id.iv_md_banner) ImageView ivMovieBanner;
    @BindView(R.id.rv_movie_detail) RecyclerView rvMovieDetail;

    Picasso picasso;
    MovieApiService apiService;

    private LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private MovieDetailAdapter movieDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        picasso = ((MyApplication)getApplicationContext()).getPicasso();
        apiService = ((MyApplication)getApplicationContext()).getApiService();

        //get selected movie from the intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        Movie selectedMovie = (bundle != null && bundle.containsKey(KEY_SELECTED_MOVIE)) ?
                (Movie) bundle.getParcelable(KEY_SELECTED_MOVIE) : null;

        //if selected movie is null, close the activity
        if (selectedMovie == null) {
            showMsg(getString(R.string.selected_movie_null));
            finish();
        }

        MovieDetailViewModel.Factory factory = new MovieDetailViewModel.Factory(getApplication(),
                apiService,
                String.valueOf(selectedMovie.getId()));

        final MovieDetailViewModel viewModel = ViewModelProviders.of(this, factory)
                .get(MovieDetailViewModel.class);

        movieDetailAdapter = new MovieDetailAdapter(selectedMovie, picasso);

        rvMovieDetail.setAdapter(movieDetailAdapter);
        rvMovieDetail.setLayoutManager(new LinearLayoutManager(this));

        //loading i,age into banner image view
        picasso.load(Config.IMAGE_BANNER_BASE_URL+selectedMovie.getBackdrop_path())
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.no_image)
                .into(ivMovieBanner);

        viewModel.getTrailers().observe(this, new Observer<TrailerList>() {
            @Override
            public void onChanged(@Nullable TrailerList trailerList) {
                if (trailerList != null) {
                    movieDetailAdapter.updateTrailers(trailerList.getResults());
                    //after fetching trailers fetch reviews
                    //getReviews(viewModel);
                }
            }
        });

        viewModel.getReviews().observe(this, new Observer<ReviewList>() {
            @Override
            public void onChanged(@Nullable ReviewList reviewList) {
                if (reviewList != null) {
                    movieDetailAdapter.updateReviews(reviewList.getResults());
                }
            }
        });
    }

    private void showMsg(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentApiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    //Shared element transition on back button
                    supportFinishAfterTransition();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }


}
