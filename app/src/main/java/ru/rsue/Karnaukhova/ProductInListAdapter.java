package ru.rsue.Karnaukhova;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;
import ru.rsue.Karnaukhova.database.ItemDbSchema.WeightUnitTable;
import ru.rsue.Karnaukhova.entity.Item;
import ru.rsue.Karnaukhova.entity.ItemInList;
import ru.rsue.Karnaukhova.entity.ItemList;
import ru.rsue.Karnaukhova.entity.WeightUnit;
import ru.rsue.Karnaukhova.repository.WeightUnitRepository;

import java.util.List;

public class ProductInListAdapter extends ArrayAdapter<ItemInList> {
    LayoutInflater mInflater;
    int mLayout;
    List<ItemInList> mItemsInList;
    Context mContext;
    SQLiteDatabase mDatabase;
    double mCost = 0;

    public ProductInListAdapter(Context context, int resource, List<ItemInList> itemsInList) {
        super(context, resource, itemsInList);

        mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        mItemsInList = itemsInList;
        mLayout = resource;
        mInflater = LayoutInflater.from(context);
    }

    double sumCost(String lastList){
        for (ItemInList i: mItemsInList) {
            if (String.valueOf(i.getListId()).equals(lastList)) {
                mCost = CountCost.CountCost(i, mCost, mContext);
            }
        }
        return mCost;
    }

    ItemCursorWrapper queryItemWithUUID(String uuid) {
        Cursor cursor = mDatabase.query(ItemDbSchema.ItemTable.NAME,
                null,
                ItemDbSchema.ItemTable.Cols.UUID + " = ?",
                new String[]{uuid},
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }

    ItemCursorWrapper queryListWithUUID(String uuid) {
        Cursor cursor = mDatabase.query(ItemDbSchema.ListTable.NAME,
                null,
                ItemDbSchema.ListTable.Cols.UUID + " = ?",
                new String[]{uuid},
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(mLayout, parent, false);

        ImageView itemColor = view.findViewById(R.id.product_list_color);

        View sepLine = view.findViewById(R.id.sep_line3);
        TextView allCost = view.findViewById(R.id.product_list_all_cost);
        View sepLine1 = view.findViewById(R.id.sep_line4);
        TextView nameView = view.findViewById(R.id.product_list_name);
        TextView listView = view.findViewById(R.id.product_list);
        TextView countView = view.findViewById(R.id.product_list_count);
        TextView weightUnitView = view.findViewById(R.id.product_list_weight_unit);
        TextView priceView = view.findViewById(R.id.product_list_price);
        CheckBox boughtCheckBox = view.findViewById(R.id.product_list_is_bought);

        ItemInList itemInList = mItemsInList.get(position);
        ItemCursorWrapper cursorWrapperItem = queryItemWithUUID(itemInList.getItemId().toString());

        Item item;
        try {
            cursorWrapperItem.moveToFirst();
            item = (cursorWrapperItem.getItem());
        }
        finally {
            cursorWrapperItem.close();
        }

        ItemList list;
        try {
            ItemCursorWrapper listCursorWrapper = queryListWithUUID(itemInList.getListId().toString());
            listCursorWrapper.moveToFirst();
            list = (listCursorWrapper.getItemList());
            listCursorWrapper.close();
        }
        finally {
            cursorWrapperItem.close();
        }

        WeightUnit weightUnit = WeightUnitRepository.get(mContext).getWeightUnitOfItem(item);
        weightUnitView.setText(weightUnit.getName());

        if (item.getColor() != null) {
            itemColor.setBackgroundColor(Color.parseColor(item.getColor()));
        }

        nameView.setText(item.getName());
        if (itemInList.getIsPriority()) {
            nameView.setTypeface(Typeface.DEFAULT_BOLD);
            nameView.setTextColor(Color.parseColor("#FF0000"));
        }
        countView.setText(String.valueOf(itemInList.getCount()));
        listView.setText(list.getListName());
        if (weightUnitView.getText().equals("шт.") || weightUnitView.getText().equals("кг") || weightUnitView.getText().equals("л")){
            priceView.setText(String.valueOf(item.getPriceForOne() * itemInList.getCount()));
        }
        else {
            priceView.setText(String.valueOf((item.getPriceForOne() / 100) * itemInList.getCount()));
        }

        String lastList = "";
        String listId = String.valueOf(itemInList.getListId());
        int pos = position;
        if (pos != 0) {
            itemInList = mItemsInList.get(pos - 1);
            lastList = String.valueOf(itemInList.getListId());
            itemInList = mItemsInList.get(pos);
        }

        if (!listId.equals(lastList)) {
            mCost = 0;
            allCost.setText(String.valueOf(sumCost(listId)));
            sepLine.setVisibility(View.VISIBLE);
            allCost.setVisibility(View.VISIBLE);
            sepLine1.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
        else {
            sepLine.setVisibility(View.GONE);
            allCost.setVisibility(View.GONE);
            sepLine1.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
        }

        if (itemInList.getQuantityBought() > 0)
            boughtCheckBox.setChecked(true);
        else
            boughtCheckBox.setChecked(false);

        Button delItem = view.findViewById(R.id.product_list_item_delete);
        ItemInList finalItemInList = itemInList;

        delItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemsInList.remove(position);
                mDatabase.execSQL("DELETE FROM ItemInList WHERE uuid = '" + finalItemInList.getId() + "'");

                notifyDataSetChanged();
            }
        });

        boughtCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // need to upgrade
                    finalItemInList.setQuantityBought(finalItemInList.getCount());
                    mDatabase.execSQL("UPDATE ItemInList" +
                            " SET quantityBought = '" + finalItemInList.getCount() +
                            "' WHERE uuid = '" + finalItemInList.getId() + "'");
                }
                else {
                    finalItemInList.setQuantityBought(0);
                    mDatabase.execSQL("UPDATE ItemInList" +
                            " SET quantityBought = '" + 0 +
                            "' WHERE uuid = '" + finalItemInList.getId() + "'");
                }

                notifyDataSetChanged();
            }
        });
        return view;
    }
}