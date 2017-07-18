package com.ik.movienow.common.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper dbHelper;

    public static DBHelper getInstance(Context context){
        if (dbHelper == null)
            dbHelper = new DBHelper(context);

        return dbHelper;
    }

    private DBHelper(Context context) {
        super(context, DataContract.DB_NAME, null, DataContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataContract.Favourites.CREATE_TABLE_FAVOURITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DataContract.Favourites.TABLE_NAME);
        onCreate(db);
    }
}
