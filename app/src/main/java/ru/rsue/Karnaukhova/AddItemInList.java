package ru.rsue.Karnaukhova;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AddItemInList extends AppCompatActivity {
    private Item mItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_in_list);

        mItem = new Item(UUID.randomUUID());

        String[] mWeightUnits = {"шт.", "г.", "мл.", "кг.", "л."};
        EditText mItemName = findViewById(R.id.item_name);
        EditText mItemCount = findViewById(R.id.item_count);
        Spinner mWeightUnitSpinner = findViewById(R.id.item_weight_unit_spinner);
        EditText mItemPriceForOne = findViewById(R.id.item_price_for_one);
        EditText mItemAddDate = findViewById(R.id.item_add_date);
        Button mAddItem = findViewById(R.id.add_new_item_in_list);

        mItemCount.setMaxWidth(100);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        mItemAddDate.setText(sdf.format(new Date()));

        try {
            Date date = sdf.parse(mItemAddDate.getText().toString());
            mItem.setAddDate(date.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        mItemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mItem.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mItemCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    mItem.setCount(Integer.parseInt(s.toString()));
                }
                else {
                    mItem.setCount(1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mItemPriceForOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    mItem.setPriceForOne(Double.parseDouble(s.toString()));
                }
                else {
                    mItem.setCount(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mItemAddDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
/////////Repair////////
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Date date = sdf.parse(s.toString());
                    mItem.setAddDate(date.getTime());
                }
                catch (Exception e){}
            }
////////////////////////

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mWeightUnits);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mWeightUnitSpinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                TextView textViewPriceForSmth = findViewById(R.id.tv_price_for_smth);
                mItem.setWeightUnit(item);

                if (item == "шт.") {
                    textViewPriceForSmth.setText("Цена за 1 шт.");
                }
                else if (item == "г.") {
                    textViewPriceForSmth.setText("Цена за 100 г.");
                }
                else if (item == "мл.") {
                    textViewPriceForSmth.setText("Цена за 100 мл.");
                }
                else if (item == "кг.") {
                    textViewPriceForSmth.setText("Цена за 1 кг.");
                }
                else if (item == "л.") {
                    textViewPriceForSmth.setText("Цена за 1 л.");
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
                ItemStorage.get(AddItemInList.this).addItem(mItem);

                Intent intent = new Intent(AddItemInList.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}