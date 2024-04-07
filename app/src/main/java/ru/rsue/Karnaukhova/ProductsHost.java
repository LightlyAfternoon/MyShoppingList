package ru.rsue.Karnaukhova;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;

import java.util.ArrayList;

public class ProductsHost extends Fragment {
    FloatingActionButton mAddItem;

    ArrayList<Item> items = new ArrayList<Item>();
    ListView itemsView;
    ItemAdapter itemAdapter;

    Context mContext;
    SQLiteDatabase mDatabase;

    private void updateUI() {
        ItemStorage itemStorage = ItemStorage.get(getContext());

        mContext = getContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        itemsView = getView().findViewById(R.id.product_view);
        if (itemAdapter == null) {
            items.addAll(itemStorage.getItems());
            itemAdapter = new ItemAdapter(getContext(), R.layout.product_item, items);
            itemsView.setAdapter(itemAdapter);
        }
        else {
            itemAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAddItem = getView().findViewById(R.id.add_product);
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddItem.class);
                startActivity(intent);
            }
        });

        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products_host, container, false);
    }
}