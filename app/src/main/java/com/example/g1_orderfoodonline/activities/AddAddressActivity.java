package com.example.g1_orderfoodonline.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.models.DeliveryAddress;
import com.example.g1_orderfoodonline.utils.AddressManager;
import com.example.g1_orderfoodonline.utils.LogUtils;

public class AddAddressActivity extends AppCompatActivity {

    private static final String TAG = "AddAddressActivity";
    
    private EditText editTextName, editTextPhone, editTextAddress, editTextDistrict, editTextCity;
    private CheckBox checkBoxDefault;
    private Button buttonSave;
    private Toolbar toolbar;
    private ImageView backButton;
    private TextView textViewTitle;
    
    private DeliveryAddress existingAddress;
    private int addressId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        
        // Kiểm tra xem có đang ở chế độ sửa địa chỉ không
        if (getIntent().hasExtra("address_id")) {
            addressId = getIntent().getIntExtra("address_id", -1);
            isEditMode = addressId != -1;
        }
        
        initViews();
        setupToolbar();
        
        if (isEditMode) {
            loadAddressData();
        }
    }
    
    private void initViews() {
        try {
            editTextName = findViewById(R.id.editTextName);
            editTextPhone = findViewById(R.id.editTextPhone);
            editTextAddress = findViewById(R.id.editTextAddress);
            editTextDistrict = findViewById(R.id.editTextDistrict);
            editTextCity = findViewById(R.id.editTextCity);
            checkBoxDefault = findViewById(R.id.checkBoxDefault);
            buttonSave = findViewById(R.id.buttonSave);
            toolbar = findViewById(R.id.toolbar);
            backButton = findViewById(R.id.backButton);
            textViewTitle = findViewById(R.id.textViewTitle);
            
            buttonSave.setOnClickListener(v -> {
                saveAddress();
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error initializing views", e);
        }
    }
    
    private void setupToolbar() {
        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            
            if (isEditMode) {
                textViewTitle.setText("Sửa địa chỉ");
            } else {
                textViewTitle.setText("Thêm địa chỉ mới");
            }
            
            backButton.setOnClickListener(v -> {
                finish();
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up toolbar", e);
        }
    }
    
    private void loadAddressData() {
        try {
            existingAddress = AddressManager.getInstance().getAddressById(addressId);
            if (existingAddress != null) {
                editTextName.setText(existingAddress.getName());
                editTextPhone.setText(existingAddress.getPhone());
                editTextAddress.setText(existingAddress.getAddress());
                editTextDistrict.setText(existingAddress.getDistrict());
                editTextCity.setText(existingAddress.getCity());
                checkBoxDefault.setChecked(existingAddress.isDefault());
            } else {
                Toast.makeText(this, "Không tìm thấy địa chỉ", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error loading address data", e);
            Toast.makeText(this, "Lỗi khi tải thông tin địa chỉ", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void saveAddress() {
        try {
            String name = editTextName.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();
            String address = editTextAddress.getText().toString().trim();
            String district = editTextDistrict.getText().toString().trim();
            String city = editTextCity.getText().toString().trim();
            boolean isDefault = checkBoxDefault.isChecked();
            
            if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || district.isEmpty() || city.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (isEditMode && existingAddress != null) {
                // Cập nhật địa chỉ hiện có
                existingAddress.setName(name);
                existingAddress.setPhone(phone);
                existingAddress.setAddress(address);
                existingAddress.setDistrict(district);
                existingAddress.setCity(city);
                existingAddress.setDefault(isDefault);
                
                AddressManager.getInstance().updateAddress(existingAddress);
                Toast.makeText(this, "Đã cập nhật địa chỉ", Toast.LENGTH_SHORT).show();
            } else {
                // Thêm địa chỉ mới
                DeliveryAddress newAddress = new DeliveryAddress(
                        0, // ID sẽ được tạo trong AddressManager
                        name,
                        phone,
                        address,
                        district,
                        city,
                        isDefault
                );
                
                AddressManager.getInstance().addAddress(newAddress);
                Toast.makeText(this, "Đã thêm địa chỉ mới", Toast.LENGTH_SHORT).show();
            }
            
            setResult(RESULT_OK);
            finish();
        } catch (Exception e) {
            LogUtils.error(TAG, "Error saving address", e);
            Toast.makeText(this, "Lỗi khi lưu địa chỉ", Toast.LENGTH_SHORT).show();
        }
    }
}
