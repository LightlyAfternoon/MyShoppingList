package ru.rsue.Karnaukhova;

import static ru.rsue.Karnaukhova.database.ItemDbSchema.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;

public class ItemStorage {
    private static ItemStorage sItemStorage;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ItemStorage get(Context context) {
        if (sItemStorage == null) {
            sItemStorage = new ItemStorage(context);
        }
            return sItemStorage;
    }

    private ItemStorage(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        //setStartUnits();
    }

    public void addItem(Item item) {
        ContentValues values = getContentValues(item);
        mDatabase.insert(ItemTable.NAME, null, values);
    }

    public void addWeightUnit(WeightUnit weightUnit) {
        ContentValues values = getContentValues(weightUnit);
        mDatabase.insert(WeightUnitTable.NAME, null, values);
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        ItemCursorWrapper cursor = queryItems(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                items.add(cursor.getItem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return items;
    }

    public List<WeightUnit> getWeightUnits() {
        List<WeightUnit> weightUnits = new ArrayList<>();
        ItemCursorWrapper cursor = queryWeightUnits(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                weightUnits.add(cursor.getWeightUnit());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return weightUnits;
    }

    public void setStartUnits() {
        List<String> namesUnits = Arrays.asList("шт.", "кг", "л", "г", "мл");
        for (String str: namesUnits) {
            WeightUnit weightUnit = new WeightUnit(UUID.randomUUID());
            weightUnit.setName(str);

            addWeightUnit(weightUnit);
        }
    }

    private static ContentValues getContentValues(Item item) {
        ContentValues values = new ContentValues();
        values.put(ItemTable.Cols.UUID, item.getId().toString());
        values.put(ItemTable.Cols.NAMEITEM, item.getName());
        values.put(ItemTable.Cols.COUNT, String.valueOf(item.getCount()));
        values.put(ItemTable.Cols.WEIGHTUNITID, item.getWeightUnit().toString());
        values.put(ItemTable.Cols.PRICEFORONE, String.valueOf(item.getPriceForOne()));
        values.put(ItemTable.Cols.ADDDATE, String.valueOf(item.getAddDate()));
        return values;
    }

    private static ContentValues getContentValues(WeightUnit weightUnit) {
        ContentValues values = new ContentValues();
        values.put(WeightUnitTable.Cols.UUID, weightUnit.getId().toString());
        values.put(WeightUnitTable.Cols.NAMEWEIGHTUNIT, weightUnit.getName());
        return values;
    }

    private ItemCursorWrapper queryItems(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(ItemTable.NAME,
                null, //Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }

    private ItemCursorWrapper queryWeightUnits(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(WeightUnitTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }
}