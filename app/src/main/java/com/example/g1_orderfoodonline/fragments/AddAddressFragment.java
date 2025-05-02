package com.example.g1_orderfoodonline.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.models.DeliveryAddress;
import com.example.g1_orderfoodonline.utils.AddressManager;
import com.example.g1_orderfoodonline.utils.LogUtils;

public class AddAddressFragment extends Fragment {

    private static final String TAG = "AddAddressFragment";
    private static final String ARG_ADDRESS_ID = "address_id";
    
    private EditText editTextName, editTextPhone, editTextAddress, editTextDistrict, editTextCity;
    private CheckBox checkBoxDefault;
    private Button buttonSave, buttonCancel;
    private TextView textViewTitle;
    
    private DeliveryAddress existingAddress;
    private int addressId = -1;
    private boolean isEditMode = false;

    public static AddAddressFragment newInstance(int addressId) {
        AddAddressFragment fragment = new AddAddressFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ADDRESS_ID, addressId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);

        if (getArguments() != null) {
            addressId = getArguments().getInt(ARG_ADDRESS_ID, -1);
            isEditMode = addressId != -1;
        }

        initViews(view);
        
        if (isEditMode) {
            loadAddressData();
        }

        return view;
    }

    private void initViews(View view) {
        try {
            editTextName = view.findViewById(R.id.editTextName);
            editTextPhone = view.findViewById(R.id.editTextPhone);
            editTextAddress = view.findViewById(R.id.editTextAddress);
            editTextDistrict = view.findViewById(R.id.editTextDistrict);
            editTextCity = view.findViewById(R.id.editTextCity);
            checkBoxDefault = view.findViewById(R.id.checkBoxDefault);
            buttonSave = view.findViewById(R.id.buttonSave);
            buttonCancel = view.findViewById(R.id.buttonCancel);
            textViewTitle = view.findViewById(R.id.textViewTitle);

            textViewTitle.setText(isEditMode ? "Sửa địa chỉ" : "Thêm địa chỉ mới");
            
            buttonSave.setOnClickListener(v -> {
                saveAddress();
            });

            buttonCancel.setOnClickListener(v -> {
                // Quay về fragment trước đó
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().popBackStack();
                }
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error initializing views", e);
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
                Toast.makeText(getContext(), "Không tìm thấy địa chỉ", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error loading address data", e);
            Toast.makeText(getContext(), "Lỗi khi tải thông tin địa chỉ", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
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
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Đã cập nhật địa chỉ", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Đã thêm địa chỉ mới", Toast.LENGTH_SHORT).show();
            }
            
            // Quay về fragment trước đó
            getParentFragmentManager().popBackStack();
        } catch (Exception e) {
            LogUtils.error(TAG, "Error saving address", e);
            Toast.makeText(getContext(), "Lỗi khi lưu địa chỉ", Toast.LENGTH_SHORT).show();
        }
    }
} 