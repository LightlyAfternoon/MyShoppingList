package ru.rsue.Karnaukhova.database;

import static ru.rsue.Karnaukhova.database.ItemDbSchema.*;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.math.BigDecimal;
import java.util.UUID;

import ru.rsue.Karnaukhova.Item;

public class ItemCursorWrapper extends CursorWrapper {
    public ItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Item getItem() {
        String uuidString = getString(getColumnIndex(ItemTable.Cols.UUID));
        String name = getString(getColumnIndex(ItemTable.Cols.NAMEITEM));
        int count = getInt(getColumnIndex(ItemTable.Cols.COUNT));
        String weightUnit = getString(getColumnIndex(ItemTable.Cols.WEIGHTUNIT));
        double priceForOne = getDouble(getColumnIndex(ItemTable.Cols.PRICEFORONE));
        long addDate = getLong(getColumnIndex(ItemTable.Cols.ADDDATE));

        Item item = new Item(UUID.fromString(uuidString));
        item.setName(name);
        item.setCount(count);
        item.setWeightUnit(weightUnit);
        item.setPriceForOne(priceForOne);
        item.setAddDate(addDate);

        return item;
    }
}
