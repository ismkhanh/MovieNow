package com.ik.movienow.movielist;

import android.view.View;
import android.widget.ImageView;

import com.ik.movienow.R;
import com.ik.movienow.common.Config;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ViewHolder class for MovieListAdapter.class
 */

class MovieListViewHolder {

    @BindView(R.id.iv_movie_thumbnail) ImageView movieThumbnail;

    MovieListViewHolder(View view){
        ButterKnife.bind(this, view);
    }

    void setMovieThumbnail(Picasso picasso, String poster_path) {
        picasso.load(Config.IMAGE_POSTER_BASE_URL + poster_path)
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.no_image)
                .into(movieThumbnail);
    }
}
