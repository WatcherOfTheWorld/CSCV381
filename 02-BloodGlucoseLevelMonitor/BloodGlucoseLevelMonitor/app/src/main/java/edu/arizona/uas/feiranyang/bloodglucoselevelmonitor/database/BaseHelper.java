package edu.arizona.uas.feiranyang.bloodglucoselevelmonitor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BaseHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "levelBase.db";

    public BaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + DbSchema.Table.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        DbSchema.Table.Cols.UUID + ", " +
                        DbSchema.Table.Cols.TITLE + ", " +
                        DbSchema.Table.Cols.DATE + ", " +
                        DbSchema.Table.Cols.FASTING+", "+
                        DbSchema.Table.Cols.BF+", "+
                        DbSchema.Table.Cols.LUNCH+", "+
                        DbSchema.Table.Cols.DINNER+
                        ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}
}
