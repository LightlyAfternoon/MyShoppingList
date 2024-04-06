package ru.rsue.Karnaukhova;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;

import java.util.List;

public class ItemAdapter extends ArrayAdapter {
    LayoutInflater mInflater;
    int mLayout;
    List<Item> mItems;
    Context mContext;
    SQLiteDatabase mDatabase;

    public ItemAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);

        mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        mItems = items;
        mLayout = resource;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(mLayout, parent, false);

        ImageView productColor = view.findViewById(R.id.product_color);

        Button deleteProduct = view.findViewById(R.id.product_delete);
        TextView nameProduct = view.findViewById(R.id.product_name);
        TextView pricePerOneMeasureProduct = view.findViewById(R.id.product_price);
        TextView countMeasureProduct = view.findViewById(R.id.product_count);
        TextView weightUnitProduct = view.findViewById(R.id.product_weight_unit);

        Item item = mItems.get(position);

        if (item.getColor() != null) {
            productColor.setBackgroundColor(Color.parseColor(item.getColor()));
        }
        nameProduct.setText(item.getName());
        pricePerOneMeasureProduct.setText(item.getPriceForOne() + " за");
        ItemCursorWrapper cursorWrapper = QueryWeightUnit.queryWeightUnit(item, mContext);
        try {
            cursorWrapper.moveToFirst();
            weightUnitProduct.setText(cursorWrapper.getString(cursorWrapper.getColumnIndex(ItemDbSchema.WeightUnitTable.Cols.NAMEWEIGHTUNIT)));
        }
        finally {
            cursorWrapper.close();
        }
        if (weightUnitProduct.getText().equals("шт.") || weightUnitProduct.getText().equals("кг") || weightUnitProduct.getText().equals("л")) {
            countMeasureProduct.setText("1");
        }
        else {
            countMeasureProduct.setText("100");
        }

        Item finalItem = item;

        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItems.remove(position);
                mDatabase.execSQL("delete from Item where uuid = '" + finalItem.getId() + "'");

                notifyDataSetChanged();
            }
        });

        return view;
    }
}