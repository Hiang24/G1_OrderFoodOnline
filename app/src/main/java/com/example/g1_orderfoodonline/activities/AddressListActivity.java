package com.example.g1_orderfoodonline.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.adapters.AddressAdapter;
import com.example.g1_orderfoodonline.models.DeliveryAddress;
import com.example.g1_orderfoodonline.utils.AddressManager;
import com.example.g1_orderfoodonline.utils.LogUtils;

import java.util.List;

public class AddressListActivity extends AppCompatActivity implements AddressAdapter.AddressListener {

    private static final String TAG = "AddressListActivity";
    private static final int REQUEST_ADD_ADDRESS = 1001;
    private static final int REQUEST_EDIT_ADDRESS = 1002;

    private RecyclerView recyclerViewAddresses;
    private TextView textViewNoAddresses;
    private Button buttonAddAddress;
    private Toolbar toolbar;
    private ImageView backButton;

    private AddressAdapter adapter;
    private List<DeliveryAddress> addresses;
    private boolean isSelectionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        // Kiểm tra xem có đang ở chế độ chọn địa chỉ không
        isSelectionMode = getIntent().getBooleanExtra("selection_mode", false);

        initViews();
        setupToolbar();
        loadAddresses();
    }

    private void initViews() {
        try {
            recyclerViewAddresses = findViewById(R.id.recyclerViewAddresses);
            textViewNoAddresses = findViewById(R.id.textViewNoAddresses);
            buttonAddAddress = findViewById(R.id.buttonAddAddress);
            toolbar = findViewById(R.id.toolbar);
            backButton = findViewById(R.id.backButton);

            buttonAddAddress.setOnClickListener(v -> {
                Intent intent = new Intent(AddressListActivity.this, AddAddressActivity.class);
                startActivityForResult(intent, REQUEST_ADD_ADDRESS);
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

            backButton.setOnClickListener(v -> {
                onBackPressed();
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up toolbar", e);
        }
    }

    private void loadAddresses() {
        try {
            addresses = AddressManager.getInstance().getAddresses();

            if (addresses.isEmpty()) {
                textViewNoAddresses.setVisibility(View.VISIBLE);
                recyclerViewAddresses.setVisibility(View.GONE);
            } else {
                textViewNoAddresses.setVisibility(View.GONE);
                recyclerViewAddresses.setVisibility(View.VISIBLE);

                adapter = new AddressAdapter(this, addresses, this, isSelectionMode);
                recyclerViewAddresses.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewAddresses.setAdapter(adapter);
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error loading addresses", e);
        }
    }

    @Override
    public void onAddressSelected(DeliveryAddress address) {
        if (isSelectionMode) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_address_id", address.getId());
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    @Override
    public void onEditAddress(DeliveryAddress address) {
        Intent intent = new Intent(this, AddAddressActivity.class);
        intent.putExtra("address_id", address.getId());
        startActivityForResult(intent, REQUEST_EDIT_ADDRESS);
    }

    @Override
    public void onDeleteAddress(DeliveryAddress address) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa địa chỉ")
                .setMessage("Bạn có chắc chắn muốn xóa địa chỉ này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    AddressManager.getInstance().deleteAddress(address.getId());
                    loadAddresses();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onSetDefaultAddress(DeliveryAddress address) {
        AddressManager.getInstance().setDefaultAddress(address.getId());
        loadAddresses();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ADD_ADDRESS || requestCode == REQUEST_EDIT_ADDRESS) {
                loadAddresses();
            }
        }
    }
}
