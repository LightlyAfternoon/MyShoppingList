package ru.rsue.Karnaukhova.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;
import ru.rsue.Karnaukhova.entity.User;

public class UserRepository {
    static UserRepository sUserRepository;
    SQLiteDatabase mDatabase;
    ItemCursorWrapper mCursorWrapper;
    static ContentValues values;

    public static UserRepository get(Context context) {
        if (sUserRepository == null) {
            sUserRepository = new UserRepository(context);
        }
        return sUserRepository;
    }

    private UserRepository(Context context) {
        Context mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();
    }

    public void addUser(User user) {
        values = getContentValues(user);
        mDatabase.insert(ItemDbSchema.UserTable.NAME, null, values);
    }

    private static ContentValues getContentValues(User user) {
        values = new ContentValues();
        values.put(ItemDbSchema.UserTable.Cols.UUID, user.getUuid().toString());
        values.put(ItemDbSchema.UserTable.Cols.LOGIN, String.valueOf(user.getLogin()));
        values.put(ItemDbSchema.UserTable.Cols.PASSWORD, user.getPassword());
        values.put(ItemDbSchema.UserTable.Cols.NICKNAME, String.valueOf(user.getNickname()));
        return values;
    }
}