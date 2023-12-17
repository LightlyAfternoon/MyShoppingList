package ru.rsue.Karnaukhova.database;

import static ru.rsue.Karnaukhova.database.ItemDbSchema.*;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import ru.rsue.Karnaukhova.Item;
import ru.rsue.Karnaukhova.ItemInList;
import ru.rsue.Karnaukhova.WeightUnit;

public class ItemCursorWrapper extends CursorWrapper {
    public ItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ItemInList getItemInList() {
        String uuidItem = getString(getColumnIndex(ItemInListTable.Cols.UUID));
        int count = getInt(getColumnIndex(ItemInListTable.Cols.COUNT));
        long addDate = getLong(getColumnIndex(ItemInListTable.Cols.ADDDATE));
        int isBought = getInt(getColumnIndex(ItemInListTable.Cols.ISBOUGHT));
        String itemId = getString(getColumnIndex(ItemInListTable.Cols.ITEMID));

        ItemInList itemInList = new ItemInList(UUID.fromString(uuidItem));
        itemInList.setCount(count);
        itemInList.setAddDate(addDate);
        itemInList.setBought(isBought);
        itemInList.setItemId(UUID.fromString(itemId));

        return itemInList;
    }

    public Item getItem() {
        String uuidItem = getString(getColumnIndex(ItemTable.Cols.UUID));
        String nameItem = getString(getColumnIndex(ItemTable.Cols.NAMEITEM));
        String weightUnitId = getString(getColumnIndex(ItemTable.Cols.WEIGHTUNITID));
        double priceForOne = getDouble(getColumnIndex(ItemTable.Cols.PRICEFORONE));

        Item item = new Item(UUID.fromString(uuidItem));
        item.setName(nameItem);
        item.setWeightUnit(UUID.fromString(weightUnitId));
        item.setPriceForOne(priceForOne);

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