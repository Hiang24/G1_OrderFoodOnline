package com.example.g1_orderfoodonline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.adapters.AddressAdapter;
import com.example.g1_orderfoodonline.fragments.AddAddressFragment;
import com.example.g1_orderfoodonline.models.DeliveryAddress;
import com.example.g1_orderfoodonline.utils.AddressDatabaseHelper;
import com.example.g1_orderfoodonline.utils.LogUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddressListActivity extends AppCompatActivity implements AddressAdapter.AddressListener {

    private static final String TAG = "AddressListActivity";
    private static final String ARG_SELECTION_MODE = "selection_mode";

    private RecyclerView recyclerViewAddresses;
    private TextView textViewNoAddresses;
    private Button buttonAddAddress;
    private Toolbar toolbar;

    private AddressAdapter adapter;
    private List<DeliveryAddress> addresses;
    private boolean isSelectionMode;
    private AddressDatabaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        isSelectionMode = getIntent().getBooleanExtra(ARG_SELECTION_MODE, false);
        dbHelper = new AddressDatabaseHelper();

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

            buttonAddAddress.setOnClickListener(v -> {
                showAddAddressFragment();
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error initializing views", e);
        }
    }

    private void setupToolbar() {
        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(isSelectionMode ? "Chọn địa chỉ giao hàng" : "Địa chỉ giao hàng");
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up toolbar", e);
        }
    }

    private void loadAddresses() {
        dbHelper.getAddresses(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addresses = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    DeliveryAddress address = child.getValue(DeliveryAddress.class);
                    if (address != null) addresses.add(address);
                }
                if (addresses.isEmpty()) {
                    textViewNoAddresses.setVisibility(View.VISIBLE);
                    recyclerViewAddresses.setVisibility(View.GONE);
                } else {
                    textViewNoAddresses.setVisibility(View.GONE);
                    recyclerViewAddresses.setVisibility(View.VISIBLE);
                    adapter = new AddressAdapter(AddressListActivity.this, addresses, AddressListActivity.this, isSelectionMode);
                    recyclerViewAddresses.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
                    recyclerViewAddresses.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                textViewNoAddresses.setVisibility(View.VISIBLE);
                recyclerViewAddresses.setVisibility(View.GONE);
            }
        });
    }

    private void showAddAddressFragment() {
        AddAddressFragment fragment = AddAddressFragment.newInstance(null);
        fragment.setTargetFragment(null, 0);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
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
        AddAddressFragment fragment = AddAddressFragment.newInstance(address.getId());
        fragment.setTargetFragment(null, 0);
        
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDeleteAddress(DeliveryAddress address) {
        dbHelper.deleteAddress(address.getId(), (error, ref) -> loadAddresses());
    }

    @Override
    public void onSetDefaultAddress(DeliveryAddress address) {
        dbHelper.setDefaultAddress(address.getId(), (error, ref) -> loadAddresses());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 