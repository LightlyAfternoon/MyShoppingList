package ru.rsue.Karnaukhova;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;
import ru.rsue.Karnaukhova.entity.Item;
import ru.rsue.Karnaukhova.entity.WeightUnit;
import ru.rsue.Karnaukhova.repository.ItemRepository;
import ru.rsue.Karnaukhova.repository.WeightUnitRepository;

public class

AddItem extends AppCompatActivity {
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

        WeightUnitRepository weightUnitRepository = WeightUnitRepository.get(AddItem.this);

        mItem = new Item(UUID.randomUUID());

        List<WeightUnit> mWeightUnits = weightUnitRepository.getWeightUnits();
        ArrayList<String> mWeightUnitsNames = new ArrayList<>();
        for (WeightUnit weightUnit : mWeightUnits) {
            mWeightUnitsNames.add(weightUnit.getName());
        }
        EditText mItemNameEditText = findViewById(R.id.item_name);
        Spinner mWeightUnitSpinner = findViewById(R.id.item_weight_unit_spinner);
        EditText mItemPriceForOne = findViewById(R.id.item_price_for_one);
        Button mAddItem = findViewById(R.id.add_new_item);

        RadioButton mGrayColor = findViewById(R.id.gray_color);
        RadioButton mBlackColor = findViewById(R.id.black_color);
        RadioButton mGreenColor = findViewById(R.id.green_color);
        RadioButton mBlueColor = findViewById(R.id.blue_color);
        RadioButton mRedColor = findViewById(R.id.red_color);
        RadioButton mPurpleColor = findViewById(R.id.purple_color);
        RadioButton mYellowColor = findViewById(R.id.yellow_color);
        RadioButton mOrangeColor = findViewById(R.id.orange_color);

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

                if (mGrayColor.isChecked())
                {
                    mItem.setColor("#808080");
                }
                else if (mBlackColor.isChecked())
                {
                    mItem.setColor("#000000");
                }
                else if (mGreenColor.isChecked())
                {
                    mItem.setColor("#A4C639");
                }
                else if (mBlueColor.isChecked())
                {
                    mItem.setColor("#0000FF");
                }
                else if (mRedColor.isChecked())
                {
                    mItem.setColor("#FF0000");
                }
                else if (mPurpleColor.isChecked())
                {
                    mItem.setColor("#800080");
                }
                else if (mYellowColor.isChecked())
                {
                    mItem.setColor("#FFFF00");
                }
                else if (mOrangeColor.isChecked())
                {
                    mItem.setColor("#FFA500");
                }
                else {
                    mItem.setColor("#FFFFFF");
                }

                mItem.setUserId(CurrentUser.currentUser.getUuid());

                ItemRepository.get(AddItem.this).addItem(mItem);

                Toast toast = new Toast(AddItem.this);
                toast.setText("Добавлено");
                toast.show();

                finish();
            }
        });
    }
}