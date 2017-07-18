package com.ik.movienow.common.data;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.ik.movienow.common.models.Movie;
import com.ik.movienow.moviedetail.MovieInfoViewHolder;

/**
 * This class performs the database related operations in the background thread
 */

public class MovieDBService extends IntentService {
    private static final String ACTION_INSERT = "action_insert";
    private static final String ACTION_ITEM_QUERY = "action_item_query";
    private static final String ACTION_DELETE = "action_delete";

    private static final String EXTRA_MOVIE_OBJECT = "movie_object";
    private static final String EXTRA_QUERY_RECEIVER_OBJECT = "query_receiver_object";
    private static final String EXTRA_MOVIE_ID = "movie_id";

    public MovieDBService() {
        super("MovieDBService");
    }

    /**
     * Starts the IntentService to insert the movie in the database.
     *
     * @param context context to start the service
     * @param movie movie object to be insert; passed to the IntentServer as a parcelable
     */
    public static void startActionInsert(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDBService.class);
        intent.setAction(ACTION_INSERT);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MOVIE_OBJECT, movie);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    /**
     * Starts the IntentService to check if the movie is marked favourite or not.
     *
     * @param context context to start the IntentService
     * @param movieId id of the movie to check if marked favourite
     * @param receiver ResultReceiver to return back the result
     */
    public static void startActionIsMarkedFavourite(Context context, String movieId,
                                                    MovieInfoViewHolder.IsFavouriteReceiver receiver) {
        Intent intent = new Intent(context, MovieDBService.class);
        intent.setAction(ACTION_ITEM_QUERY);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_MOVIE_ID, movieId);
        bundle.putParcelable(EXTRA_QUERY_RECEIVER_OBJECT, receiver);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    /**
     * Starts the IntentService to delete the movie from database.
     *
     * @param context context to start the IntentService
     * @param movieId id of the movie to be deleted
     */
    public static void startActionDelete(Context context, String movieId) {
        Intent intent = new Intent(context, MovieDBService.class);
        intent.setAction(ACTION_DELETE);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_MOVIE_ID, movieId);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INSERT.equals(action)) {
                Bundle bundle = intent.getExtras();
                final Movie movie = bundle.getParcelable(EXTRA_MOVIE_OBJECT);
                handleActionInsert(movie);
            } else if (ACTION_ITEM_QUERY.equals(action)) {
                Bundle bundle = intent.getExtras();
                final String movieId = bundle.getString(EXTRA_MOVIE_ID);
                final ResultReceiver receiver = bundle.getParcelable(EXTRA_QUERY_RECEIVER_OBJECT);
                handleActionIsFavourite(movieId, receiver);
            } else if (ACTION_DELETE.equals(action)){
                Bundle bundle = intent.getExtras();
                final String movieId = bundle.getString(EXTRA_MOVIE_ID);
                handleActionDelete(movieId);
            }
        }
    }

    /**
     * Handle insert operation in the background thread
     * @param movie movie object to be inserted
     */
    private void handleActionInsert(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(DataContract.Favourites.COL_MOVIE_ID, "" + movie.getId());
        values.put(DataContract.Favourites.COL_POSTER_PATH, movie.getPoster_path());
        values.put(DataContract.Favourites.COL_BACKDROP_PATH, movie.getBackdrop_path());
        values.put(DataContract.Favourites.COL_RELEASE_DATE, movie.getRelease_date());
        values.put(DataContract.Favourites.COL_VOTE_AVG, movie.getVote_average());
        values.put(DataContract.Favourites.COL_OVERVIEW, "" + movie.getOverview());
        getContentResolver().insert(DataContract.Favourites.LIST_URI, values);
    }

    /**
     * Query's the database in the background thread to check if the movie is marked favourite
     * and returns the result via ResultReceiver
     *
     * @param movieId id of the movie to be retrieved
     * @param receiver receiver to send back the result
     */
    private void handleActionIsFavourite(String movieId, ResultReceiver receiver) {
        Cursor cursor =
                getContentResolver().query(DataContract.Favourites.ITEM_URI(Long.parseLong(movieId)),
                null,
                DataContract.Favourites.COL_MOVIE_ID+"=?",
                new String[]{movieId},
                null,
                null);
        if(cursor != null && cursor.getCount() > 0){
            //movie marked as favourite
            receiver.send(1, null);
            cursor.close();
        } else{
            //movie not marked as favourite
            receiver.send(-1, null);
        }

    }

    /**
     * Performs delete operation in the background thread
     *
     * @param movieId id of the movie to be deleted
     */
    private void handleActionDelete(String movieId) {
        getContentResolver().delete(DataContract.Favourites.LIST_URI,
                DataContract.Favourites.COL_MOVIE_ID+"=?",
                new String[]{movieId});
    }
}
