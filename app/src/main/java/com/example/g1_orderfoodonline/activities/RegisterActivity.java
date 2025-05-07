package com.example.g1_orderfoodonline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.g1_orderfoodonline.R;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        initViews();
        setupClickListeners();
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);
    }

    private void setupClickListeners() {
        buttonRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                String input = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String email;
                if (isValidEmail(input)) {
                    email = input;
                } else {
                    email = input + "@orderfood.com";
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        textViewLogin.setOnClickListener(v -> {
            finish();
        });
    }

    private boolean validateInputs() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Vui lòng nhập email");
            return false;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Vui lòng nhập mật khẩu");
            return false;
        }

        if (confirmPassword.isEmpty()) {
            editTextConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Mật khẩu không khớp");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}

