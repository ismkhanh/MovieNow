package com.ik.movienow.common;

import com.ik.movienow.common.models.MovieList;
import com.ik.movienow.common.models.ReviewList;
import com.ik.movienow.common.models.TrailerList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieApiService {

    @GET("/3/movie/popular?api_key="+Config.API_KEY)
    Call<MovieList> getPopularMovies();

    @GET("/3/movie/top_rated?api_key="+Config.API_KEY)
    Call<MovieList> getTopRatedMovies();

    @GET("/3/movie/{id}/videos?api_key="+Config.API_KEY)
    Call<TrailerList> getMovieTrailers(@Path("id") String movieId);

    @GET("/3/movie/{id}/reviews?api_key="+Config.API_KEY)
    Call<ReviewList> getMovieReviews(@Path("id") String movieId);
}
