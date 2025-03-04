package ru.rsue.Karnaukhova.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;
import ru.rsue.Karnaukhova.dto.WeightUnitDTO;
import ru.rsue.Karnaukhova.entity.Item;
import ru.rsue.Karnaukhova.entity.WeightUnit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WeightUnitRepository {
    static WeightUnitRepository sWeightUnitRepository;
    SQLiteDatabase mDatabase;
    ItemCursorWrapper mCursorWrapper;
    static ContentValues values;

    public static WeightUnitRepository get(Context context) {
        if (sWeightUnitRepository == null) {
            sWeightUnitRepository = new WeightUnitRepository(context);
        }
        return sWeightUnitRepository;
    }

    private WeightUnitRepository(Context context) {
        Context mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();
    }

//    public WeightUnit getWeightUnitOfItem(Item it) {
//        WeightUnit weightUnit = null;
//        mCursorWrapper = queryWeightUnit(it);
//        try {
//            mCursorWrapper.moveToFirst();
//            weightUnit = mCursorWrapper.getWeightUnit();
//        } finally {
//            mCursorWrapper.close();
//        }
//
//        return weightUnit;
//    }

    public WeightUnitDTO getWeightUnitOfItem(Item it) {
        WeightUnitDTO weightUnitDTO = null;

        HttpURLConnection httpURLConnection;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL("http://10.0.2.2:8080/MyShoppingListBackend/weight_unit/" + it.getWeightUnitUuid());

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
            weightUnitDTO = objectMapper.readValue(stringBuilder.toString(), WeightUnitDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return weightUnitDTO;
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

    private ItemCursorWrapper queryAllWeightUnits(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(ItemDbSchema.WeightUnitTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }

    private ItemCursorWrapper queryWeightUnit(Item it) {
        Cursor cursor = mDatabase.query(ItemDbSchema.WeightUnitTable.NAME,
                null,
                ItemDbSchema.WeightUnitTable.Cols.UUID + " = ?",
                new String[]{it.getWeightUnitUuid().toString()},
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }
}