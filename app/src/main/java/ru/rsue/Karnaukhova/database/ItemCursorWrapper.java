package ru.rsue.Karnaukhova.database;

import static ru.rsue.Karnaukhova.database.ItemDbSchema.*;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import android.graphics.Color;
import androidx.annotation.Nullable;
import ru.rsue.Karnaukhova.*;

public class ItemCursorWrapper extends CursorWrapper {
    public ItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ItemInList getItemInList() {
        String uuidItem = getString(getColumnIndex(ItemInListTable.Cols.UUID));
        int count = getInt(getColumnIndex(ItemInListTable.Cols.COUNT));
        long addDate = getLong(getColumnIndex(ItemInListTable.Cols.ADDDATE));
        String itemId = getString(getColumnIndex(ItemInListTable.Cols.ITEMID));
        @Nullable String listId = getString(getColumnIndex(ItemInListTable.Cols.LISTID));
        int quantityBought = getInt(getColumnIndex(ItemInListTable.Cols.QUANTITYBOUGHT));
        long buyOnDate = getLong(getColumnIndex(ItemInListTable.Cols.BUYONDATE));
        int isPriority = getInt(getColumnIndex(ItemInListTable.Cols.ISPRIORITY));

        ItemInList itemInList = new ItemInList(UUID.fromString(uuidItem));
        itemInList.setCount(count);
        itemInList.setAddDate(addDate);
        itemInList.setItemId(UUID.fromString(itemId));
        try {
            itemInList.setListId(UUID.fromString(listId));
        }
        catch (Exception ex) {
            itemInList.setListId(null);
        }
        itemInList.setQuantityBought(quantityBought);
        itemInList.setBuyOnDate(buyOnDate);
        itemInList.setIsPriority(isPriority);

        return itemInList;
    }

    public Item getItem() {
        String uuidItem = getString(getColumnIndex(ItemTable.Cols.UUID));
        String weightUnitId = getString(getColumnIndex(ItemTable.Cols.WEIGHTUNITID));
        double priceForOne = getDouble(getColumnIndex(ItemTable.Cols.PRICEFORONE));
        @Nullable String color = getString(getColumnIndex(ItemTable.Cols.COLOR));
        @Nullable String userId = getString(getColumnIndex(ItemTable.Cols.USERID));
        String nameItem = getString(getColumnIndex(ItemTable.Cols.NAMEITEM));

        Item item = new Item(UUID.fromString(uuidItem));
        item.setWeightUnit(UUID.fromString(weightUnitId));
        item.setPriceForOne(priceForOne);
        item.setColor(color);
        try {
            item.setUserId(UUID.fromString(userId));
        }
        catch (Exception ex) {
            item.setUserId(null);
        }
        item.setName(nameItem);

        return item;
    }

    public WeightUnit getWeightUnit() {
        String uuidWeightUnit = getString(getColumnIndex(WeightUnitTable.Cols.UUID));
        String nameWeightUnit = getString(getColumnIndex(WeightUnitTable.Cols.NAMEWEIGHTUNIT));

        WeightUnit weightUnit = new WeightUnit(UUID.fromString(uuidWeightUnit));
        weightUnit.setName(nameWeightUnit);

        return weightUnit;
    }

    public User getUser() {
        String uuidUser = getString(getColumnIndex(UserTable.Cols.UUID));
        String login = getString(getColumnIndex(UserTable.Cols.LOGIN));
        String password = getString(getColumnIndex(UserTable.Cols.PASSWORD));
        String nickname = getString(getColumnIndex(UserTable.Cols.NICKNAME));

        User user = new User(UUID.fromString(uuidUser));
        user.setLogin(login);
        user.setPassword(password);
        user.setNickname(nickname);

        return user;
    }
}