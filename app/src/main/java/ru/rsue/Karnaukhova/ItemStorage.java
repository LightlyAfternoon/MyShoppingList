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
import ru.rsue.Karnaukhova.database.ItemDbSchema;

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

    public void addItemInList(ItemInList itemInList) {
        values = getContentValues(itemInList);
        mDatabase.insert(ItemInListTable.NAME, null, values);
    }

    public List<ItemInList> getItemsInList() {
        List<ItemInList> itemInLists = new ArrayList<>();
        mCursorWrapper = queryItems(null, null);
        try {
            mCursorWrapper.moveToFirst();
            while (!mCursorWrapper.isAfterLast()) {
                itemInLists.add(mCursorWrapper.getItemInList());
                mCursorWrapper.moveToNext();
            }
        } finally {
            mCursorWrapper.close();
        }

        return itemInLists;
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

    private static ContentValues getContentValues(ItemInList itemInList) {
        values = new ContentValues();
        values.put(ItemInListTable.Cols.UUID, itemInList.getId().toString());
        values.put(ItemInListTable.Cols.COUNT, String.valueOf(itemInList.getCount()));
        values.put(ItemInListTable.Cols.ITEMID, itemInList.getItemId().toString());
        values.put(ItemInListTable.Cols.ADDDATE, String.valueOf(itemInList.getAddDate()));
        return values;
    }

    private ItemCursorWrapper queryItems(String whereClause, String[] whereArgs) {
        cursor = mDatabase.query(ItemInListTable.NAME,
                null, //Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }

    public static ItemCursorWrapper queryItem(ItemInList it, Context mContext) {
        SQLiteDatabase mDatabase;
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();
        Cursor cursor = mDatabase.query(ItemDbSchema.ItemTable.NAME,
                new String[]{ItemTable.Cols.NAMEITEM},
                ItemDbSchema.ItemTable.Cols.UUID + " = ?",
                new String[]{it.getItemId().toString()},
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