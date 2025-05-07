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
    private boolean isLoginMode = true;

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

        // Toggle login/register mode
        binding.tvToggleMode.setOnClickListener(v -> {
            isLoginMode = !isLoginMode;
            updateUIMode();
        });

        // Action button click listener (Login or Register)
        binding.btnLogin.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (isLoginMode) {
                if (validateLoginInputs(email, password)) {
                    loginUser(email, password);
                }
            } else {
                if (validateRegisterInputs(name, email, password)) {
                    registerUser(name, email, password);
                }
            }
        });
    }

    private void updateUIMode() {
        if (isLoginMode) {
            binding.etName.setVisibility(View.GONE);
            binding.btnLogin.setText("Log In");
            binding.tvToggleMode.setText("Don't have an account? Register");
        } else {
            binding.etName.setVisibility(View.VISIBLE);
            binding.btnLogin.setText("Register");
            binding.tvToggleMode.setText("Already have an account? Log In");
        }
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

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        apiService.login(user).enqueue(new Callback<User>() {
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
                    Toast.makeText(AuthActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
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
        binding.btnLogin.setEnabled(false);
        binding.btnLogin.setText("Registering...");

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        apiService.register(user).enqueue(new Callback<User>() {
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
                    Toast.makeText(AuthActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    binding.btnLogin.setEnabled(true);
                    binding.btnLogin.setText("Register");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AuthActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                binding.btnLogin.setEnabled(true);
                binding.btnLogin.setText("Register");
            }
        });
    }
}