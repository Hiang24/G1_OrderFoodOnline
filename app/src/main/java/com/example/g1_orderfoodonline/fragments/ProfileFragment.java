package com.example.g1_orderfoodonline.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.activities.LoginActivity;
import com.example.g1_orderfoodonline.activities.OrderHistoryActivity;
import com.example.g1_orderfoodonline.utils.LogUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private TextView textViewName, textViewEmail;
    private Button buttonEditProfile, buttonLogout;
    private LinearLayout layoutMyOrders, layoutAddress, layoutPayment, layoutNotification;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initViews(view);
        setupClickListeners();
        loadUserData();

        return view;
    }

    private void initViews(View view) {
        try {
            textViewName = view.findViewById(R.id.textViewName);
            textViewEmail = view.findViewById(R.id.textViewEmail);
            buttonEditProfile = view.findViewById(R.id.buttonEditProfile);
            buttonLogout = view.findViewById(R.id.buttonLogout);
            layoutMyOrders = view.findViewById(R.id.layoutMyOrders);
            layoutAddress = view.findViewById(R.id.layoutAddress);
            layoutPayment = view.findViewById(R.id.layoutPayment);
            layoutNotification = view.findViewById(R.id.layoutNotification);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error initializing views", e);
        }
    }

    private void loadUserData() {
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();
            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String fullName = documentSnapshot.getString("fullName");
                            String email = documentSnapshot.getString("email");
                            String phone = documentSnapshot.getString("phone");

                            textViewName.setText(fullName);
                            // Hiển thị email nếu là email thật, ngược lại hiển thị số điện thoại
                            if (email.contains("@orderfood.com")) {
                                textViewEmail.setText(phone);
                            } else {
                                textViewEmail.setText(email);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        LogUtils.error(TAG, "Error loading user data", e);
                    });
        }
    }

    private void setupClickListeners() {
        try {
            buttonEditProfile.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
            });

            buttonLogout.setOnClickListener(v -> {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            });

            layoutMyOrders.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
                startActivity(intent);
            });

            layoutAddress.setOnClickListener(v -> {
                AddressListFragment addressListFragment = new AddressListFragment();
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, addressListFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            layoutPayment.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
            });

            layoutNotification.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up click listeners", e);
        }
    }
}
