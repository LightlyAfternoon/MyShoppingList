package ru.rsue.Karnaukhova.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.rsue.Karnaukhova.WeightUnit;
import ru.rsue.Karnaukhova.database.ItemDbSchema.UserTable;
import ru.rsue.Karnaukhova.database.ItemDbSchema.ItemTable;
import ru.rsue.Karnaukhova.database.ItemDbSchema.ListTable;
import ru.rsue.Karnaukhova.database.ItemDbSchema.ItemInListTable;
import ru.rsue.Karnaukhova.database.ItemDbSchema.WeightUnitTable;
import ru.rsue.Karnaukhova.database.ItemDbSchema.AllowedUserToListTable;

public class ItemBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "itemBase.db";
    public ItemBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // whytf I need here usual id AND uuid? :/
        db.execSQL("create table " + UserTable.NAME + "( " +
                " _id integer primary key autoIncrement, " +
                UserTable.Cols.UUID + ", " +
                UserTable.Cols.LOGIN + ", " +
                UserTable.Cols.PASSWORD + ", " +
                UserTable.Cols.NICKNAME + ")");

        db.execSQL("insert into " + UserTable.NAME + "(" + UserTable.Cols.UUID + ", " + UserTable.Cols.LOGIN + ", " + UserTable.Cols.PASSWORD + ", " + UserTable.Cols.NICKNAME + ")" +
                " values (" + "'" + UUID.randomUUID() + "'" + ", 'Developer', " + "'1111', " + "'Разработчик'" +
                ")");

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
                ItemTable.Cols.COLOR + ", " +
                ItemTable.Cols.USERID + ", " +
                "foreign key(" + ItemTable.Cols.WEIGHTUNITID + ") references " + WeightUnitTable.NAME + "(" + WeightUnitTable.Cols.UUID + ")" + ", " +
                "foreign key(" + ItemTable.Cols.USERID + ") references " + UserTable.NAME + "(" + UserTable.Cols.UUID + ")" + ")");

        ItemCursorWrapper cursor = new ItemCursorWrapper(db.rawQuery("select * from "+ WeightUnitTable.NAME, null));
        List<WeightUnit> weightUnitsList = new ArrayList<>();

        while (cursor.moveToNext()) {
            weightUnitsList.add(cursor.getWeightUnit());
        }
        cursor.close();

        String pcsUnitId = weightUnitsList.get(1).getId().toString();
        for (WeightUnit unit: weightUnitsList) {
            try {
                if (unit.getName().contains("шт.")) {
                    pcsUnitId = unit.getId().toString();
                }
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
                ItemInListTable.Cols.ITEMID + ", " +
                ItemInListTable.Cols.LISTID + ", " +
                ItemInListTable.Cols.QUANTITYBOUGHT + ", " +
                ItemInListTable.Cols.BUYONDATE + ", " +
                ItemInListTable.Cols.ISPRIORITY + ", " +
                "foreign key(" + ItemInListTable.Cols.ITEMID + ") references " + ItemTable.NAME + "(" + ItemTable.Cols.UUID + ")" + ", " +
                "foreign key(" + ItemInListTable.Cols.LISTID + ") references " + ListTable.NAME + "(" + ListTable.Cols.UUID + ")" + ")");

        db.execSQL("create table " + ListTable.NAME + "(" +
                ListTable.Cols.UUID + ", " +
                ListTable.Cols.LISTNAME + ", " +
                ListTable.Cols.OWNERUSERID + ", " +
                "foreign key(" + ListTable.Cols.OWNERUSERID + ") references " + UserTable.NAME + "(" + UserTable.Cols.UUID + ")" + ")");

        db.execSQL("create table " + AllowedUserToListTable.NAME + "(" +
                " _id integer primary key autoIncrement, " +
                AllowedUserToListTable.Cols.UUID + ", " +
                AllowedUserToListTable.Cols.LISTID + ", " +
                AllowedUserToListTable.Cols.USERID + ", " +
                "foreign key(" + AllowedUserToListTable.Cols.USERID + ") references " + UserTable.NAME + "(" + UserTable.Cols.UUID + ")" + ", " +
                "foreign key(" + AllowedUserToListTable.Cols.LISTID + ") references " + ListTable.NAME + "(" + ListTable.Cols.UUID + ")" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}