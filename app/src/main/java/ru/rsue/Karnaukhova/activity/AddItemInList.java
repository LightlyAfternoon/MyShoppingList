package ru.rsue.Karnaukhova.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.rsue.Karnaukhova.CurrentUser;
import ru.rsue.Karnaukhova.MainActivity;
import ru.rsue.Karnaukhova.R;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;
import ru.rsue.Karnaukhova.entity.Item;
import ru.rsue.Karnaukhova.entity.ItemInList;
import ru.rsue.Karnaukhova.entity.ItemList;
import ru.rsue.Karnaukhova.repository.ItemInListRepository;
import ru.rsue.Karnaukhova.repository.ItemRepository;
import ru.rsue.Karnaukhova.repository.ListRepository;

public class AddItemInList extends AppCompatActivity {
    ItemInList mItemInList;
    Context mContext;
    SQLiteDatabase mDatabase;
    Date date;

    Item item;
    ItemList list;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_in_list);

        mContext = getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        ItemRepository itemRepository = ItemRepository.get(AddItemInList.this);
        ListRepository listRepository = ListRepository.get(AddItemInList.this);

        mItemInList = new ItemInList(UUID.randomUUID());

        List<Item> mItems = itemRepository.getItems();
        List<ItemList> mLists = listRepository.getLists();

        Spinner mItemName = findViewById(R.id.item_spinner);
        EditText mItemCount = findViewById(R.id.item_count);
        TextView mWeightUnit = findViewById(R.id.item_weight_unit);
        EditText mItemAddDate = findViewById(R.id.item_add_date);
        Spinner mListSpinner = findViewById(R.id.list_spinner);
        CheckBox mIsPriority = findViewById(R.id.is_priority);
        Button mAddItem = findViewById(R.id.add_new_item_in_list);
        RadioButton mRbtDate = findViewById(R.id.rbt_date);
        RadioButton mRbtList = findViewById(R.id.rbt_list);

        mItemCount.setMaxWidth(100);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        mItemAddDate.setText(sdf.format(new Date()));

        try {
            date = sdf.parse(mItemAddDate.getText().toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        ArrayAdapter<Item> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mItemName.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = (Item) parent.getItemAtPosition(position);

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

        ArrayAdapter<ItemList> adapterList = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mLists);
        adapterList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mListSpinner.setAdapter(adapterList);

        AdapterView.OnItemSelectedListener listSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list = (ItemList) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };

        mListSpinner.setOnItemSelectedListener(listSelectedListener);

        mRbtDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mListSpinner.setVisibility(View.GONE);
                    mItemAddDate.setVisibility(View.VISIBLE);
                }
            }
        });
        mRbtList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mListSpinner.setVisibility(View.VISIBLE);
                    mItemAddDate.setVisibility(View.GONE);
                }
            }
        });

        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mItemCount.getText().toString().isEmpty()) {
                    mItemInList.setCount(Float.parseFloat(mItemCount.getText().toString()));
                } else {
                    mItemInList.setCount(1f);
                }

                try {
                    date = sdf.parse(mItemAddDate.getText().toString());
                    mItemInList.setAddDate(new Date().getTime());
                    if (mRbtList.isChecked()) {
                        mItemInList.setListId(list.getId());
                    } else {
                        mItemInList.setBuyOnDate(date.getTime());
                    }
                    mItemInList.setItemId(item.getId());
                    mItemInList.setQuantityBought(0f);
                    mItemInList.setIsPriority(mIsPriority.isChecked());
                    mItemInList.setUserId(CurrentUser.currentUser.getUuid());

                    ItemInListRepository.get(AddItemInList.this).addItemInList(mItemInList);

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