package edu.upf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rober on 23/05/2017.
 */

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PETS.db";
    public static final String TABLE_NAME = "PETS";
    public static final String RFID = "RFID";
    public static final String PET_NAME = "PET_NAME";
    public static final String DAY = "DAY";
    public static final String START_MORN_FEED_TIME = "MORN_STIME";
    public static final String END_MORN_FEED_TIME = "MORN_ETIME";
    public static final String START_LUNCH_FEED_TIME = "LUN_STIME";
    public static final String END_LUNCH_FEED_TIME = "LUN_ETIME";
    public static final String START_DINNER_FEED_TIME = "DIN_STIME";
    public static final String END_DINNER_FEED_TIME = "DIN_ETIME";
    public static final String MAX_FEED_MORN = "MORN_MAX";
    public static final String MAX_FEED_LUNCH = "LUN_MAX";
    public static final String MAX_FEED_DINNER = "DIN_MAX";
    public static final String FED_COUNT = "FED";


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    RFID + " TEXT NOT NULL," +
                    PET_NAME + " TEXT," +
                    DAY + " TEXT NOT NULL," +
                    START_MORN_FEED_TIME + " TEXT," +
                    END_MORN_FEED_TIME + " TEXT," +
                    START_LUNCH_FEED_TIME + " TEXT," +
                    END_LUNCH_FEED_TIME + " TEXT," +
                    START_DINNER_FEED_TIME + " TEXT," +
                    END_DINNER_FEED_TIME + " TEXT," +
                    MAX_FEED_MORN + " INTEGER," +
                    MAX_FEED_LUNCH + " INTEGER," +
                    MAX_FEED_DINNER + " INTEGER," +
                    FED_COUNT + " INTEGER," +
                    "PRIMARY KEY(" + RFID + ", " + DAY + ")," +
                    "CONSTRAINT NO_DUPLICATION UNIQUE (" + RFID + ", " + DAY + ")" +
                    ")";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insert(String RFID_IN, String PETNAME, String DAY_WEEK, String MORN_START, String MORN_END, String LUN_START, String LUN_END, String DIN_START, String DIN_END, int MAX_MORN, int MAX_LUN, int MAX_DIN) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RFID, RFID_IN);
        values.put(PET_NAME, PETNAME);
        values.put(DAY, DAY_WEEK);
        values.put(START_MORN_FEED_TIME, MORN_START);
        values.put(END_MORN_FEED_TIME, MORN_END);
        values.put(START_LUNCH_FEED_TIME, LUN_START);
        values.put(END_LUNCH_FEED_TIME, LUN_END);
        values.put(START_DINNER_FEED_TIME, DIN_START);
        values.put(END_DINNER_FEED_TIME, DIN_END);
        values.put(MAX_FEED_MORN, MAX_MORN);
        values.put(MAX_FEED_LUNCH, MAX_LUN);
        values.put(MAX_FEED_DINNER, MAX_DIN);
        values.put(FED_COUNT, 0);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public String[][] readAllforPET(String RFID_query) {
        ArrayList<String[]> petslist = new ArrayList<String[]>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.query(TABLE_NAME, new String []{RFID, PET_NAME, DAY,
                START_MORN_FEED_TIME ,
                END_MORN_FEED_TIME ,
                START_LUNCH_FEED_TIME ,
                END_LUNCH_FEED_TIME ,
                START_DINNER_FEED_TIME ,
                END_DINNER_FEED_TIME ,
                MAX_FEED_MORN ,
                MAX_FEED_LUNCH ,
                MAX_FEED_DINNER ,
                FED_COUNT},RFID + " IS ?",new String[] {RFID_query},null,null,PET_NAME);
        if(cursor!=null && !cursor.isAfterLast()){
            cursor.moveToFirst();
            String[] temp = new String[13];
            for(int j=0;j<13;j++) {
                temp[j] = cursor.getString(j);
            }
            petslist.add(temp);
        }
        while(cursor.moveToNext()) {
            String[] temp = new String[13];
            for(int j=0;j<13;j++) {
                temp[j] = cursor.getString(j);
            }
            petslist.add(temp);
        }

        return petslist.toArray(new String[][] {});
    }

    public String[][] readAll() {
        ArrayList<String[]> petslist = new ArrayList<String[]>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.query(TABLE_NAME, new String []{RFID, PET_NAME, DAY,
                        START_MORN_FEED_TIME ,
                        END_MORN_FEED_TIME ,
                        START_LUNCH_FEED_TIME ,
                        END_LUNCH_FEED_TIME ,
                        START_DINNER_FEED_TIME ,
                        END_DINNER_FEED_TIME ,
                        MAX_FEED_MORN ,
                        MAX_FEED_LUNCH ,
                        MAX_FEED_DINNER ,
                        FED_COUNT},null,null,RFID,null,PET_NAME);
        if(cursor!=null && !cursor.isAfterLast()){
            cursor.moveToFirst();
            String[] temp = new String[13];
            for(int j=0;j<13;j++) {
                temp[j] = cursor.getString(j);
            }
            petslist.add(temp);
        }
        while(cursor.moveToNext()) {
            String[] temp = new String[13];
            for(int j=0;j<13;j++) {
                temp[j] = cursor.getString(j);
            }
            petslist.add(temp);
        }

        return petslist.toArray(new String[][] {});
    }

    public String[] read(String RFID_query, String day){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor= db.query(TABLE_NAME, new String []{RFID, PET_NAME, DAY,
                START_MORN_FEED_TIME ,
                END_MORN_FEED_TIME ,
                START_LUNCH_FEED_TIME ,
                END_LUNCH_FEED_TIME ,
                START_DINNER_FEED_TIME ,
                END_DINNER_FEED_TIME ,
                MAX_FEED_MORN ,
                MAX_FEED_LUNCH ,
                MAX_FEED_DINNER ,
                FED_COUNT}, RFID+ "=? and " + DAY + " = ?", new String []{RFID_query, day},
                null,null,null,null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        String[] dataArray = new String[13];
        for(int i=0;i<13;i++) {
            dataArray[i] = cursor.getString(i);
        }
        return dataArray;

    }
}
