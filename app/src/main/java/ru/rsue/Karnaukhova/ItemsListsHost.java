package ru.rsue.Karnaukhova;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;

import java.util.ArrayList;

public class ItemsListsHost extends Fragment {
    FloatingActionButton mAddList;
    Button menu;

    ArrayList<ItemList> lists = new ArrayList<ItemList>();
    ListView itemsListsView;
    ItemsListAdapter listAdapter;

    Context mContext;
    SQLiteDatabase mDatabase;

    private void updateUI() {
        ItemStorage itemStorage = ItemStorage.get(getContext());

        mContext = getContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        itemsListsView = getView().findViewById(R.id.list_view);
        if (listAdapter == null) {
            lists.addAll(itemStorage.getLists());
            listAdapter = new ItemsListAdapter(getContext(), R.layout.list_item, lists);
            itemsListsView.setAdapter(listAdapter);
        }
        else {
            listAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAddList = getView().findViewById(R.id.add_list);
        mAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddList.class);
                startActivity(intent);
            }
        });

        menu = view.findViewById(R.id.show_menu3);
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
        return inflater.inflate(R.layout.fragment_items_lists_host, container, false);
    }
}