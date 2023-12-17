package ru.rsue.Karnaukhova;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.UUID;

import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;

public class CountCost {
    static SQLiteDatabase mDatabase;
    static ItemCursorWrapper queryItemWithName(String id) {
        Cursor cursor = mDatabase.query(ItemDbSchema.ItemTable.NAME,
                null,
                ItemDbSchema.ItemTable.Cols.UUID + " = ?",
                new String[]{id},
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }
    public static double CountCost(ItemInList itInL, double cost, Context mContext) {
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        ItemCursorWrapper itemCursorWrapper = queryItemWithName(itInL.getItemId().toString());
        Item it = new Item(UUID.randomUUID());
        try {
            itemCursorWrapper.moveToFirst();
            while (!itemCursorWrapper.isAfterLast()) {
                it = itemCursorWrapper.getItem();
                itemCursorWrapper.moveToNext();
            }
        }
        finally {
            itemCursorWrapper.close();
        }

        ItemCursorWrapper cursorWrapper = QueryWeightUnit.queryWeightUnit(it, mContext);
        String nameWeightUnit = "";
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                nameWeightUnit = cursorWrapper.getString(cursorWrapper.getColumnIndex(ItemDbSchema.WeightUnitTable.Cols.NAMEWEIGHTUNIT));
                cursorWrapper.moveToNext();
            }
        }
        finally {
            cursorWrapper.close();
        }

        if (nameWeightUnit.equals("шт.") || nameWeightUnit.equals("кг") || nameWeightUnit.equals("л")){
            cost += it.getPriceForOne() * itInL.getCount();
        }
        else {
            cost += (it.getPriceForOne() / 100) * itInL.getCount();
        }

        return cost;
    }
}