package ru.rsue.Karnaukhova;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;

public class ItemAdapter extends ArrayAdapter<Item> {
    private LayoutInflater mInflater;
    private int mLayout;
    private List<Item> mItems;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    double Cost = 0;

    double sumCost(String ld){
        for (Item i: mItems) {
            if (String.valueOf(i.getAddDate()).equals(ld)) {
                if (i.getWeightUnit().equals("шт.") || i.getWeightUnit().equals("кг.") || i.getWeightUnit().equals("л.")){
                    Cost += i.getPriceForOne() * i.getCount();
                }
                else {
                    Cost += (i.getPriceForOne() / 100) * i.getCount();
                }
            }
        }

        return Cost;
    }

    public ItemAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);

        mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        mItems = items;
        mLayout = resource;
        mInflater = LayoutInflater.from(context);
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

        Item item = mItems.get(position);

        nameView.setText(item.getName());
        countView.setText(String.valueOf(item.getCount()));
        weightUnitView.setText(item.getWeightUnit());
        dateView.setText(DateFormat.getDateInstance(DateFormat.FULL).format(item.getAddDate()));
        if (item.getWeightUnit().equals("шт.") || item.getWeightUnit().equals("кг.") || item.getWeightUnit().equals("л.")){
            priceView.setText(String.valueOf(item.getPriceForOne() * item.getCount()));
        }
        else {
            priceView.setText(String.valueOf((item.getPriceForOne() / 100) * item.getCount()));
        }

        String lastDate = "";
        String date =String.valueOf(item.getAddDate());
        int pos = position;
        if (pos != 0) {
            item = mItems.get(pos - 1);
            lastDate = String.valueOf(item.getAddDate());
            item = mItems.get(pos);
        }

        if (!date.equals(lastDate)) {
            Cost = 0;
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

        return view;
    }
}
