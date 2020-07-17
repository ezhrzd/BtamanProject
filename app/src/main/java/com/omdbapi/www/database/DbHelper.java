package com.omdbapi.www.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.omdbapi.www.models.MainList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DbHelper extends SQLiteOpenHelper {

    public static String path = "";
    public SQLiteDatabase sqLiteDatabase;
    public static final String DB_NAME = "batman.db";

    public static final String TBL_MianList = "MianList";
    public static final String TBL_DETAIL = "detail";

    public static final String iMDBColumn = "idImdb";
    public static final String titleColumn = "title";
    public static final String yearColumn = "year";
    public static final String typeColumn = "Type";
    public static final String posterColumn = "poster";
    public static final String ratedColumn = "rated";
    public static final String releaseColumn = "releasee";
    public static final String runtimeColumn = "runtime";
    public static final String genreColumn = "genre";
    public static final String directorColumn = "director";
    public static final String writerColumn = "writer";
    public static final String actorsColumn = "actors";
    public static final String plotColumn = "plot";
    public static final String languageColumn = "language";
    public static final String countryColumn = "country";
    public static final String awardsColumn = "awards";
    public static final String metascoreColumn = "metascore";
    public static final String imdbRatingColumn = "imdbRating";
    public static final String imdbVotesColumn = "imdbVotes";
    public static final String dVDColumn = "dVD";
    public static final String boxOfficeColumn = "boxOffice";
    public static final String productionColumn = "production";
    public static final String websiteColumn = "website";
    public static final String responseColumn = "response";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, 3);
        path = "data/data/" + context.getPackageName() + "/databases/";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query, query2;
        query = (" CREATE TABLE " + TBL_MianList + "(Id INTEGER PRIMARY KEY AutoIncrement ,idImdb TEXT ,title TEXT,year TEXT , Type TEXT , poster TEXT)");
        query2 = (" CREATE TABLE " + TBL_DETAIL + "(Id INTEGER PRIMARY KEY AutoIncrement , title TEXT, year TEXT,  rated TEXT,  releasee TEXT, runtime TEXT,  genre TEXT,  director TEXT,  writer TEXT,  actors TEXT,  plot TEXT,  language TEXT,  country TEXT,  awards TEXT,  poster TEXT,  metascore TEXT,  imdbRating TEXT,  imdbVotes TEXT, idImdb TEXT,  Type TEXT,  dVD TEXT,  boxOffice TEXT,  production TEXT,  website TEXT,  response TEXT)");        db.execSQL(query);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query, query2;
        query = (" DROP TABLE IF EXISTS " + TBL_MianList);
        query2 = (" DROP TABLE IF EXISTS " + TBL_DETAIL);
        db.execSQL(query);
        db.execSQL(query2);
        onCreate(db);

    }


    public void open() {
        sqLiteDatabase = SQLiteDatabase.openDatabase(path + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
    }


    public boolean insertData(String idImdb, String title, String year, String type, String poster) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(iMDBColumn, idImdb);
        cv.put(titleColumn, title);
        cv.put(yearColumn, year);
        cv.put(typeColumn, type);
        cv.put(posterColumn, poster);
        long result = db.insert(TBL_MianList, null, cv);
        if (result == -1)
            return false;
        else
            return true;
    }


    public boolean insertDataDetail(String title, String year, String rated, String release, String runtime, String genre, String directors, String writers, String actors
            , String plot, String language, String country, String awards, String poster, String metascore, String imdbRating, String imdbVotes,
                                    String imdbID, String type, String dVD, String boxOffice, String production, String website, String response
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(titleColumn, title);
        cv.put(yearColumn, year);
        cv.put(ratedColumn, rated);
        cv.put(releaseColumn, release);
        cv.put(runtimeColumn, runtime);
        cv.put(genreColumn, genre);
        cv.put(directorColumn, directors);
        cv.put(writerColumn, writers);
        cv.put(actorsColumn, actors);
        cv.put(plotColumn, plot);
        cv.put(languageColumn, language);
        cv.put(countryColumn, country);
        cv.put(awardsColumn, awards);
        cv.put(posterColumn, poster);
        cv.put(metascoreColumn, metascore);
        cv.put(imdbRatingColumn, imdbRating);
        cv.put(imdbVotesColumn, imdbVotes);
        cv.put(iMDBColumn, imdbID);
        cv.put(typeColumn, type);
        cv.put(dVDColumn, dVD);
        cv.put(boxOfficeColumn, boxOffice);
        cv.put(productionColumn, production);
        cv.put(websiteColumn, website);
        cv.put(responseColumn, response);


        long result = db.insert(TBL_DETAIL, null, cv);
        if (result == -1)
            return false;
        else
            return true;
    }


    public List<MainList> getMainList() {
        List<MainList> movieList = new ArrayList<>();
        movieList.clear();
        open();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM MianList", null);
        while (cursor.moveToNext()) {
            movieList.add(new MainList(cursor.getString(cursor.getColumnIndex(iMDBColumn)),
                    cursor.getString(cursor.getColumnIndex(titleColumn)),
                    cursor.getString(cursor.getColumnIndex(yearColumn)),
                    cursor.getString(cursor.getColumnIndex(typeColumn)),
                    cursor.getString(cursor.getColumnIndex(posterColumn))

            ));
        }
        cursor.close();
        sqLiteDatabase.close();
        return movieList;
    }

    public Map<String, String> getDetail(String id) {

        Map<String, String> maplist = new HashMap<>();
        maplist.clear();
        open();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TBL_DETAIL + " where idImdb='" + id + "' group by id", null);


        while (cursor.moveToNext()) {
            maplist.put(titleColumn,cursor.getString(cursor.getColumnIndex(titleColumn)));
            maplist.put(yearColumn, cursor.getString(cursor.getColumnIndex(yearColumn)));
            maplist.put(ratedColumn, cursor.getString(cursor.getColumnIndex(ratedColumn)));
            maplist.put(releaseColumn, cursor.getString(cursor.getColumnIndex(releaseColumn)));
            maplist.put(runtimeColumn, cursor.getString(cursor.getColumnIndex(runtimeColumn)));
            maplist.put(genreColumn, cursor.getString(cursor.getColumnIndex(genreColumn)));
            maplist.put(directorColumn, cursor.getString(cursor.getColumnIndex(directorColumn)));
            maplist.put(writerColumn, cursor.getString(cursor.getColumnIndex(writerColumn)));
            maplist.put(actorsColumn, cursor.getString(cursor.getColumnIndex(actorsColumn)));
            maplist.put(plotColumn,  cursor.getString(cursor.getColumnIndex(plotColumn)));
            maplist.put(languageColumn,   cursor.getString(cursor.getColumnIndex(languageColumn)));
            maplist.put(countryColumn, cursor.getString(cursor.getColumnIndex(countryColumn)));
            maplist.put(awardsColumn,  cursor.getString(cursor.getColumnIndex(awardsColumn)));
            maplist.put(metascoreColumn, cursor.getString(cursor.getColumnIndex(metascoreColumn)));
            maplist.put(imdbRatingColumn,  cursor.getString(cursor.getColumnIndex(imdbRatingColumn)));
            maplist.put(imdbVotesColumn,   cursor.getString(cursor.getColumnIndex(imdbVotesColumn)));
            maplist.put(iMDBColumn,   cursor.getString(cursor.getColumnIndex(iMDBColumn)));
            maplist.put(typeColumn,   cursor.getString(cursor.getColumnIndex(typeColumn)));
            maplist.put(dVDColumn, cursor.getString(cursor.getColumnIndex(dVDColumn)));
            maplist.put(boxOfficeColumn,   cursor.getString(cursor.getColumnIndex(boxOfficeColumn)));
            maplist.put(productionColumn,   cursor.getString(cursor.getColumnIndex(productionColumn)));
            maplist.put(websiteColumn, cursor.getString(cursor.getColumnIndex(websiteColumn)));
            maplist.put(responseColumn,   cursor.getString(cursor.getColumnIndex(responseColumn)));
            maplist.put(posterColumn,   cursor.getString(cursor.getColumnIndex(posterColumn)));
        }

        cursor.close();
        sqLiteDatabase.close();

        return maplist;
    }

    public boolean rowIdExists(String StrId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select Id from " + TBL_DETAIL
                + " where idImdb=?", new String[]{StrId});
        boolean exists = (cursor.getCount() > 0);
        return exists;
    }

}
