package ru.rsue.Karnaukhova.database;

import static ru.rsue.Karnaukhova.database.ItemDbSchema.*;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import ru.rsue.Karnaukhova.Item;
import ru.rsue.Karnaukhova.WeightUnit;

public class ItemCursorWrapper extends CursorWrapper {
    public ItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Item getItem() {
        String uuidItem = getString(getColumnIndex(ItemTable.Cols.UUID));
        String nameItem = getString(getColumnIndex(ItemTable.Cols.NAMEITEM));
        int count = getInt(getColumnIndex(ItemTable.Cols.COUNT));
        String weightUnitId = getString(getColumnIndex(ItemTable.Cols.WEIGHTUNITID));
        double priceForOne = getDouble(getColumnIndex(ItemTable.Cols.PRICEFORONE));
        long addDate = getLong(getColumnIndex(ItemTable.Cols.ADDDATE));
        int isBought = getInt(getColumnIndex(ItemTable.Cols.ISBOUGHT));

        Item item = new Item(UUID.fromString(uuidItem));
        item.setName(nameItem);
        item.setCount(count);
        item.setWeightUnit(UUID.fromString(weightUnitId));
        item.setPriceForOne(priceForOne);
        item.setAddDate(addDate);
        item.setBought(isBought);

        return item;
    }

    public WeightUnit getWeightUnit() {
        String uuidWeightUnit = getString(getColumnIndex(WeightUnitTable.Cols.UUID));
        String nameWeightUnit = getString(getColumnIndex(WeightUnitTable.Cols.NAMEWEIGHTUNIT));

        WeightUnit weightUnit = new WeightUnit(UUID.fromString(uuidWeightUnit));
        weightUnit.setName(nameWeightUnit);

        return weightUnit;
    }
}