package ru.rsue.Karnaukhova;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import ru.rsue.Karnaukhova.entity.ItemInList;
import ru.rsue.Karnaukhova.repository.ItemStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListsPurchasesHost extends Fragment {
    FloatingActionButton addNewItemInListPage;
    Button menu;

    ArrayList<ItemInList> itemsInList = new ArrayList<ItemInList>();
    ListView itemsInListView;
    ProductInListAdapter itemInListAdapter;

    Context mContext;
    SQLiteDatabase mDatabase;

    private void sort() {
        Collections.sort(itemsInList, new Comparator<ItemInList>() {
            @Override
            public int compare(ItemInList lhs, ItemInList rhs) {
                return String.valueOf(rhs.getListId()).compareTo(String.valueOf(lhs.getListId()));
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

        itemsInListView = getView().findViewById(R.id.item_list_view);
        if (itemInListAdapter == null) {
            itemsInList.addAll(itemStorage.getListsItems());
            itemInListAdapter = new ProductInListAdapter(getContext(), R.layout.product_in_list_item, itemsInList);
            itemsInListView.setAdapter(itemInListAdapter);

            sort();
        }
        else {
            itemInListAdapter.notifyDataSetChanged();
        }
    }

    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addNewItemInListPage = view.findViewById(R.id.add_item_in_list);
        addNewItemInListPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddItemInList.class);
                startActivity(intent);
            }
        });

        menu = view.findViewById(R.id.show_menu4);
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
        return inflater.inflate(R.layout.fragment_lists_purchases_host, container, false);
    }
}