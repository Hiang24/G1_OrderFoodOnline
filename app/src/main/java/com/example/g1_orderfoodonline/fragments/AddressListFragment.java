package com.example.g1_orderfoodonline.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.adapters.AddressAdapter;
import com.example.g1_orderfoodonline.models.DeliveryAddress;
import com.example.g1_orderfoodonline.utils.AddressDatabaseHelper;
import com.example.g1_orderfoodonline.utils.LogUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddressListFragment extends Fragment implements AddressAdapter.AddressListener {

    private static final String TAG = "AddressListFragment";
    private static final String ARG_SELECTION_MODE = "selection_mode";

    private RecyclerView recyclerViewAddresses;
    private TextView textViewNoAddresses;
    private Button buttonAddAddress;
    private TextView textViewTitle;

    private AddressAdapter adapter;
    private List<DeliveryAddress> addresses = new ArrayList<>();
    private boolean isSelectionMode;
    private AddressDatabaseHelper dbHelper;

    public static AddressListFragment newInstance(boolean selectionMode) {
        AddressListFragment fragment = new AddressListFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_SELECTION_MODE, selectionMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address_list, container, false);
        dbHelper = new AddressDatabaseHelper();
        if (getArguments() != null) {
            isSelectionMode = getArguments().getBoolean(ARG_SELECTION_MODE, false);
        }
        initViews(view);
        loadAddresses();
        return view;
    }

    private void initViews(View view) {
        try {
            recyclerViewAddresses = view.findViewById(R.id.recyclerViewAddresses);
            textViewNoAddresses = view.findViewById(R.id.textViewNoAddresses);
            buttonAddAddress = view.findViewById(R.id.buttonAddAddress);
            textViewTitle = view.findViewById(R.id.textViewTitle);
            textViewTitle.setText(isSelectionMode ? "Chọn địa chỉ giao hàng" : "Địa chỉ giao hàng");
            buttonAddAddress.setOnClickListener(v -> showAddAddressFragment());
            recyclerViewAddresses.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new AddressAdapter(requireContext(), addresses, this, isSelectionMode);
            recyclerViewAddresses.setAdapter(adapter);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error initializing views", e);
        }
    }

    private void loadAddresses() {
        dbHelper.getAddresses(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addresses.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    DeliveryAddress address = child.getValue(DeliveryAddress.class);
                    if (address != null) addresses.add(address);
                }
                updateUI();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi khi tải địa chỉ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
            if (addresses.isEmpty()) {
                textViewNoAddresses.setVisibility(View.VISIBLE);
                recyclerViewAddresses.setVisibility(View.GONE);
            } else {
                textViewNoAddresses.setVisibility(View.GONE);
                recyclerViewAddresses.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    private void showAddAddressFragment() {
        AddAddressFragment fragment = AddAddressFragment.newInstance(null);
        fragment.setTargetFragment(this, 0);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showEditAddressFragment(String addressId) {
        AddAddressFragment fragment = AddAddressFragment.newInstance(addressId);
        fragment.setTargetFragment(this, 0);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAddressSelected(DeliveryAddress address) {
        if (isSelectionMode && getActivity() != null) {
            getActivity().setResult(getActivity().RESULT_OK);
            getActivity().finish();
        }
    }

    @Override
    public void onEditAddress(DeliveryAddress address) {
        showEditAddressFragment(address.getId());
    }

    @Override
    public void onDeleteAddress(DeliveryAddress address) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa địa chỉ")
                .setMessage("Bạn có chắc chắn muốn xóa địa chỉ này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteAddress(address.getId(), (error, ref) -> loadAddresses());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onSetDefaultAddress(DeliveryAddress address) {
        dbHelper.setDefaultAddress(address.getId(), (error, ref) -> loadAddresses());
    }
} 