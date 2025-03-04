package ru.rsue.Karnaukhova.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.rsue.Karnaukhova.CurrentUser;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;
import ru.rsue.Karnaukhova.dto.ItemDTO;
import ru.rsue.Karnaukhova.dto.ItemInListDTO;
import ru.rsue.Karnaukhova.dto.mapper.ItemDTOMapper;
import ru.rsue.Karnaukhova.dto.mapper.ItemInListDTOMapper;
import ru.rsue.Karnaukhova.entity.Item;
import ru.rsue.Karnaukhova.entity.ItemInList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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

//    public List<ItemInList> getDailyItems() {
//        List<ItemInList> itemsInList = new ArrayList<>();
//
//        mCursorWrapper = queryItemsInList(ItemDbSchema.ItemInListTable.Cols.USERID + " = ? AND " + ItemDbSchema.ItemInListTable.Cols.LISTID + " = 'null'", new String[] {CurrentUser.currentUser.getUuid().toString()});
//        try {
//            mCursorWrapper.moveToFirst();
//            while (!mCursorWrapper.isAfterLast()) {
//                itemsInList.add(mCursorWrapper.getItemInList());
//                mCursorWrapper.moveToNext();
//            }
//        } finally {
//            mCursorWrapper.close();
//        }
//
//        return itemsInList;
//    }

    public List<ItemInList> getDailyItems() {
        List<ItemInList> itemsInLists = new ArrayList<>();
        List<ItemInListDTO> itemsInListsDTOS = new ArrayList<>();

        HttpURLConnection httpURLConnection;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL("http://10.0.2.2:8080/MyShoppingListBackend/list/0/item?date=all");

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setRequestMethod("GET");

            httpURLConnection.connect();

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
            }

            httpURLConnection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            itemsInListsDTOS = Arrays.asList(objectMapper.readValue(stringBuilder.toString(), ItemInListDTO[].class));

            for (ItemInListDTO itemInListDTO : itemsInListsDTOS) {
                itemsInLists.add(ItemInListDTOMapper.INSTANCE.mapToEntity(itemInListDTO));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return itemsInLists;
    }

    public List<ItemInList> getListsItems() {
        List<ItemInList> itemsInList = new ArrayList<>();

        mCursorWrapper = queryItemsInList(ItemDbSchema.ItemInListTable.Cols.USERID + " = ? AND" + ItemDbSchema.ItemInListTable.Cols.LISTID + " != 'null'", new String[] {CurrentUser.currentUser.getUuid().toString()});
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

        values.put(ItemDbSchema.ItemInListTable.Cols.UUID, itemInList.getUuid().toString());
        values.put(ItemDbSchema.ItemInListTable.Cols.COUNT, String.valueOf(itemInList.getCount()));
        values.put(ItemDbSchema.ItemInListTable.Cols.ADDDATE, String.valueOf(itemInList.getAddDate()));
        values.put(ItemDbSchema.ItemInListTable.Cols.ITEMID, itemInList.getItemId().toString());
        values.put(ItemDbSchema.ItemInListTable.Cols.LISTID, String.valueOf(itemInList.getListId()));
        values.put(ItemDbSchema.ItemInListTable.Cols.QUANTITYBOUGHT, String.valueOf(itemInList.getQuantityBought()));
        values.put(ItemDbSchema.ItemInListTable.Cols.BUYONDATE, String.valueOf(itemInList.getBuyOnDate()));
        if (itemInList.getIsPriority()) {
            values.put(ItemDbSchema.ItemInListTable.Cols.ISPRIORITY, 1);
        } else {
            values.put(ItemDbSchema.ItemInListTable.Cols.ISPRIORITY, 0);
        }
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