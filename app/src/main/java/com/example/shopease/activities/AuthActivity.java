package com.example.shopease.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.shopease.databinding.ActivityAuthBinding;
import com.example.shopease.models.User;
import com.example.shopease.network.ApiClient;
import com.example.shopease.network.ApiService;
import com.example.shopease.utils.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;
    private ApiService apiService;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize API and SharedPreferences
        apiService = ApiClient.getClient().create(ApiService.class);
        sharedPrefManager = SharedPrefManager.getInstance(this);

        // Back button click listener
        binding.ivBack.setOnClickListener(v -> finish());

        // Login button click listener
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (validateLoginInputs(email, password)) {
                loginUser(email, password);
            }
        });

        // Register button click listener
        binding.btnRegister.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (validateRegisterInputs(name, email, password)) {
                registerUser(name, email, password);
            }
        });
    }

    private boolean validateLoginInputs(String email, String password) {
        if (email.isEmpty()) {
            binding.etEmail.setError("Email is required");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Enter a valid email");
            return false;
        }

        if (password.isEmpty()) {
            binding.etPassword.setError("Password is required");
            return false;
        }

        return true;
    }

    private boolean validateRegisterInputs(String name, String email, String password) {
        if (name.isEmpty()) {
            binding.etName.setError("Name is required");
            return false;
        }

        if (email.isEmpty()) {
            binding.etEmail.setError("Email is required");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Enter a valid email");
            return false;
        }

        if (password.isEmpty()) {
            binding.etPassword.setError("Password is required");
            return false;
        } else if (password.length() < 6) {
            binding.etPassword.setError("Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    private void loginUser(String email, String password) {
        binding.btnLogin.setEnabled(false);
        binding.btnLogin.setText("Logging in...");

        ApiService.LoginRequest request = new ApiService.LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User loggedInUser = response.body();
                    sharedPrefManager.userLogin(
                            loggedInUser.getId(),
                            loggedInUser.getName(),
                            loggedInUser.getEmail(),
                            loggedInUser.getPhone(),
                            loggedInUser.getAddress(),
                            loggedInUser.getCity(),
                            loggedInUser.getZipCode()
                    );
                    Toast.makeText(AuthActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg = response.message().isEmpty() ? "Login failed" : response.message();
                    Toast.makeText(AuthActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    binding.btnLogin.setEnabled(true);
                    binding.btnLogin.setText("Log In");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AuthActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                binding.btnLogin.setEnabled(true);
                binding.btnLogin.setText("Log In");
            }
        });
    }

    private void registerUser(String name, String email, String password) {
        binding.btnRegister.setEnabled(false);
        binding.btnRegister.setText("Registering...");

        ApiService.RegisterRequest request = new ApiService.RegisterRequest(name, email, password);

        apiService.register(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User registeredUser = response.body();
                    sharedPrefManager.userLogin(
                            registeredUser.getId(),
                            registeredUser.getName(),
                            registeredUser.getEmail(),
                            registeredUser.getPhone(),
                            registeredUser.getAddress(),
                            registeredUser.getCity(),
                            registeredUser.getZipCode()
                    );
                    Toast.makeText(AuthActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg = response.message().isEmpty() ? "Registration failed" : response.message();
                    Toast.makeText(AuthActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    binding.btnRegister.setEnabled(true);
                    binding.btnRegister.setText("Register");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AuthActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                binding.btnRegister.setEnabled(true);
                binding.btnRegister.setText("Register");
            }
        });
    }
}