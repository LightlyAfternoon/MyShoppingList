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
    ItemInList mItemInList;
    Context mContext;
    SQLiteDatabase mDatabase;
    Date date;

    String itemName;
    Item item;

    ItemCursorWrapper queryItemWithName(String name) {
        Cursor cursor = mDatabase.query(ItemDbSchema.ItemTable.NAME,
                null,
                ItemDbSchema.ItemTable.Cols.NAMEITEM + " = ?",
                new String[]{name},
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }

    ItemCursorWrapper queryWeightUnitWithUUID(String uuid) {
        Cursor cursor = mDatabase.query(ItemDbSchema.WeightUnitTable.NAME,
                null,
                ItemDbSchema.WeightUnitTable.Cols.UUID + " = ?",
                new String[]{uuid},
                null,
                null,
                null);
        return new ItemCursorWrapper(cursor);
    }



    //need more set



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_in_list);

        mContext = getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        ItemStorage itemStorage = ItemStorage.get(AddItemInList.this);

        mItemInList = new ItemInList(UUID.randomUUID());

        List<Item> mItems = itemStorage.getItems();
        ArrayList<String> mItemNames = new ArrayList<>();
        for (Item item : mItems) {
            mItemNames.add(item.getName());
        }

        Spinner mItemName = findViewById(R.id.item_spinner);
        EditText mItemCount = findViewById(R.id.item_count);
        TextView mWeightUnit = findViewById(R.id.item_weight_unit);
        EditText mItemAddDate = findViewById(R.id.item_add_date);
        Button mAddItem = findViewById(R.id.add_new_item_in_list);

        mItemCount.setMaxWidth(100);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        mItemAddDate.setText(sdf.format(new Date()));

        try {
            date = sdf.parse(mItemAddDate.getText().toString());
            mItemInList.setAddDate(date.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItemNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mItemName.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemName = (String) parent.getItemAtPosition(position);

                ItemCursorWrapper itemCursor = queryItemWithName(itemName);
                try {
                    itemCursor.moveToFirst();
                    while (!itemCursor.isAfterLast()) {
                        item = itemCursor.getItem();
                        itemCursor.moveToNext();
                    }
                } finally {
                    itemCursor.close();
                }

                ItemCursorWrapper weightUnitCursor = queryWeightUnitWithUUID(item.getWeightUnit().toString());
                try {
                    weightUnitCursor.moveToFirst();
                    while (!weightUnitCursor.isAfterLast()) {
                        mWeightUnit.setText(weightUnitCursor.getWeightUnit().getName());
                        weightUnitCursor.moveToNext();
                    }
                } finally {
                    weightUnitCursor.close();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        mItemName.setOnItemSelectedListener(itemSelectedListener);

        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mItemCount.getText().toString().isEmpty()) {
                    mItemInList.setCount(Integer.parseInt(mItemCount.getText().toString()));
                } else {
                    mItemInList.setCount(1);
                }

                try {
                    date = sdf.parse(mItemAddDate.getText().toString());
                    mItemInList.setAddDate(date.getTime());

                    ItemCursorWrapper cursor = queryItemWithName(itemName);
                    try {
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            mItemInList.setItemId(cursor.getItem().getId());
                            cursor.moveToNext();
                        }
                    } finally {
                        cursor.close();
                    }

                    mItemInList.setQuantityBought(0);

                    ItemStorage.get(AddItemInList.this).addItemInList(mItemInList);

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