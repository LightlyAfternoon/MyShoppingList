package ru.rsue.Karnaukhova;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import ru.rsue.Karnaukhova.database.ItemBaseHelper;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton mAddItem;

    ArrayList<Item> items = new ArrayList<Item>();
    ListView itemsView;
    ItemAdapter itemAdapter;

    String[] mFilterChoices = {"Всё", "За день", "За месяц"};
    Spinner mSelectFilterSpinner;

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    Context mContext;
    SQLiteDatabase mDatabase;

    private void sort() {
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item lhs, Item rhs) {
                return String.valueOf(rhs.getAddDate()).compareTo(String.valueOf(lhs.getAddDate()));
            }
        });
    }

    private void updateUI() {
        ItemStorage itemStorage = ItemStorage.get(MainActivity.this);

        mContext = getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        mSelectFilterSpinner = findViewById(R.id.filter_item_select);
        EditText itemsDateFilter = findViewById(R.id.items_date_filter);
        TextView itemsMonthCost = findViewById(R.id.items_month_cost);

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mFilterChoices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectFilterSpinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView)parent.getChildAt(0)).setTextSize(16);

                String selectedItem = (String)parent.getItemAtPosition(position);

                if (selectedItem.equals("За день")) {
                    itemsDateFilter.setVisibility(View.VISIBLE);
                    itemsMonthCost.setVisibility(View.GONE);
                    sdf = new SimpleDateFormat("dd.MM.yyyy");

                    itemsDateFilter.setText(sdf.format(new Date()));
                    itemAdapter.notifyDataSetChanged();
                }
                else if (selectedItem.equals("За месяц")) {
                    itemsMonthCost.setVisibility(View.VISIBLE);
                    sdf = new SimpleDateFormat("MM.yyyy");

                    itemsDateFilter.setVisibility(View.VISIBLE);
                    itemsDateFilter.setText(sdf.format(new Date()));

                    sort();
                    itemAdapter.notifyDataSetChanged();
                }
                else {
                    itemsDateFilter.setVisibility(View.GONE);
                    itemsMonthCost.setVisibility(View.GONE);

                    items.clear();
                    items.addAll(itemStorage.getItems());

                    sort();
                    itemAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        mSelectFilterSpinner.setOnItemSelectedListener(itemSelectedListener);

        itemsDateFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Date date = null;
                double monthCost = 0;
                try {
                    items.clear();
                    date = sdf.parse(s.toString());
                }
                catch (ParseException e) {
                    date = new Date();
                }
                for (Item it : itemStorage.getItems()) {
                    String firstDate = sdf.format(new Date(it.getAddDate()));
                    String secondDate = sdf.format(date);

                    try {
                        if (sdf.parse(firstDate).equals(sdf.parse(secondDate))) {
                            items.add(it);
                            monthCost = CountCost.CountCost(it, monthCost, mContext);
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }

                itemsMonthCost.setText(String.valueOf(monthCost));
            }

            @Override
            public void afterTextChanged(Editable s) {
                sort();
            }
        });

        itemsView = findViewById(R.id.item_view);
        if (itemAdapter == null) {
            items.addAll(itemStorage.getItems());
            itemAdapter = new ItemAdapter(this, R.layout.list_item, items);
            itemsView.setAdapter(itemAdapter);
        }
        else {
            itemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateUI();

        mAddItem = (FloatingActionButton)findViewById(R.id.add_item);
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItemInList.class);
                startActivity(intent);
            }
        });
    }
}