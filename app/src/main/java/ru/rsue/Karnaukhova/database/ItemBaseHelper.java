package ru.rsue.Karnaukhova.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.rsue.Karnaukhova.entity.WeightUnit;
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
                UserTable.Cols.UUID + " text primary key, " +
                UserTable.Cols.LOGIN + " text not null, " +
                UserTable.Cols.PASSWORD + " text not null, " +
                UserTable.Cols.NICKNAME + " text not null)");

        db.execSQL("insert into " + UserTable.NAME + "(" + UserTable.Cols.UUID + ", " + UserTable.Cols.LOGIN + ", " + UserTable.Cols.PASSWORD + ", " + UserTable.Cols.NICKNAME + ")" +
                " values (" + "'" + UUID.randomUUID() + "'" + ", 'Developer', " + "'1111', " + "'Разработчик'" +
                ")");

        db.execSQL("create table " + WeightUnitTable.NAME + "(" +
                WeightUnitTable.Cols.UUID + " text primary key, " +
                WeightUnitTable.Cols.NAMEWEIGHTUNIT + " text not null)");

        db.execSQL("INSERT INTO " + WeightUnitTable.NAME + "(" + WeightUnitTable.Cols.UUID + ", " + WeightUnitTable.Cols.NAMEWEIGHTUNIT + ")" +
                " VALUES " + "(" + "'" + UUID.randomUUID() + "'" +  ", 'шт.'), " + "(" + "'" + UUID.randomUUID() + "'" +  ", 'кг'), " + "(" + "'" + UUID.randomUUID() + "'" +  ", 'л'), " + "(" + "'" + UUID.randomUUID() + "'" +  ", 'г'), " + "(" + "'" + UUID.randomUUID() + "'" +  ", 'мл')");

        db.execSQL("create table " + ItemTable.NAME + "(" +
                ItemTable.Cols.UUID + " text primary key, " +
                ItemTable.Cols.NAMEITEM + " text not null, " +
                ItemTable.Cols.PRICEFORONE + " real not null, " +
                ItemTable.Cols.WEIGHTUNITID + " text not null, " +
                ItemTable.Cols.COLOR + " text not null, " +
                ItemTable.Cols.USERID + " text not null, " +
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
                ItemInListTable.Cols.UUID + " text primary key, " +
                ItemInListTable.Cols.COUNT + " integer not null, " +
                ItemInListTable.Cols.ADDDATE + " real not null, " +
                ItemInListTable.Cols.ITEMID + " text not null, " +
                ItemInListTable.Cols.LISTID + " text, " +
                ItemInListTable.Cols.QUANTITYBOUGHT + " text not null, " +
                ItemInListTable.Cols.BUYONDATE + " real, " +
                ItemInListTable.Cols.ISPRIORITY + " integer not null check(" + ItemInListTable.Cols.ISPRIORITY + " in ('0', '1')), " +
                ItemInListTable.Cols.USERID + " text not null, " +
                "foreign key(" + ItemInListTable.Cols.ITEMID + ") references " + ItemTable.NAME + "(" + ItemTable.Cols.UUID + ")" + ", " +
                "foreign key(" + ItemInListTable.Cols.LISTID + ") references " + ListTable.NAME + "(" + ListTable.Cols.UUID + ")" + ", " +
                "foreign key(" + ItemInListTable.Cols.USERID + ") references " + UserTable.NAME + "(" + UserTable.Cols.UUID + ")" + ")");

        db.execSQL("create table " + ListTable.NAME + "(" +
                ListTable.Cols.UUID + " text primary key, " +
                ListTable.Cols.LISTNAME + " text not null, " +
                ListTable.Cols.OWNERUSERID + " text not null, " +
                "foreign key(" + ListTable.Cols.OWNERUSERID + ") references " + UserTable.NAME + "(" + UserTable.Cols.UUID + ")" + ")");

        db.execSQL("create table " + AllowedUserToListTable.NAME + "(" +
                AllowedUserToListTable.Cols.UUID + " text primary key, " +
                AllowedUserToListTable.Cols.LISTID + " text not null, " +
                AllowedUserToListTable.Cols.USERID + " text not null, " +
                "foreign key(" + AllowedUserToListTable.Cols.USERID + ") references " + UserTable.NAME + "(" + UserTable.Cols.UUID + ")" + ", " +
                "foreign key(" + AllowedUserToListTable.Cols.LISTID + ") references " + ListTable.NAME + "(" + ListTable.Cols.UUID + ")" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}