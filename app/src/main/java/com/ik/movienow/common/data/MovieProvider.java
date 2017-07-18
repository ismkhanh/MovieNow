package com.ik.movienow.common.data;

import android.arch.persistence.room.Room;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.AsyncTask;

import com.ik.movienow.common.Util;
import com.ik.movienow.common.models.Movie;

import timber.log.Timber;

public class MovieProvider extends ContentProvider {

    private static final int ID_FAV_LIST_URI = 100;
    private static final int ID_FAV_ITEM_URI = 101;
    private DBHelper dbHelper;
    private static UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.Favourites.TABLE_NAME, ID_FAV_LIST_URI);
        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.Favourites.TABLE_NAME+"/#", ID_FAV_ITEM_URI);
        return uriMatcher;
    }

    public MovieProvider() {

    }

    @Override
    public boolean onCreate() {
        dbHelper = DBHelper.getInstance(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
       switch (uriMatcher.match(uri)){
           case ID_FAV_LIST_URI:
               return DataContract.Favourites.TYPE_DIR;
           case ID_FAV_ITEM_URI:
               return DataContract.Favourites.TYPE_ITEM;
            default:
                throw new UnsupportedOperationException("Uri not supported: "+uri);
       }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Timber.i("query method called");
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case ID_FAV_LIST_URI:
                cursor = favouriteMovieQuery(projection, selection, selectionArgs, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;

            case ID_FAV_ITEM_URI:
                String movieId = uri.getPathSegments().get(1);
                cursor = favouriteMovieSingleItemQuery(projection,
                        DataContract.Favourites.COL_MOVIE_ID+"=?",
                        new String[]{movieId},
                        sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                return cursor;

            default: throw new UnsupportedOperationException("Uri not supported: "+uri);
        }

    }

    @Override
    public int delete(final Uri uri, String selection, final String[] selectionArgs) {
        int result =  dbHelper.getWritableDatabase().delete(DataContract.Favourites.TABLE_NAME, selection, selectionArgs);
        Timber.i("result: "+result);
        if (result != -1) {
            getContext().getContentResolver().notifyChange(uri, null);
            Timber.i("delete success");
        }else{
            Timber.i("delete unsuccess");
        }
        return result;
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        Timber.i("insert uri: "+uri);
        long result = dbHelper.getWritableDatabase().insert(DataContract.Favourites.TABLE_NAME, null, values);
        Timber.i("result: "+result);
        if (result > 0){
            Timber.i("insert success");
            getContext().getContentResolver().notifyChange(uri, null);
        }else{
            Timber.i("insert unsuccess");
        }

        return uri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Cursor favouriteMovieQuery(String[] projection, String selection, String[] selectionArgs,
                                     String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DataContract.Favourites.TABLE_NAME);
        return queryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor favouriteMovieSingleItemQuery(String[] projection, String selection,
                                       String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DataContract.Favourites.TABLE_NAME);
        return queryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }
}
