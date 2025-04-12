package com.example.g1_orderfoodonline.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.fragments.CartFragment;
import com.example.g1_orderfoodonline.fragments.HomeFragment;
import com.example.g1_orderfoodonline.fragments.MenuFragment;
import com.example.g1_orderfoodonline.fragments.ProfileFragment;
import com.example.g1_orderfoodonline.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupBottomNavigation();

        // Set default fragment or load specific fragment from intent
        if (savedInstanceState == null) {
            String fragmentToLoad = getIntent().getStringExtra("fragment");
            if (fragmentToLoad != null) {
                switch (fragmentToLoad) {
                    case "menu":
                        loadFragment(new MenuFragment());
                        bottomNavigationView.setSelectedItemId(R.id.navigation_menu);
                        break;
                    case "cart":
                        loadFragment(new CartFragment());
                        bottomNavigationView.setSelectedItemId(R.id.navigation_cart);
                        break;
                    case "profile":
                        loadFragment(new ProfileFragment());
                        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
                        break;
                    default:
                        loadFragment(new HomeFragment());
                        break;
                }
            } else {
                loadFragment(new HomeFragment());
            }
        }
    }

    private void initViews() {
        try {
            bottomNavigationView = findViewById(R.id.bottom_navigation);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error initializing views", e);
        }
    }

    private void setupBottomNavigation() {
        try {
            bottomNavigationView.setOnItemSelectedListener(item -> {
                Fragment fragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home) {
                    fragment = new HomeFragment();
                } else if (itemId == R.id.navigation_menu) {
                    fragment = new MenuFragment();
                } else if (itemId == R.id.navigation_cart) {
                    fragment = new CartFragment();
                } else if (itemId == R.id.navigation_profile) {
                    fragment = new ProfileFragment();
                }

                return loadFragment(fragment);
            });
        } catch (Exception e) {
            LogUtils.error(TAG, "Error setting up bottom navigation", e);
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            try {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
                return true;
            } catch (Exception e) {
                LogUtils.error(TAG, "Error loading fragment", e);
                return false;
            }
        }
        return false;
    }
}
