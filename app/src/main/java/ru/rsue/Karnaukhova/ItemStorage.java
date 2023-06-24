package ru.rsue.Karnaukhova;

import static ru.rsue.Karnaukhova.database.ItemDbSchema.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;

public class ItemStorage {
    static ItemStorage sItemStorage;
    Context mContext;
    SQLiteDatabase mDatabase;
    ItemCursorWrapper mCursorWrapper;
    Cursor cursor;
    static ContentValues values;

    public static ItemStorage get(Context context) {
        if (sItemStorage == null) {
            sItemStorage = new ItemStorage(context);
        }
            return sItemStorage;
    }

    private ItemStorage(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();
    }

    public void addItem(Item item) {
        values = getContentValues(item);
        mDatabase.insert(ItemTable.NAME, null, values);
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        mCursorWrapper = queryItems(null, null);
        try {
            mCursorWrapper.moveToFirst();
            while (!mCursorWrapper.isAfterLast()) {
                items.add(mCursorWrapper.getItem());
                mCursorWrapper.moveToNext();
            }
        } finally {
            mCursorWrapper.close();
        }

        return items;
    }

    public List<WeightUnit> getWeightUnits() {
        List<WeightUnit> weightUnits = new ArrayList<>();
        mCursorWrapper = queryAllWeightUnits(null, null);
        try {
            mCursorWrapper.moveToFirst();
            while (!mCursorWrapper.isAfterLast()) {
                weightUnits.add(mCursorWrapper.getWeightUnit());
                mCursorWrapper.moveToNext();
            }
        } finally {
            mCursorWrapper.close();
        }

        return weightUnits;
    }

    private static ContentValues getContentValues(Item item) {
        values = new ContentValues();
        values.put(ItemTable.Cols.UUID, item.getId().toString());
        values.put(ItemTable.Cols.NAMEITEM, item.getName());
        values.put(ItemTable.Cols.COUNT, String.valueOf(item.getCount()));
        values.put(ItemTable.Cols.WEIGHTUNITID, item.getWeightUnit().toString());
        values.put(ItemTable.Cols.PRICEFORONE, String.valueOf(item.getPriceForOne()));
        values.put(ItemTable.Cols.ADDDATE, String.valueOf(item.getAddDate()));
        return values;
    }

    private ItemCursorWrapper queryItems(String whereClause, String[] whereArgs) {
        cursor = mDatabase.query(ItemTable.NAME,
                null, //Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }

    private ItemCursorWrapper queryAllWeightUnits(String whereClause, String[] whereArgs) {
        cursor = mDatabase.query(WeightUnitTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }
}