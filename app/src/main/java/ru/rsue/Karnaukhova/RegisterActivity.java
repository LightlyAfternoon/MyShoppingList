package ru.rsue.Karnaukhova;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;
import ru.rsue.Karnaukhova.entity.User;
import ru.rsue.Karnaukhova.repository.ItemStorage;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    TextView txtErrorRegister;
    EditText edTxtLogin;
    EditText edTxtPassword;
    EditText edTxtNickName;
    Button btnRegister;

    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtErrorRegister = findViewById(R.id.error_register_text_view);
        edTxtLogin = findViewById(R.id.log_in_register_edit_text);
        edTxtPassword = findViewById(R.id.password_register_edit_text);
        edTxtNickName = findViewById(R.id.nickname_register_edit_text);
        btnRegister = findViewById(R.id.register_button);

        mDatabase = new ItemBaseHelper(RegisterActivity.this).getWritableDatabase();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = mDatabase.rawQuery("select * from User where " + ItemDbSchema.UserTable.Cols.LOGIN + " = '" + edTxtLogin.getText().toString() + "'", null);

                if (edTxtLogin.getText().toString().isBlank() || edTxtPassword.getText().toString().isBlank()) {
                    txtErrorRegister.setText("Необходимо ввести логин и пароль");

                    cursor.close();
                }
                else if (cursor.getCount() > 0) {
                    txtErrorRegister.setText("Пользователь с данным логином уже существует");

                    cursor.close();
                }
                else {
                    User newUser = new User(UUID.randomUUID());

                    newUser.setLogin(edTxtLogin.getText().toString());
                    newUser.setPassword(edTxtPassword.getText().toString());
                    newUser.setNickname(edTxtNickName.getText().toString());

                    ItemStorage.get(RegisterActivity.this).addUser(newUser);

                    Toast toast = new Toast(RegisterActivity.this);
                    toast.setText("Вы успешно зарегистрированны!");
                    toast.show();

                    cursor.close();

                    finish();
                }
            }
        });
    }
}