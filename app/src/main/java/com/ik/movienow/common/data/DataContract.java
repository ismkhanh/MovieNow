package com.ik.movienow.common.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DataContract {
    public static final String DB_NAME = "movie.db";
    public static final int DB_VERSION = 2;

    public static final String SCHEME = "content://";
    public static final String AUTHORITY= "com.ik.movienow";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    public static class Favourites implements BaseColumns {
        public static final String TABLE_NAME = "favourite";

        public static final String COL_MOVIE_ID = "id";
        public static final String COL_OVERVIEW = "overview";
        public static final String COL_POSTER_PATH = "poster_path";
        public static final String COL_BACKDROP_PATH = "backdrop_path";
        public static final String COL_VOTE_AVG = "vote_average";
        public static final String COL_RELEASE_DATE = "release_date";


        public static final String CREATE_TABLE_FAVOURITE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID  + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_MOVIE_ID + " VARCHAR NOT NULL UNIQUE," +
                COL_OVERVIEW + " VARCHAR NOT NULL," +
                COL_POSTER_PATH + " VARCHAR NOT NULL," +
                COL_BACKDROP_PATH + " VARCHAR NOT NULL," +
                COL_VOTE_AVG + " VARCHAR NOT NULL," +
                COL_RELEASE_DATE + " VARCHAR NOT NULL" +
                ")";

        public static final Uri LIST_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static Uri ITEM_URI(long id){
            return ContentUris.withAppendedId(
                    BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build(),
                    id);
        }
        public static final String TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                AUTHORITY + "/" + TABLE_NAME;

        public static final String TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                AUTHORITY + "/" + TABLE_NAME;
    }
}
