package ru.rsue.Karnaukhova;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemCursorWrapper;
import ru.rsue.Karnaukhova.database.ItemDbSchema.UserTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

public class LogInAppActivity extends AppCompatActivity {

    SQLiteDatabase database;
    Context context;
    EditText logInEditText;
    EditText passwordEditText;
    Button logInButton;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_app);

        context = getApplicationContext();
        database = new ItemBaseHelper(context).getReadableDatabase();

        logInEditText = findViewById(R.id.log_in_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        logInButton = findViewById(R.id.log_in_button);
        signUpButton = findViewById(R.id.sign_up_button);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = database.rawQuery("select count(*) as 'total' from " + UserTable.NAME +
                        " where Login = '" + logInEditText.getText() + "' AND Password = '" + passwordEditText.getText() + "'", null);
                cursor.moveToFirst();
                int usersCount = cursor.getInt(0);
                cursor.close();

                if (usersCount > 0) {
                    Cursor uuidCursor = database.rawQuery("select * from " + UserTable.NAME +
                            " where Login = '" + logInEditText.getText() + "' AND Password = '" + passwordEditText.getText() + "'", null);
                    uuidCursor.moveToFirst();
                    User currentUser = new ItemCursorWrapper(uuidCursor).getUser();
                    uuidCursor.close();
                    CurrentUser.currentUser = currentUser;

                    Toast.makeText(LogInAppActivity.this, "Здравствуйте, " + CurrentUser.currentUser.getNickname(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(LogInAppActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast toast = Toast.makeText(context, "Неправильный логин и/или пароль", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}