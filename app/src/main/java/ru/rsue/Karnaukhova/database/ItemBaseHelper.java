package ru.rsue.Karnaukhova.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.UUID;

import ru.rsue.Karnaukhova.database.ItemDbSchema.ItemTable;

public class ItemBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "itemBase.db";
    public ItemBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ItemTable.NAME + "(" +
                " _id integer primary key autoIncrement, " +
                ItemTable.Cols.UUID + ", " +
                ItemTable.Cols.NAMEITEM + ", " +
                ItemTable.Cols.COUNT + ", " +
                ItemTable.Cols.WEIGHTUNIT + ", " +
                ItemTable.Cols.PRICEFORONE + ", " +
                ItemTable.Cols.ADDDATE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
