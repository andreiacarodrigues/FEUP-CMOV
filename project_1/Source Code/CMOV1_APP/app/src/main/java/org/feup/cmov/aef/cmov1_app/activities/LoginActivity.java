package org.feup.cmov.aef.cmov1_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.feup.cmov.aef.cmov1_app.MyApplication;
import org.feup.cmov.aef.cmov1_app.R;
import org.feup.cmov.aef.cmov1_app.files.LoadSaveData;
import org.feup.cmov.aef.cmov1_app.requests.LoginRequest;
import org.feup.cmov.aef.cmov1_app.tasks.LoginTask;

public class LoginActivity extends AppCompatActivity implements LoginTask.ICallback {

    private EditText usernameEditText;
    private EditText passwordEditText;

    private TextInputLayout usernameInputLayout;
    private TextInputLayout passwordInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getLoadSaveManager().Load();
        LoadSaveData load = MyApplication.getLoadSaveManager().getData();
        System.out.println("user id: " + MyApplication.getLoadSaveManager().getData().UserId);
       if(!MyApplication.getLoadSaveManager().getData().UserId.equals(""))
        {
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        }


        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> onLoginButton());

        usernameEditText = findViewById(R.id.login_username);
        passwordEditText = findViewById(R.id.login_password);

        usernameInputLayout = findViewById(R.id.login_username_layout);
        passwordInputLayout = findViewById(R.id.login_password_layout);

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> onRegisterButton());
    }

    private void onRegisterButton()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void onLoginButton()
    {
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        LoginRequest loginRequest = new LoginRequest(username, password);
        new LoginTask(this).execute(loginRequest);
    }

    @Override
    public void onPostLoginTask(LoginTask.LoginTaskResult result)
    {
        findViewById(R.id.progress_bar).setVisibility(View.GONE);

        switch(result)
        {
            case Success:
                Toast.makeText(getApplicationContext(), "Login successful.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case BadCredentials:
                usernameInputLayout.setError("");
                passwordInputLayout.setError("");
                Toast.makeText(getApplicationContext(), "Incorrect username or password.", Toast.LENGTH_LONG).show();
                break;
            case UnknownError:
                Toast.makeText(getApplicationContext(), "Unknown error, please try again later.", Toast.LENGTH_LONG).show();
                break;
        }

    }
}
