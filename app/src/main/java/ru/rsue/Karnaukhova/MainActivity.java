package ru.rsue.Karnaukhova;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mAddItem;

    ArrayList<Item> items = new ArrayList<Item>();
    ListView itemsView;
    ItemAdapter itemAdapter;

    private void updateUI() {
        ItemStorage itemStorage = ItemStorage.get(MainActivity.this);

        itemsView = findViewById(R.id.item_view);
        if (itemAdapter == null) {
            items.addAll(itemStorage.getItems());
            itemAdapter = new ItemAdapter(this, R.layout.list_item, items);
            itemsView.setAdapter(itemAdapter);
        }
        else {
            itemAdapter.notifyDataSetChanged();
        }
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item lhs, Item rhs) {
                return String.valueOf(rhs.getAddDate()).compareTo(String.valueOf(lhs.getAddDate()));
            }
        });
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