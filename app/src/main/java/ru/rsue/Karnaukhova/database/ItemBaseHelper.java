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

        String kgUnitId = weightUnitsList.get(1).getId().toString();
        String gUnitId = weightUnitsList.get(3).getId().toString();
        String lUnitId = weightUnitsList.get(2).getId().toString();

        db.execSQL("INSERT INTO " + ItemTable.NAME + "(" + ItemTable.Cols.UUID + ", " + ItemTable.Cols.NAMEITEM + ", " + ItemTable.Cols.PRICEFORONE + ", " + ItemTable.Cols.WEIGHTUNITID + ", " + ItemTable.Cols.COLOR + ")" +
                " VALUES " + "(" + "'" + UUID.randomUUID() + "'" + ", 'Картофель', '16', " + "'" + kgUnitId + "'" + ", '#FFFF00'" + ")," +
                "(" + "'" + UUID.randomUUID() + "'" + ", 'Морковь', '42', " + "'" + kgUnitId + "'" + ", '#FFA500'" + "),"+
                "(" + "'" + UUID.randomUUID() + "'" + ", 'Лук', '31', " + "'" + kgUnitId + "'" + ", '#808080'" + "),"+
                "(" + "'" + UUID.randomUUID() + "'" + ", 'Чеснок', '26', " + "'" + gUnitId + "'" + ", '#808080'" + "),"+
                "(" + "'" + UUID.randomUUID() + "'" + ", 'Петрушка', '90', " + "'" + gUnitId + "'" + ", '#A4C639'" + "),"+
                "(" + "'" + UUID.randomUUID() + "'" + ", 'Укроп', '45', " + "'" + gUnitId + "'" + ", '#A4C639'" + "),"+
                "(" + "'" + UUID.randomUUID() + "'" + ", 'Яблоко', '95', " + "'" + kgUnitId + "'" + ", '#FF0000'" + "),"+
                "(" + "'" + UUID.randomUUID() + "'" + ", 'Банан', '131', " + "'" + kgUnitId + "'" + ", '#FFFF00'" + "),"+
                "(" + "'" + UUID.randomUUID() + "'" + ", 'Лимон', '105', " + "'" + kgUnitId + "'" + ", '#FFFF00'" + "),"+
                "(" + "'" + UUID.randomUUID() + "'" + ", 'Молоко', '75', " + "'" + lUnitId + "'" + ", '#FFFFFF'" + ")");

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
                ItemInListTable.Cols.USERID + ", " +
                "foreign key(" + ItemInListTable.Cols.ITEMID + ") references " + ItemTable.NAME + "(" + ItemTable.Cols.UUID + ")" + ", " +
                "foreign key(" + ItemInListTable.Cols.LISTID + ") references " + ListTable.NAME + "(" + ListTable.Cols.UUID + ")" + ", " +
                "foreign key(" + ItemInListTable.Cols.USERID + ") references " + UserTable.NAME + "(" + UserTable.Cols.UUID + ")" + ")");

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