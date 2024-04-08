package ru.rsue.Karnaukhova;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class

AddList extends AppCompatActivity {
    ItemList mList;
    Context mContext;
    SQLiteDatabase mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_list);

        mContext = getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        mList = new ItemList(UUID.randomUUID());

        EditText mListNameEditText = findViewById(R.id.items_list_name);
        Button mAddList = findViewById(R.id.add_new_list);

        mAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.setListName((mListNameEditText.getText().toString()));

                mList.setOwnerUserId(CurrentUser.currentUser.getUuid());

                ItemStorage.get(AddList.this).addList(mList);

                Toast toast = new Toast(AddList.this);
                toast.setText("Добавлено");
                toast.show();

                finish();
            }
        });
    }
}