package ru.rsue.Karnaukhova;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class DailyProductsHost extends Fragment {
    FloatingActionButton addNewItemInListPage;
    Button menu;

    ArrayList<ItemInList> itemsInList = new ArrayList<ItemInList>();
    ListView itemsInListView;
    ItemInListAdapter itemInListAdapter;

    String[] mFilterChoices = {"Всё", "За день", "За месяц"};
    Spinner mSelectFilterSpinner;

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    Context mContext;
    SQLiteDatabase mDatabase;

    private void sort() {
        Collections.sort(itemsInList, new Comparator<ItemInList>() {
            @Override
            public int compare(ItemInList lhs, ItemInList rhs) {
                return String.valueOf(rhs.getAddDate()).compareTo(String.valueOf(lhs.getAddDate()));
            }
        }.thenComparing(new Comparator<ItemInList>() {
            @Override
            public int compare(ItemInList lhs, ItemInList rhs) {
                return String.valueOf(rhs.getIsPriority()).compareTo(String.valueOf(lhs.getIsPriority()));
            }
        }));
    }

    private void updateUI() {
        ItemStorage itemStorage = ItemStorage.get(getContext());

        mContext = getContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        mSelectFilterSpinner = getView().findViewById(R.id.filter_item_select);
        EditText itemsDateFilter = getView().findViewById(R.id.items_date_filter);
        TextView itemsMonthCost = getView().findViewById(R.id.items_month_cost);

        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, mFilterChoices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectFilterSpinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView)parent.getChildAt(0)).setTextSize(16);

                } catch (Exception ex){}

                String selectedItem = (String)parent.getItemAtPosition(position);

                if (selectedItem.equals("За день")) {
                    itemsDateFilter.setVisibility(View.VISIBLE);
                    itemsMonthCost.setVisibility(View.GONE);
                    sdf = new SimpleDateFormat("dd.MM.yyyy");

                    itemsDateFilter.setText(sdf.format(new Date()));
                    itemInListAdapter.notifyDataSetChanged();
                }
                else if (selectedItem.equals("За месяц")) {
                    itemsMonthCost.setVisibility(View.VISIBLE);
                    sdf = new SimpleDateFormat("MM.yyyy");

                    itemsDateFilter.setVisibility(View.VISIBLE);
                    itemsDateFilter.setText(sdf.format(new Date()));

                    sort();
                    itemInListAdapter.notifyDataSetChanged();
                }
                else {
                    itemsDateFilter.setVisibility(View.GONE);
                    itemsMonthCost.setVisibility(View.GONE);

                    itemsInList.clear();
                    itemsInList.addAll(itemStorage.getDailyItems());

                    sort();
                    itemInListAdapter.notifyDataSetChanged();

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
                    itemsInList.clear();
                    date = sdf.parse(s.toString());
                }
                catch (ParseException e) {
                    date = new Date();
                }
                for (ItemInList itInL : itemStorage.getDailyItems()) {
                    String firstDate = sdf.format(new Date(itInL.getAddDate()));
                    String secondDate = sdf.format(date);

                    try {
                        if (sdf.parse(firstDate).equals(sdf.parse(secondDate))) {
                            itemsInList.add(itInL);
                            monthCost = CountCost.CountCost(itInL, monthCost, mContext);
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

        itemsInListView = getView().findViewById(R.id.item_view);
        if (itemInListAdapter == null) {
            itemsInList.addAll(itemStorage.getDailyItems());
            itemInListAdapter = new ItemInListAdapter(getContext(), R.layout.daily_product_item, itemsInList);
            itemsInListView.setAdapter(itemInListAdapter);
        }
        else {
            itemInListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addNewItemInListPage = view.findViewById(R.id.add_daily_item);
        addNewItemInListPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddItemInList.class);
                startActivity(intent);
            }
        });

        menu = view.findViewById(R.id.show_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout mDrawerLayout = ((MainActivity)getActivity()).mDrawerLayout;
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_products_host, container, false);
    }
}