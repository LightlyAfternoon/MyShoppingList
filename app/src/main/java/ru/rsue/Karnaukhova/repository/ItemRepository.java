package ru.rsue.Karnaukhova.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.rsue.Karnaukhova.CurrentUser;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;
import ru.rsue.Karnaukhova.dto.ItemDTO;
import ru.rsue.Karnaukhova.dto.mapper.ItemDTOMapper;
import ru.rsue.Karnaukhova.entity.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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

//    public List<Item> getItems() {
//        List<Item> items = new ArrayList<>();
//        mCursorWrapper = queryItems(ItemDbSchema.ItemTable.Cols.USERID + " = ? OR " + ItemDbSchema.ItemTable.Cols.USERID +" IS NULL", new String[] {CurrentUser.currentUser.getUuid().toString()});
//        try {
//            mCursorWrapper.moveToFirst();
//            while (!mCursorWrapper.isAfterLast()) {
//                items.add(mCursorWrapper.getItem());
//                mCursorWrapper.moveToNext();
//            }
//        } finally {
//            mCursorWrapper.close();
//        }
//
//        return items;
//    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        List<ItemDTO> itemDTOS = new ArrayList<>();

        HttpURLConnection httpURLConnection;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL("http://10.0.2.2:8080/MyShoppingListBackend/item?user=" + CurrentUser.currentUser.getUuid());

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
            itemDTOS = Arrays.asList(objectMapper.readValue(stringBuilder.toString(), ItemDTO[].class));

            for (ItemDTO itemDTO : itemDTOS) {
                items.add(ItemDTOMapper.INSTANCE.mapToEntity(itemDTO));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return items;
    }

    private static ContentValues getContentValues(Item item) {
        values = new ContentValues();
        values.put(ItemDbSchema.ItemTable.Cols.UUID, item.getUuid().toString());
        values.put(ItemDbSchema.ItemTable.Cols.NAMEITEM, String.valueOf(item.getName()));
        values.put(ItemDbSchema.ItemTable.Cols.PRICEFORONE, item.getPriceForOne());
        values.put(ItemDbSchema.ItemTable.Cols.WEIGHTUNITID, String.valueOf(item.getWeightUnitUuid()));
        values.put(ItemDbSchema.ItemTable.Cols.COLOR, String.valueOf(item.getColor()));
        values.put(ItemDbSchema.ItemTable.Cols.USERID, String.valueOf(item.getUserId()));
        return values;
    }

//    private ItemCursorWrapper queryItems(String whereClause, String[] whereArgs) {
//        Cursor cursor = mDatabase.query(ItemDbSchema.ItemTable.NAME,
//                null, //Columns - null выбирает все столбцы
//                whereClause,
//                whereArgs,
//                null,
//                null,
//                null);
//        return new ItemCursorWrapper(cursor);
//    }
}