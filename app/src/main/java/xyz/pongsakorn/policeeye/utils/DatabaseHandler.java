package xyz.pongsakorn.policeeye.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xyz.pongsakorn.policeeye.model.HistoryModel;

/**
 * Created by Porpeeranut on 28/2/2559.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "sketchDatabse";
    private static final String TABLE_HISTORY = "history";

    private static final String KEY_FILE_NAME = "file_name";
    private static final String KEY_NAME = "name";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_NOTE = "note";
    private static final String KEY_PEOPLE = "people";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + KEY_FILE_NAME + " TEXT PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_GENDER + " TEXT,"
                + KEY_NOTE + " TEXT,"
                + KEY_PEOPLE + " TEXT" + ")";
        db.execSQL(CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);*/
    }

    public void addHistory(String fileName, String name, String gender, String note, String people) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FILE_NAME, fileName);
        values.put(KEY_NAME, name);
        values.put(KEY_GENDER, gender);
        values.put(KEY_NOTE, note);
        values.put(KEY_PEOPLE, people);

        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }

    public void deleteHistory(String fileName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY, KEY_FILE_NAME + " = ?", new String[]{fileName});
        db.close();
    }

    public ArrayList<HistoryModel> getAllHistory() {
        ArrayList<HistoryModel> historyList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_HISTORY + " ORDER BY " + KEY_FILE_NAME + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HistoryModel history = new HistoryModel(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4));
                historyList.add(history);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //Collections.reverse(historyList);
        return historyList;
    }
}
