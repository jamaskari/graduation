package com.example.jamaskari.myapplication;

/**
 * Created by jamaskari on 05.06.2016.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String> getEOnames() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM main_data", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getEOaddress() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM main_data", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(3));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }


    public List<String> getEOspecialities() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM spec", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;

    }
    public List<String> getEOspecialityCodes() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM spec", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getDataByNameEO(String name) {
        List<String> datasetlist = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM main_data WHERE shortname = " +"\"" + name+"\"", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            datasetlist.add(cursor.getString(0));
            datasetlist.add(cursor.getString(1));
            datasetlist.add(cursor.getString(2));
            datasetlist.add(cursor.getString(3));
            datasetlist.add(cursor.getString(4));
            cursor.moveToNext();
        }
        return datasetlist;
    }

    public List<String> getEObyspecs(String spec)
    {
        List<String> datasetlist = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM spec WHERE speciality" + " = " +"\"" + spec+"\"", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            datasetlist.add(cursor.getString(0));
            datasetlist.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();

        return datasetlist;
    }

    public List<String> getEObyspecscodes(String speccode)
    {
        List<String> datasetlist = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM spec WHERE code" + " = " +"\"" + speccode+"\"", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            datasetlist.add(cursor.getString(0));
            datasetlist.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();

        return datasetlist;
    }



    public List<String> extortEoByItsID(int id)
    {
        List<String> datasetlist = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM main_data WHERE _id = " +"\"" + id+"\"", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            datasetlist.add(cursor.getString(0));
            datasetlist.add(cursor.getString(1));
            datasetlist.add(cursor.getString(2));
            datasetlist.add(cursor.getString(3));
            datasetlist.add(cursor.getString(4));
            cursor.moveToNext();
        }
        cursor.close();
        return datasetlist;
    }


    public int getIdByLink(String link)
    {
        int _id=0;

        Cursor cursor = database.rawQuery("SELECT * FROM main_data WHERE link = " +"\"" + link+"\"", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
                _id = Integer.parseInt(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return _id;
    }

    public List<String> getSpecialitiesById(int id)
    {
        List<String> datasetlist = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM spec WHERE _id = " +"\"" + id+"\"", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            datasetlist.add(cursor.getString(0));
            datasetlist.add(cursor.getString(1));
            datasetlist.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return datasetlist;
    }

    public List<String> getAllBuildingsFromDb()
    {
        List<String> datasetlist = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM buildings", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            datasetlist.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return datasetlist;
    }
    public List<String> getAllBuildingsAddressesFromDb()
    {
        List<String> datasetlist = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM buildings", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            datasetlist.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return datasetlist;
    }

}