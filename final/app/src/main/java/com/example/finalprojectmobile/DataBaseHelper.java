package com.example.finalprojectmobile;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "FinalProjectMobile.db";
    public static final String HIGHSCORES_TABLE_NAME ="HIGHSCORE";
    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + HIGHSCORES_TABLE_NAME + "(ID INT PRIMARY KEY, SCORE INT, PLAYTIME LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HIGHSCORES_TABLE_NAME);

        onCreate(db);
    }

    public boolean insertGame(int score){
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("score", score);
        long result = database.insert(HIGHSCORES_TABLE_NAME, null, contentValues);

        return result != -1;
    }
    public int getScoreCount() {
        SQLiteDatabase database = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s", HIGHSCORES_TABLE_NAME), null);

        return cursor.getCount();
    }

    @SuppressLint("Range")
    public void setGame(Game game, int id){
        SQLiteDatabase database = this.getReadableDatabase();
        @SuppressLint("Recycle")Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE ID=?",HIGHSCORES_TABLE_NAME), new String[] {String.valueOf(id) });

        if(cursor.moveToFirst()) {
            int score = cursor.getInt(cursor.getColumnIndex("SCORE"));
            game.setScore(score);
        }
    }
}
