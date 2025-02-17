package ru.rsue.Karnaukhova.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ru.rsue.Karnaukhova.CurrentUser;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;
import ru.rsue.Karnaukhova.entity.ItemList;

import java.util.ArrayList;
import java.util.List;

public class ListRepository {
    static ListRepository sListRepository;
    SQLiteDatabase mDatabase;
    ItemCursorWrapper mCursorWrapper;
    static ContentValues values;

    public static ListRepository get(Context context) {
        if (sListRepository == null) {
            sListRepository = new ListRepository(context);
        }
        return sListRepository;
    }

    private ListRepository(Context context) {
        Context mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();
    }

    public void addList(ItemList list) {
        values = getContentValues(list);
        mDatabase.insert(ItemDbSchema.ListTable.NAME, null, values);
    }

    public List<ItemList> getLists() {
        List<ItemList> itemsLists = new ArrayList<>();
        mCursorWrapper = queryItemsLists("ownerUserId = ?", new String[] {CurrentUser.currentUser.getUuid().toString()});
        try {
            mCursorWrapper.moveToFirst();
            while (!mCursorWrapper.isAfterLast()) {
                itemsLists.add(mCursorWrapper.getItemList());
                mCursorWrapper.moveToNext();
            }
        } finally {
            mCursorWrapper.close();
        }

        return itemsLists;
    }

    private static ContentValues getContentValues(ItemList itemList) {
        values = new ContentValues();
        values.put(ItemDbSchema.ListTable.Cols.UUID, itemList.getId().toString());
        values.put(ItemDbSchema.ListTable.Cols.LISTNAME, String.valueOf(itemList.getListName()));
        values.put(ItemDbSchema.ListTable.Cols.OWNERUSERID, String.valueOf(itemList.getOwnerUserId()));

        return values;
    }

    private ItemCursorWrapper queryItemsLists(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(ItemDbSchema.ListTable.NAME,
                null, //Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }
}