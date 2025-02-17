package ru.rsue.Karnaukhova.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ru.rsue.Karnaukhova.CurrentUser;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;
import ru.rsue.Karnaukhova.entity.ItemInList;

import java.util.ArrayList;
import java.util.List;

public class ItemInListRepository {
    static ItemInListRepository sItemInListRepository;
    SQLiteDatabase mDatabase;
    ItemCursorWrapper mCursorWrapper;
    static ContentValues values;

    public static ItemInListRepository get(Context context) {
        if (sItemInListRepository == null) {
            sItemInListRepository = new ItemInListRepository(context);
        }
        return sItemInListRepository;
    }

    private ItemInListRepository(Context context) {
        Context mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();
    }

    public void addItemInList(ItemInList itemInList) {
        values = getContentValues(itemInList);
        mDatabase.insert(ItemDbSchema.ItemInListTable.NAME, null, values);
    }

    public List<ItemInList> getDailyItems() {
        List<ItemInList> itemsInList = new ArrayList<>();

        mCursorWrapper = queryItemsInList("userId = ? AND listId = 'null'", new String[] {CurrentUser.currentUser.getUuid().toString()});
        try {
            mCursorWrapper.moveToFirst();
            while (!mCursorWrapper.isAfterLast()) {
                itemsInList.add(mCursorWrapper.getItemInList());
                mCursorWrapper.moveToNext();
            }
        } finally {
            mCursorWrapper.close();
        }

        return itemsInList;
    }

    public List<ItemInList> getListsItems() {
        List<ItemInList> itemsInList = new ArrayList<>();

        mCursorWrapper = queryItemsInList("userId = ? AND listId != 'null'", new String[] {CurrentUser.currentUser.getUuid().toString()});
        try {
            mCursorWrapper.moveToFirst();
            while (!mCursorWrapper.isAfterLast()) {
                itemsInList.add(mCursorWrapper.getItemInList());
                mCursorWrapper.moveToNext();
            }
        } finally {
            mCursorWrapper.close();
        }

        return itemsInList;
    }

    private static ContentValues getContentValues(ItemInList itemInList) {
        values = new ContentValues();

        values.put(ItemDbSchema.ItemInListTable.Cols.UUID, itemInList.getId().toString());
        values.put(ItemDbSchema.ItemInListTable.Cols.COUNT, String.valueOf(itemInList.getCount()));
        values.put(ItemDbSchema.ItemInListTable.Cols.ADDDATE, String.valueOf(itemInList.getAddDate()));
        values.put(ItemDbSchema.ItemInListTable.Cols.ITEMID, itemInList.getItemId().toString());
        values.put(ItemDbSchema.ItemInListTable.Cols.LISTID, String.valueOf(itemInList.getListId()));
        values.put(ItemDbSchema.ItemInListTable.Cols.QUANTITYBOUGHT, String.valueOf(itemInList.getQuantityBought()));
        values.put(ItemDbSchema.ItemInListTable.Cols.BUYONDATE, String.valueOf(itemInList.getBuyOnDate()));
        values.put(ItemDbSchema.ItemInListTable.Cols.ISPRIORITY, String.valueOf(itemInList.getIsPriority()));
        values.put(ItemDbSchema.ItemInListTable.Cols.USERID, String.valueOf(itemInList.getUserId()));

        return values;
    }

    private ItemCursorWrapper queryItemsInList(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(ItemDbSchema.ItemInListTable.NAME,
                null, //Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }

    public static ItemCursorWrapper queryItemInList(ItemInList it, Context mContext) {
        SQLiteDatabase mDatabase;
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();
        Cursor cursor = mDatabase.query(ItemDbSchema.ItemTable.NAME,
                new String[]{ItemDbSchema.ItemTable.Cols.NAMEITEM},
                ItemDbSchema.ItemTable.Cols.UUID + " = ?",
                new String[]{it.getItemId().toString()},
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }
}