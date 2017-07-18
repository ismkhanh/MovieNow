package com.ik.movienow.movielist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ik.movienow.R;
import com.ik.movienow.common.models.Movie;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

class MovieListAdapter extends BaseAdapter {

    private Movie[] movieList;
    private Picasso picasso;

    MovieListAdapter(Movie[] movieList, Picasso picasso){
        this.movieList = movieList;
        this.picasso = picasso;
    }

    @Override
    public int getCount() {
        return movieList.length;
    }

    @Override
    public Object getItem(int position) {
        return movieList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieListViewHolder holder;
        if(convertView == null){
            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_list_item, parent, false);
            holder = new MovieListViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (MovieListViewHolder) convertView.getTag();
        }

        Movie movie = movieList[position];
        holder.setMovieThumbnail(picasso, movie.getPoster_path());

        return convertView;
    }

    //update the list
    void updateList(Movie[] movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }
}
