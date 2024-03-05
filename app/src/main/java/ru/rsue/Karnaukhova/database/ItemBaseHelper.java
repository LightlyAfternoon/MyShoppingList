package ru.rsue.Karnaukhova.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.rsue.Karnaukhova.ItemStorage;
import ru.rsue.Karnaukhova.MainActivity;
import ru.rsue.Karnaukhova.WeightUnit;
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

        // don't work
        Cursor weightUnitCursor = db.rawQuery("select * from "+ WeightUnitTable.NAME, null);
        List<WeightUnit> weightUnitsList = new ArrayList<>();

        while (weightUnitCursor.moveToNext()) {
            int columnIndex = weightUnitCursor.getColumnIndex(WeightUnitTable.Cols.UUID);
            weightUnitsList.add(new WeightUnit(UUID.fromString(weightUnitCursor.getString(columnIndex))));
        }
        weightUnitCursor.close();
        String pcsUnitId = "";
        for (WeightUnit unit: weightUnitsList) {
            try {
                if (unit.getName().equals("шт."))
                    pcsUnitId = unit.getId().toString();
            }
            catch (Exception ex){
            }
        }

        db.execSQL("INSERT INTO " + ItemTable.NAME + "(" + ItemTable.Cols.UUID + ", " + ItemTable.Cols.NAMEITEM + ", " + ItemTable.Cols.PRICEFORONE + ", " + ItemTable.Cols.WEIGHTUNITID + ")" +
                " VALUES " + "(" + "'" + UUID.randomUUID() + "'" + ", 'Тарелка', '94', " + "'" + pcsUnitId + "'" + ")");

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