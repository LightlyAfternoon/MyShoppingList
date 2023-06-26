package ru.rsue.Karnaukhova;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;

public class AddItemInList extends AppCompatActivity {
    Item mItem;
    Context mContext;
    SQLiteDatabase mDatabase;
    Date date;

    String item;

    ItemCursorWrapper queryWeightUnitWithName(String name) {
        Cursor cursor = mDatabase.query(ItemDbSchema.WeightUnitTable.NAME,
                null,
                ItemDbSchema.WeightUnitTable.Cols.NAMEWEIGHTUNIT + " = ?",
                new String[]{name},
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_in_list);

        mContext = getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        ItemStorage itemStorage = ItemStorage.get(AddItemInList.this);

        mItem = new Item(UUID.randomUUID());

        List<WeightUnit> mWeightUnits = itemStorage.getWeightUnits();
        ArrayList<String> mWeightUnitsNames = new ArrayList<>();
        for (WeightUnit weightUnit : mWeightUnits) {
            mWeightUnitsNames.add(weightUnit.getName());
        }
        EditText mItemName = findViewById(R.id.item_name);
        EditText mItemCount = findViewById(R.id.item_count);
        Spinner mWeightUnitSpinner = findViewById(R.id.item_weight_unit_spinner);
        EditText mItemPriceForOne = findViewById(R.id.item_price_for_one);
        EditText mItemAddDate = findViewById(R.id.item_add_date);
        Button mAddItem = findViewById(R.id.add_new_item_in_list);

        mItemCount.setMaxWidth(100);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        mItemAddDate.setText(sdf.format(new Date()));

        try {
            date = sdf.parse(mItemAddDate.getText().toString());
            mItem.setAddDate(date.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mWeightUnitsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mWeightUnitSpinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = (String) parent.getItemAtPosition(position);
                TextView textViewPriceForSmth = findViewById(R.id.tv_price_for_smth);

                if (item.equals("шт.")) {
                    textViewPriceForSmth.setText("Цена за 1 шт.");
                } else if (item.equals("г")) {
                    textViewPriceForSmth.setText("Цена за 100 г");
                } else if (item.equals("мл")) {
                    textViewPriceForSmth.setText("Цена за 100 мл");
                } else if (item.equals("кг")) {
                    textViewPriceForSmth.setText("Цена за 1 кг");
                } else if (item.equals("л")) {
                    textViewPriceForSmth.setText("Цена за 1 л");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        mWeightUnitSpinner.setOnItemSelectedListener(itemSelectedListener);

        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItem.setName(mItemName.getText().toString());

                if (!mItemCount.getText().toString().isEmpty()) {
                    mItem.setCount(Integer.parseInt(mItemCount.getText().toString()));
                } else {
                    mItem.setCount(1);
                }

                if (!mItemPriceForOne.getText().toString().isEmpty()) {
                    mItem.setPriceForOne(Double.parseDouble(mItemPriceForOne.getText().toString()));
                } else {
                    mItem.setPriceForOne(0);
                }

                try {
                    date = sdf.parse(mItemAddDate.getText().toString());
                    mItem.setAddDate(date.getTime());

                    ItemCursorWrapper cursor = queryWeightUnitWithName(item);
                    try {
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            mItem.setWeightUnit(cursor.getWeightUnit().getId());
                            cursor.moveToNext();
                        }
                    } finally {
                        cursor.close();
                    }

                    mItem.setBought(0);

                    ItemStorage.get(AddItemInList.this).addItem(mItem);

                    Intent intent = new Intent(AddItemInList.this, MainActivity.class);
                    startActivity(intent);
                }
                catch (ParseException ex) {
                    Toast.makeText(mContext, "Введите корректную дату", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}