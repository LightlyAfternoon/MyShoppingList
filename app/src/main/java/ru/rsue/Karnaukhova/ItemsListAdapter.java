package ru.rsue.Karnaukhova;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;

import java.util.List;

public class ItemsListAdapter extends ArrayAdapter {
    LayoutInflater mInflater;
    int mLayout;
    List<ItemList> mItemsLists;
    Context mContext;
    SQLiteDatabase mDatabase;

    public ItemsListAdapter(Context context, int resource, List<ItemList> lists) {
        super(context, resource, lists);

        mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        mItemsLists = lists;
        mLayout = resource;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(mLayout, parent, false);

        Button deleteList = view.findViewById(R.id.list_delete);
        TextView nameList = view.findViewById(R.id.list_name);

        ItemList itemsList = mItemsLists.get(position);

        nameList.setText(itemsList.getListName());

        ItemList finalList = itemsList;

        deleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = mDatabase.rawQuery("select * from ItemInList where listId = '" + finalList.getId() + "'", null);
                if (cursor.getCount() == 0) {
                    mItemsLists.remove(position);
                    mDatabase.execSQL("delete from List where uuid = '" + finalList.getId() + "'");
                }
                else {
                    Toast.makeText(getContext(), "Сперва необходимо удалить всё из списка", Toast.LENGTH_LONG).show();
                }

                notifyDataSetChanged();
            }
        });

        return view;
    }
}