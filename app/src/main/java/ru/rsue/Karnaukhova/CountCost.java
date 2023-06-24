package ru.rsue.Karnaukhova;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;

public class CountCost {
    static SQLiteDatabase mDatabase;

    public static double CountCost(Item it, double cost, Context mContext) {
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

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
            cost += it.getPriceForOne() * it.getCount();
        }
        else {
            cost += (it.getPriceForOne() / 100) * it.getCount();
        }

        return cost;
    }
}