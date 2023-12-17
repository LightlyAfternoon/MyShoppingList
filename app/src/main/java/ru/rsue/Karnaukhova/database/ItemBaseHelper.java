package ru.rsue.Karnaukhova.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.UUID;

import ru.rsue.Karnaukhova.database.ItemDbSchema.ItemTable;
import ru.rsue.Karnaukhova.database.ItemDbSchema.ItemInListTable;
import ru.rsue.Karnaukhova.database.ItemDbSchema.WeightUnitTable;

public class ItemBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "itemBase.db";
    public ItemBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + WeightUnitTable.NAME + "(" +
                " _id integer primary key autoIncrement, " +
                WeightUnitTable.Cols.UUID + ", " +
                WeightUnitTable.Cols.NAMEWEIGHTUNIT + ")");

        db.execSQL("INSERT INTO " + WeightUnitTable.NAME + "(" + WeightUnitTable.Cols.UUID + ", " + WeightUnitTable.Cols.NAMEWEIGHTUNIT + ")" +
                " VALUES " + "(" + "'" + UUID.randomUUID() + "'" +  ", 'шт.'), " + "(" + "'" + UUID.randomUUID() + "'" +  ", 'кг'), " + "(" + "'" + UUID.randomUUID() + "'" +  ", 'л'), " + "(" + "'" + UUID.randomUUID() + "'" +  ", 'г'), " + "(" + "'" + UUID.randomUUID() + "'" +  ", 'мл')");

        db.execSQL("create table " + ItemTable.NAME + "(" +
                " _id integer primary key autoIncrement, " +
                ItemTable.Cols.UUID + ", " +
                ItemTable.Cols.NAMEITEM + ", " +
                ItemTable.Cols.PRICEFORONE + ", " +
                ItemTable.Cols.WEIGHTUNITID + ", " +
                "foreign key(" + ItemTable.Cols.WEIGHTUNITID + ") references " + ItemTable.NAME + "(" + ItemTable.Cols.UUID + ")" + ")");

        db.execSQL("create table " + ItemInListTable.NAME + "(" +
                " _id integer primary key autoIncrement, " +
                ItemInListTable.Cols.UUID + ", " +
                ItemInListTable.Cols.COUNT + ", " +
                ItemInListTable.Cols.ADDDATE + ", " +
                ItemInListTable.Cols.ISBOUGHT + ", " +
                ItemInListTable.Cols.ITEMID + ", " +
                "foreign key(" + ItemInListTable.Cols.ITEMID + ") references " + ItemTable.NAME + "(" + ItemTable.Cols.UUID + ")" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}