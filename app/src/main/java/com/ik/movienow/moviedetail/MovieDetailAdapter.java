package com.ik.movienow.moviedetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ik.movienow.R;
import com.ik.movienow.common.models.Movie;
import com.ik.movienow.common.models.Review;
import com.ik.movienow.common.models.Trailer;
import com.squareup.picasso.Picasso;

class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Trailer[] trailers;
    private Review[] reviews;
    private Movie movie;
    private Picasso picasso;

    MovieDetailAdapter(Movie movie, Picasso picasso) {
        this.movie = movie;
        this.picasso = picasso;
        trailers = new Trailer[]{};
        reviews = new Review[]{};
    }

    void updateTrailers(Trailer[] trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    void updateReviews(Review[] reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.empty_layout, parent, false);
            return new EmptyViewHolder(view);
        } else if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_info_layout, parent, false);
            return new MovieInfoViewHolder(view);
        } else if (viewType == 2) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.trailer_row_item, parent, false);
            return new TrailerViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_row_item, parent, false);
            return new ReviewViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //position 0 is empty view
        if (position != 0) {
            if (position == 1) {//position 1 is the movie info view - contains overview rating year etc.
                MovieInfoViewHolder ivh = (MovieInfoViewHolder) holder;
                ivh.isMarkedFavorite(String.valueOf(movie.getId()));
                ivh.setIvMovieThumbnail(picasso, movie.getPoster_path());
                ivh.setMovieDate(movie.getRelease_date());
                ivh.setMovieRating(String.valueOf(movie.getVote_average()));
                ivh.setMovieOverview(movie.getOverview());
                ivh.setMovie(movie);
            } else if (position < (trailers.length + 2)) { //position from 2 to trailers.length - trailer view
                int index = position - 2;
                boolean showLabel = index == 0;
                TrailerViewHolder tvh = (TrailerViewHolder) holder;
                tvh.setTrailerNumber(String.valueOf(index + 1), trailers[index].getKey(), showLabel);
            } else { //position from trailers.length+1 to reviews.length-1 - review view
                ReviewViewHolder rvh = (ReviewViewHolder) holder;
                int index = (position - (trailers.length)) - 2;
                boolean showLabel = index == 0;
                Review review = reviews[index];
                rvh.addReview(review.getAuthor(), review.getContent(), showLabel);
            }
        }

    }

    @Override
    public int getItemCount() {
        return trailers.length + reviews.length + 1 + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == 1)
            return 1;
        else if (position < trailers.length + 2)
            return 2;
        else
            return 3;
    }


}
