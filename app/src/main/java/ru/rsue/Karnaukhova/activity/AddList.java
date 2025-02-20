package ru.rsue.Karnaukhova.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import ru.rsue.Karnaukhova.CurrentUser;
import ru.rsue.Karnaukhova.R;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.entity.ItemList;
import ru.rsue.Karnaukhova.repository.ListRepository;

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

                ListRepository.get(AddList.this).addList(mList);

                Toast toast = new Toast(AddList.this);
                toast.setText("Добавлено");
                toast.show();

                finish();
            }
        });
    }
}