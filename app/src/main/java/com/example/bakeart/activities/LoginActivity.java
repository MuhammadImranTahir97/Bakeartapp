package com.example.bakeart;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bakeart.activities.RegisterActivity;
import com.example.bakeart.api.RecipeApi;
import com.example.bakeart.models.User;
import com.example.bakeart.utils.RetrofitClient;
import com.example.bakeart.utils.UserSession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView tvRegisterLink = findViewById(R.id.tvRegisterLink);
        tvRegisterLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        loginBtn.setOnClickListener(v -> {
            String userEmail = email.getText().toString().trim();
            String userPass = password.getText().toString().trim();

            if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPass)) {
                Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(userEmail, userPass);
        });

        registerBtn.setOnClickListener(v -> {
            String userEmail = email.getText().toString().trim();
            String userPass = password.getText().toString().trim();

            if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPass)) {
                Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser(userEmail, userPass);
        });
    }

    private void loginUser(String email, String password) {
        User user = new User(email, password);
        RecipeApi api = RetrofitClient.getInstance().create(RecipeApi.class);
        api.login(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserSession session = new UserSession(LoginActivity.this);
                    session.saveUser(
                            response.body().get_id(),
                            response.body().getUsername(),
                            response.body().getRole()
                    );

                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed: Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(String email, String password) {
        User user = new User(email, password);
        RecipeApi api = RetrofitClient.getInstance().create(RecipeApi.class);
        api.register(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserSession session = new UserSession(LoginActivity.this);
                    session.saveUser(
                            response.body().get_id(),
                            response.body().getUsername(),
                            response.body().getRole()
                    );

                    Toast.makeText(LoginActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

