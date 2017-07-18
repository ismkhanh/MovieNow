package com.ik.movienow.moviedetail;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ik.movienow.R;
import com.ik.movienow.common.Config;
import com.ik.movienow.common.data.MovieDBService;
import com.ik.movienow.common.models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieInfoViewHolder extends RecyclerView.ViewHolder{

    private final View itemView;
    @BindView(R.id.iv_movie_thumbnail)
    ImageView ivMovieThumbnail;
    @BindView(R.id.tv_md_date)
    TextView tvMovieDate;
    @BindView(R.id.tv_md_rating)
    TextView tvMovieRating;
    @BindView(R.id.tv_md_overview)
    TextView tvMovieOverview;
    @BindView(R.id.btn_md_fav)
    Button btnFavourite;
    private Movie movie;

    MovieInfoViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        ButterKnife.bind(this, itemView);

    }

    void setIvMovieThumbnail(Picasso picasso, String posterPath) {
        //loading image into thumbnail image view
        picasso.load(Config.IMAGE_POSTER_BASE_URL + posterPath)
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.no_image)
                .into(ivMovieThumbnail);
    }


    void setMovieDate(String date) {
        tvMovieDate.setText(date);
    }

    void setMovieRating(String rating) {
        tvMovieRating.setText(rating);
    }

    void setMovieOverview(String overview) {
        tvMovieOverview.setText(overview);
    }

    void setMovie(Movie movie){
        this.movie = movie;

    }

    void isMarkedFavorite(String movieId){
        IsFavouriteReceiver receiver = new IsFavouriteReceiver(new Handler());
        MovieDBService.startActionIsMarkedFavourite(itemView.getContext(), movieId, receiver);
    }

    @OnClick(R.id.btn_md_fav)
    void onFavClick(View view) {
        Button button = (Button) view;
        if (button.getText().equals(view.getContext().getString(R.string.mark_fav))) {
            btnFavourite.setText(R.string.remove_fav);
            Toast.makeText(view.getContext(), R.string.toast_mark_fav, Toast.LENGTH_SHORT).show();
            MovieDBService.startActionInsert(view.getContext(), movie);
        }else{
            btnFavourite.setText(R.string.mark_fav);
            Toast.makeText(view.getContext(), R.string.toast_remove_fav, Toast.LENGTH_SHORT).show();
            MovieDBService.startActionDelete(view.getContext(), String.valueOf(movie.getId()));
        }
    }

    public class IsFavouriteReceiver extends ResultReceiver{

        IsFavouriteReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == 1) {
                btnFavourite.setText(R.string.remove_fav);
            }else{
                btnFavourite.setText(R.string.mark_fav);
            }
            super.onReceiveResult(resultCode, resultData);
        }
    }
}
