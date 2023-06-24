package ru.rsue.Karnaukhova;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema.WeightUnitTable;

public class ItemAdapter extends ArrayAdapter<Item> {
    LayoutInflater mInflater;
    int mLayout;
    List<Item> mItems;
    Context mContext;
    SQLiteDatabase mDatabase;
    double mCost = 0;

    public ItemAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);

        mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        mItems = items;
        mLayout = resource;
        mInflater = LayoutInflater.from(context);
    }

    double sumCost(String lastDate){
        for (Item i: mItems) {
            if (String.valueOf(i.getAddDate()).equals(lastDate)) {
                mCost = CountCost.CountCost(i, mCost, mContext);
            }
        }
        return mCost;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(mLayout, parent, false);

        View sepLine = view.findViewById(R.id.sep_line2);
        TextView allCost = view.findViewById(R.id.list_item_all_cost);
        View sepLine1 = view.findViewById(R.id.sep_line1);
        TextView nameView = view.findViewById(R.id.list_item_name);
        TextView dateView = view.findViewById(R.id.list_item_date);
        TextView countView = view.findViewById(R.id.list_item_count);
        TextView weightUnitView = view.findViewById(R.id.list_item_weight_unit);
        TextView priceView = view.findViewById(R.id.list_item_price);
        CheckBox boughtCheckBox = view.findViewById(R.id.list_item_is_bought);

        Item item = mItems.get(position);

        ItemCursorWrapper cursorWrapper = QueryWeightUnit.queryWeightUnit(item, mContext);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                weightUnitView.setText(cursorWrapper.getString(cursorWrapper.getColumnIndex(WeightUnitTable.Cols.NAMEWEIGHTUNIT)));
                cursorWrapper.moveToNext();
            }
        }
        finally {
            cursorWrapper.close();
        }

        nameView.setText(item.getName());
        countView.setText(String.valueOf(item.getCount()));
        dateView.setText(DateFormat.getDateInstance(DateFormat.FULL).format(item.getAddDate()));
        if (weightUnitView.getText().equals("шт.") || weightUnitView.getText().equals("кг") || weightUnitView.getText().equals("л")){
            priceView.setText(String.valueOf(item.getPriceForOne() * item.getCount()));
        }
        else {
            priceView.setText(String.valueOf((item.getPriceForOne() / 100) * item.getCount()));
        }

        String lastDate = "";
        String date = String.valueOf(item.getAddDate());
        int pos = position;
        if (pos != 0) {
            item = mItems.get(pos - 1);
            lastDate = String.valueOf(item.getAddDate());
            item = mItems.get(pos);
        }

        if (!date.equals(lastDate)) {
            mCost = 0;
            allCost.setText(String.valueOf(sumCost(date)));
            sepLine.setVisibility(View.VISIBLE);
            allCost.setVisibility(View.VISIBLE);
            sepLine1.setVisibility(View.VISIBLE);
            dateView.setVisibility(View.VISIBLE);
        }
        else {
            sepLine.setVisibility(View.GONE);
            allCost.setVisibility(View.GONE);
            sepLine1.setVisibility(View.GONE);
            dateView.setVisibility(View.GONE);
        }

        if (item.isBought() == 1)
            boughtCheckBox.setChecked(true);
        else
            boughtCheckBox.setChecked(false);

        Button delItem = view.findViewById(R.id.list_item_delete);
        Item finalItem = item;

        delItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItems.remove(position);
                mDatabase.execSQL("DELETE FROM Item WHERE uuid = '" + finalItem.getId() + "'");

                notifyDataSetChanged();
            }
        });

        boughtCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    finalItem.setBought(1);
                    mDatabase.execSQL("UPDATE Item" +
                            " SET isBought = '" + 1 +
                            "' WHERE uuid = '" + finalItem.getId() + "'");
                }
                else {
                    finalItem.setBought(0);
                    mDatabase.execSQL("UPDATE Item" +
                            " SET isBought = '" + 0 +
                            "' WHERE uuid = '" + finalItem.getId() + "'");
                }

                notifyDataSetChanged();
            }
        });
        return view;
    }
}