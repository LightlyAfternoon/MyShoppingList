package ru.rsue.Karnaukhova.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;
import ru.rsue.Karnaukhova.entity.Item;
import ru.rsue.Karnaukhova.entity.WeightUnit;

import java.util.ArrayList;
import java.util.List;

public class WeightUnitRepository {
    static WeightUnitRepository sWeightUnitRepository;
    SQLiteDatabase mDatabase;
    ItemCursorWrapper mCursorWrapper;
    static ContentValues values;

    public static WeightUnitRepository get(Context context) {
        if (sWeightUnitRepository == null) {
            sWeightUnitRepository = new WeightUnitRepository(context);
        }
        return sWeightUnitRepository;
    }

    private WeightUnitRepository(Context context) {
        Context mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();
    }

    public WeightUnit getWeightUnitOfItem(Item it) {
        WeightUnit weightUnit = null;
        mCursorWrapper = queryWeightUnit(it);
        try {
            mCursorWrapper.moveToFirst();
            weightUnit = mCursorWrapper.getWeightUnit();
        } finally {
            mCursorWrapper.close();
        }

        return weightUnit;
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

    private ItemCursorWrapper queryAllWeightUnits(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(ItemDbSchema.WeightUnitTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }

    private ItemCursorWrapper queryWeightUnit(Item it) {
        Cursor cursor = mDatabase.query(ItemDbSchema.WeightUnitTable.NAME,
                null,
                ItemDbSchema.WeightUnitTable.Cols.UUID + " = ?",
                new String[]{it.getWeightUnit().toString()},
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }
}