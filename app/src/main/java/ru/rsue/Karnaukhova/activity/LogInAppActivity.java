package ru.rsue.Karnaukhova.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import ru.rsue.Karnaukhova.CurrentUser;
import ru.rsue.Karnaukhova.MainActivity;
import ru.rsue.Karnaukhova.R;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.dto.UserDTO;
import ru.rsue.Karnaukhova.dto.UserLogInDTO;
import ru.rsue.Karnaukhova.dto.mapper.UserDTOMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                try {
                    String s = Executors.newSingleThreadExecutor().submit(() -> getLoggedInUser()).get();

                    if (!s.isEmpty()) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        UserDTO userDTO = objectMapper.readValue(s, UserDTO.class);

                        CurrentUser.currentUser = UserDTOMapper.INSTANCE.mapToEntity(userDTO, userDTO.getUuid());

                        Toast.makeText(LogInAppActivity.this, "Здравствуйте, " + CurrentUser.currentUser.getNickname(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(LogInAppActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(context, "Неправильный логин и/или пароль", Toast.LENGTH_LONG).show();
                    }
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (JsonMappingException e) {
                    throw new RuntimeException(e);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInAppActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @NotNull
    private String getLoggedInUser() {
        HttpURLConnection httpURLConnection;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL("http://10.0.2.2:8080/MyShoppingListBackend/login");

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setRequestMethod("POST");

            try (DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream())) {
                ObjectMapper objectMapper = new ObjectMapper();
                UserLogInDTO userLogInDTO = new UserLogInDTO();

                userLogInDTO.setLogin(logInEditText.getText().toString());
                userLogInDTO.setPassword(passwordEditText.getText().toString());

                dataOutputStream.writeBytes(objectMapper.writeValueAsString(userLogInDTO));
                dataOutputStream.flush();
            }

            httpURLConnection.connect();

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
            }

            httpURLConnection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stringBuilder.toString();
    }
}