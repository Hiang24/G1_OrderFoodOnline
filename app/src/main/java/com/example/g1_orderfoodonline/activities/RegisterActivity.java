package com.example.g1_orderfoodonline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.g1_orderfoodonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initViews();
        setupClickListeners();
    }

    private void initViews() {
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);
    }

    private void setupClickListeners() {
        buttonRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                String fullName = editTextFullName.getText().toString().trim();
                String input = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String email;
                if (isValidEmail(input)) {
                    email = input;
                } else {
                    email = input + "@orderfood.com";
                }

                // Vô hiệu hóa nút đăng ký trong khi xử lý
                buttonRegister.setEnabled(false);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Lưu thông tin người dùng vào Firestore
                                String userId = mAuth.getCurrentUser().getUid();
                                Map<String, Object> user = new HashMap<>();
                                user.put("fullName", fullName);
                                user.put("email", email);
                                user.put("phone", input);

                                // Chuyển về màn hình đăng nhập ngay lập tức
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                                // Lưu thông tin vào Firestore sau khi đã chuyển màn hình
                                db.collection("users").document(userId)
                                        .set(user)
                                        .addOnFailureListener(e -> {
                                            Log.e("RegisterActivity", "Error saving user data: " + e.getMessage());
                                        });
                            } else {
                                runOnUiThread(() -> {
                                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    buttonRegister.setEnabled(true);
                                });
                            }
                        });
            }
        });

        textViewLogin.setOnClickListener(v -> {
            finish();
        });
    }

    private boolean validateInputs() {
        String fullName = editTextFullName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (fullName.isEmpty()) {
            editTextFullName.setError("Vui lòng nhập họ và tên");
            return false;
        }

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

