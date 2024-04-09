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

    public void addList(ItemList list) {
        values = getContentValues(list);
        mDatabase.insert(ListTable.NAME, null, values);
    }

    public void addItem(Item item) {
        values = getContentValues(item);
        mDatabase.insert(ItemTable.NAME, null, values);
    }

    public void addUser(User user) {
        values = getContentValues(user);
        mDatabase.insert(UserTable.NAME, null, values);
    }

    public List<ItemInList> getItemsInList() {
        List<ItemInList> itemsInList = new ArrayList<>();
        mCursorWrapper = queryItemsInList("userId = ?", new String[] {CurrentUser.currentUser.getUuid().toString()});
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
        values.put(ItemInListTable.Cols.ADDDATE, String.valueOf(itemInList.getAddDate()));
        values.put(ItemInListTable.Cols.ITEMID, itemInList.getItemId().toString());
        values.put(ItemInListTable.Cols.LISTID, String.valueOf(itemInList.getListId()));
        values.put(ItemInListTable.Cols.QUANTITYBOUGHT, String.valueOf(itemInList.getQuantityBought()));
        values.put(ItemInListTable.Cols.BUYONDATE, String.valueOf(itemInList.getBuyOnDate()));
        values.put(ItemInListTable.Cols.ISPRIORITY, String.valueOf(itemInList.getIsPriority()));
        values.put(ItemInListTable.Cols.USERID, String.valueOf(itemInList.getUserId()));
        return values;
    }

    private static ContentValues getContentValues(ItemList itemList) {
        values = new ContentValues();
        values.put(ItemDbSchema.ListTable.Cols.UUID, itemList.getId().toString());
        values.put(ListTable.Cols.LISTNAME, String.valueOf(itemList.getListName()));
        values.put(ListTable.Cols.OWNERUSERID, String.valueOf(itemList.getOwnerUserId()));
        return values;
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

    private static ContentValues getContentValues(User user) {
        values = new ContentValues();
        values.put(ItemDbSchema.UserTable.Cols.UUID, user.getUuid().toString());
        values.put(ItemDbSchema.UserTable.Cols.LOGIN, String.valueOf(user.getLogin()));
        values.put(ItemDbSchema.UserTable.Cols.PASSWORD, user.getPassword());
        values.put(ItemDbSchema.UserTable.Cols.NICKNAME, String.valueOf(user.getNickname()));
        return values;
    }

    private ItemCursorWrapper queryItemsInList(String whereClause, String[] whereArgs) {
        cursor = mDatabase.query(ItemInListTable.NAME,
                null, //Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
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

    private ItemCursorWrapper queryItemsLists(String whereClause, String[] whereArgs) {
        cursor = mDatabase.query(ListTable.NAME,
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
