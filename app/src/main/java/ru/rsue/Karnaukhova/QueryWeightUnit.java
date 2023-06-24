package ru.rsue.Karnaukhova;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;

public class QueryWeightUnit {
    static SQLiteDatabase mDatabase;
    static ItemCursorWrapper queryWeightUnit(Item it, Context mContext) {
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();
        Cursor cursor = mDatabase.query(ItemDbSchema.WeightUnitTable.NAME,
                new String[]{ItemDbSchema.WeightUnitTable.Cols.NAMEWEIGHTUNIT},
                ItemDbSchema.WeightUnitTable.Cols.UUID + " = ?",
                new String[]{it.getWeightUnit().toString()},
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }
}