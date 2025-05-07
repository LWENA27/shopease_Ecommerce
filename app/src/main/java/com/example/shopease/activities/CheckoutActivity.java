package com.example.shopease.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopease.R;
import com.example.shopease.database.AppDatabase;
import com.example.shopease.models.Order;
import com.example.shopease.network.ApiClient;
import com.example.shopease.network.ApiService;
import com.example.shopease.utils.SharedPrefManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private ImageView ivBack;
    private EditText etFullName, etEmail, etPhone, etAddress, etCity, etZipCode;
    private RadioGroup rgPaymentMethod;
    private RadioButton rbCreditCard, rbPaypal, rbCod;
    private TextView tvSubtotal, tvShipping, tvTotal;
    private Button btnPlaceOrder;

    private AppDatabase database;
    private ApiService apiService;
    private ExecutorService executorService;
    private SharedPrefManager sharedPrefManager;

    private double subtotal, shipping, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize views
        ivBack = findViewById(R.id.iv_back);
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        etCity = findViewById(R.id.et_city);
        etZipCode = findViewById(R.id.et_zip_code);
        rgPaymentMethod = findViewById(R.id.rg_payment_method);
        rbCreditCard = findViewById(R.id.rb_credit_card);
        rbPaypal = findViewById(R.id.rb_paypal);
        rbCod = findViewById(R.id.rb_cod);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvShipping = findViewById(R.id.tv_shipping);
        tvTotal = findViewById(R.id.tv_total);
        btnPlaceOrder = findViewById(R.id.btn_place_order);

        // Initialize database, API, and shared preferences
        database = AppDatabase.getInstance(this);
        apiService = ApiClient.getClient().create(ApiService.class);
        executorService = Executors.newSingleThreadExecutor();
        sharedPrefManager = SharedPrefManager.getInstance(this);

        // Get intent data
        subtotal = getIntent().getDoubleExtra("SUBTOTAL", 0);
        shipping = getIntent().getDoubleExtra("SHIPPING", 0);
        total = getIntent().getDoubleExtra("TOTAL", 0);

        // Setup click listeners
        setupClickListeners();

        // Display order summary
        displayOrderSummary();

        // Load user data
        loadUserData();
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());

        btnPlaceOrder.setOnClickListener(v -> {
            if (validateInputs()) {
                placeOrder();
            }
        });
    }

    private void displayOrderSummary() {
        tvSubtotal.setText(String.format("$%.2f", subtotal));
        tvShipping.setText(String.format("$%.2f", shipping));
        tvTotal.setText(String.format("$%.2f", total));
    }

    private void loadUserData() {
        if (sharedPrefManager.isLoggedIn()) {
            etFullName.setText(sharedPrefManager.getUserName());
            etEmail.setText(sharedPrefManager.getUserEmail());
            etPhone.setText(sharedPrefManager.getUserPhone());
            etAddress.setText(sharedPrefManager.getUserAddress());
            etCity.setText(sharedPrefManager.getUserCity());
            etZipCode.setText(sharedPrefManager.getUserZipCode());
        }
    }

    private boolean validateInputs() {
        // Validate name
        if (etFullName.getText().toString().trim().isEmpty()) {
            etFullName.setError("Name is required");
            return false;
        }

        // Validate email
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            return false;
        }

        // Validate phone
        if (etPhone.getText().toString().trim().isEmpty()) {
            etPhone.setError("Phone is required");
            return false;
        }

        // Validate address
        if (etAddress.getText().toString().trim().isEmpty()) {
            etAddress.setError("Address is required");
            return false;
        }

        // Validate city
        if (etCity.getText().toString().trim().isEmpty()) {
            etCity.setError("City is required");
            return false;
        }

        // Validate zip code
        if (etZipCode.getText().toString().trim().isEmpty()) {
            etZipCode.setError("ZIP code is required");
            return false;
        }

        // Validate payment method
        if (rgPaymentMethod.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void placeOrder() {
        btnPlaceOrder.setEnabled(false);
        btnPlaceOrder.setText("Processing...");

        // Get payment method
        String paymentMethod;
        int checkedRadioButtonId = rgPaymentMethod.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.rb_credit_card) {
            paymentMethod = "Credit Card";
        } else if (checkedRadioButtonId == R.id.rb_paypal) {
            paymentMethod = "PayPal";
        } else {
            paymentMethod = "Cash on Delivery";
        }

        // Create order object
        Order order = new Order();
        order.setUserId(sharedPrefManager.getUserId());
        order.setFullName(etFullName.getText().toString().trim());
        order.setEmail(etEmail.getText().toString().trim());
        order.setPhone(etPhone.getText().toString().trim());
        order.setAddress(etAddress.getText().toString().trim());
        order.setCity(etCity.getText().toString().trim());
        order.setZipCode(etZipCode.getText().toString().trim());
        order.setPaymentMethod(paymentMethod);
        order.setSubtotal(subtotal);
        order.setShipping(shipping);
        order.setTotal(total);

        // Send order to server
        apiService.placeOrder(order).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Clear cart
                    executorService.execute(() -> {
                        database.cartDao().clearCart();

                        runOnUiThread(() -> {
                            // Navigate to order success screen
                            Intent intent = new Intent(CheckoutActivity.this, OrderSuccessActivity.class);
                            intent.putExtra("ORDER_ID", response.body().getId());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        });
                    });
                } else {
                    Toast.makeText(CheckoutActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                    btnPlaceOrder.setEnabled(true);
                    btnPlaceOrder.setText("Place Order");
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(CheckoutActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                btnPlaceOrder.setEnabled(true);
                btnPlaceOrder.setText("Place Order");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}