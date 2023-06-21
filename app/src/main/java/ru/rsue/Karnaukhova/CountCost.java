package ru.rsue.Karnaukhova;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;

public class CountCost {
    private static SQLiteDatabase mDatabase;

    static ItemCursorWrapper queryWeightUnit(Item it) {
        Cursor cursor = mDatabase.query(ItemDbSchema.WeightUnitTable.NAME,
                new String[]{ItemDbSchema.WeightUnitTable.Cols.NAMEWEIGHTUNIT},
                ItemDbSchema.WeightUnitTable.Cols.UUID + " = ?",
                new String[]{it.getWeightUnit().toString()},
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }

    public static double CountCost(Item it, double cost, Context mContext) {
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        ItemCursorWrapper cursor = queryWeightUnit(it);
        String name = "";
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                name = cursor.getString(cursor.getColumnIndex(ItemDbSchema.WeightUnitTable.Cols.NAMEWEIGHTUNIT));
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        if (name.equals("шт.") || name.equals("кг") || name.equals("л")){
            cost += it.getPriceForOne() * it.getCount();
        }
        else {
            cost += (it.getPriceForOne() / 100) * it.getCount();
        }

        return cost;
    }
}
