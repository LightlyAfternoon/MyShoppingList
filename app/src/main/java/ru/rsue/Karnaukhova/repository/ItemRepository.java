package ru.rsue.Karnaukhova.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ru.rsue.Karnaukhova.CurrentUser;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;
import ru.rsue.Karnaukhova.entity.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    static ItemRepository sItemRepository;
    SQLiteDatabase mDatabase;
    ItemCursorWrapper mCursorWrapper;
    static ContentValues values;

    public static ItemRepository get(Context context) {
        if (sItemRepository == null) {
            sItemRepository = new ItemRepository(context);
        }
        return sItemRepository;
    }

    private ItemRepository(Context context) {
        Context mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();
    }

    public void addItem(Item item) {
        values = getContentValues(item);
        mDatabase.insert(ItemDbSchema.ItemTable.NAME, null, values);
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        mCursorWrapper = queryItems("userId = ? OR userId IS NULL", new String[] {CurrentUser.currentUser.getUuid().toString()});
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

    private static ContentValues getContentValues(Item item) {
        values = new ContentValues();
        values.put(ItemDbSchema.ItemTable.Cols.UUID, item.getId().toString());
        values.put(ItemDbSchema.ItemTable.Cols.NAMEITEM, String.valueOf(item.getName()));
        values.put(ItemDbSchema.ItemTable.Cols.PRICEFORONE, item.getPriceForOne());
        values.put(ItemDbSchema.ItemTable.Cols.WEIGHTUNITID, String.valueOf(item.getWeightUnit()));
        values.put(ItemDbSchema.ItemTable.Cols.COLOR, String.valueOf(item.getColor()));
        values.put(ItemDbSchema.ItemTable.Cols.USERID, String.valueOf(item.getUserId()));
        return values;
    }

    private ItemCursorWrapper queryItems(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(ItemDbSchema.ItemTable.NAME,
                null, //Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }
}