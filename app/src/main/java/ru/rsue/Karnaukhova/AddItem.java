package ru.rsue.Karnaukhova;

import android.content.ContentValues;
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

public class AddItem extends AppCompatActivity {
    Item mItem;
    Context mContext;
    SQLiteDatabase mDatabase;

    String weightUnit;

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
        setContentView(R.layout.add_item);

        mContext = getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        ItemStorage itemStorage = ItemStorage.get(AddItem.this);

        mItem = new Item(UUID.randomUUID());

        List<WeightUnit> mWeightUnits = itemStorage.getWeightUnits();
        ArrayList<String> mWeightUnitsNames = new ArrayList<>();
        for (WeightUnit weightUnit : mWeightUnits) {
            mWeightUnitsNames.add(weightUnit.getName());
        }
        EditText mItemNameEditText = findViewById(R.id.item_name);
        Spinner mWeightUnitSpinner = findViewById(R.id.item_weight_unit_spinner);
        EditText mItemPriceForOne = findViewById(R.id.item_price_for_one);
        Button mAddItem = findViewById(R.id.add_new_item);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mWeightUnitsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mWeightUnitSpinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weightUnit = (String) parent.getItemAtPosition(position);
                TextView textViewPriceForSmth = findViewById(R.id.tv_price_for_smth);

                if (weightUnit.equals("шт.")) {
                    textViewPriceForSmth.setText("Цена за 1 шт.");
                } else if (weightUnit.equals("г")) {
                    textViewPriceForSmth.setText("Цена за 100 г");
                } else if (weightUnit.equals("мл")) {
                    textViewPriceForSmth.setText("Цена за 100 мл");
                } else if (weightUnit.equals("кг")) {
                    textViewPriceForSmth.setText("Цена за 1 кг");
                } else if (weightUnit.equals("л")) {
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
                mItem.setName((mItemNameEditText.getText().toString()));

                if (!mItemPriceForOne.getText().toString().isEmpty()) {
                    mItem.setPriceForOne(Double.parseDouble(mItemPriceForOne.getText().toString()));
                } else {
                    mItem.setPriceForOne(0);
                }

                ItemCursorWrapper cursor = queryWeightUnitWithName(weightUnit);
                try {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        mItem.setWeightUnit(cursor.getWeightUnit().getId());
                        cursor.moveToNext();
                    }
                } finally {
                    cursor.close();
                }

                mItem.setColor("#D3D3D3");
                mItem.setUserId(CurrentUser.currentUser.getUuid());

                ItemStorage.get(AddItem.this).addItem(mItem);

                Toast toast = new Toast(AddItem.this);
                toast.setText("Добавлено");
                toast.show();

                Intent intent = new Intent(AddItem.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}