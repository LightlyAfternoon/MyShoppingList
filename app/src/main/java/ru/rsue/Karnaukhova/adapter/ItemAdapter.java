package ru.rsue.Karnaukhova.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ru.rsue.Karnaukhova.R;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;
import ru.rsue.Karnaukhova.dto.mapper.WeightUnitDTOMapper;
import ru.rsue.Karnaukhova.entity.Item;
import ru.rsue.Karnaukhova.entity.WeightUnit;
import ru.rsue.Karnaukhova.repository.WeightUnitRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

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
        WeightUnit weightUnit = null;
        try {
            weightUnit = WeightUnitDTOMapper.INSTANCE.mapToEntity(Executors.newSingleThreadExecutor().submit(() -> WeightUnitRepository.get(mContext).getWeightUnitOfItem(item)).get());
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        weightUnitProduct.setText(weightUnit.getName());
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
                Cursor cursor = mDatabase.rawQuery("select * from " + ItemDbSchema.ItemInListTable.NAME +
                        " where " + ItemDbSchema.ItemInListTable.Cols.ITEMID + " = '" + finalItem.getUuid() + "'", null);
                if (cursor.getCount() == 0) {
                    mItems.remove(position);
                    mDatabase.execSQL("delete from " + ItemDbSchema.ItemTable.NAME +
                            " where " + ItemDbSchema.ItemTable.Cols.UUID + " = '" + finalItem.getUuid() + "'");
                }
                else {
                    Toast.makeText(getContext(), "Сперва необходимо удалить " + finalItem.getName() + " из списков", Toast.LENGTH_LONG).show();
                }

                notifyDataSetChanged();
            }
        });

        return view;
    }
}