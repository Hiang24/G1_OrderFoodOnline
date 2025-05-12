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
import com.example.g1_orderfoodonline.utils.AddressDatabaseHelper;
import com.example.g1_orderfoodonline.utils.LogUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AddAddressFragment extends Fragment {

    private static final String TAG = "AddAddressFragment";
    private EditText editTextName, editTextPhone, editTextAddress, editTextDistrict, editTextCity;
    private CheckBox checkBoxDefault;
    private Button buttonSave, buttonCancel;
    private TextView textViewTitle;
    private boolean isEditMode = false;
    private DeliveryAddress existingAddress;
    private String addressId = null;
    private AddressDatabaseHelper dbHelper;

    public static AddAddressFragment newInstance(String addressId) {
        AddAddressFragment fragment = new AddAddressFragment();
        Bundle args = new Bundle();
        args.putString("address_id", addressId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);
        dbHelper = new AddressDatabaseHelper();
        if (getArguments() != null) {
            addressId = getArguments().getString("address_id", null);
            isEditMode = addressId != null;
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
            buttonSave.setOnClickListener(v -> saveAddress());
            buttonCancel.setOnClickListener(v -> {
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().popBackStack();
                }
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error initializing views", e);
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
                existingAddress.setName(name);
                existingAddress.setPhone(phone);
                existingAddress.setAddress(address);
                existingAddress.setDistrict(district);
                existingAddress.setCity(city);
                existingAddress.setDefault(isDefault);
                dbHelper.updateAddress(existingAddress, (error, ref) -> {
                    if (error == null) {
                        Toast.makeText(getContext(), "Đã cập nhật địa chỉ", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getContext(), "Lỗi khi cập nhật địa chỉ", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                DeliveryAddress newAddress = new DeliveryAddress(
                        null, name, phone, address, district, city, isDefault, System.currentTimeMillis()
                );
                dbHelper.addAddress(newAddress, (error, ref) -> {
                    if (error == null) {
                        Toast.makeText(getContext(), "Đã thêm địa chỉ mới", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getContext(), "Lỗi khi thêm địa chỉ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error saving address", e);
            Toast.makeText(getContext(), "Lỗi khi lưu địa chỉ", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAddressData() {
        if (addressId == null) return;
        dbHelper.getAddressById(addressId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                existingAddress = snapshot.getValue(DeliveryAddress.class);
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi khi tải thông tin địa chỉ", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }
        });
    }
} 