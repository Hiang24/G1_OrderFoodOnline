package com.example.g1_orderfoodonline.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.g1_orderfoodonline.fragments.CartFragment;
import com.example.g1_orderfoodonline.fragments.HomeFragment;
import com.example.g1_orderfoodonline.fragments.MenuFragment;
import com.example.g1_orderfoodonline.fragments.ProfileFragment;

public class FragmentHelper {
    private final FragmentManager fragmentManager;
    private final int containerId;

    public FragmentHelper(FragmentManager fragmentManager, int containerId) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
    }

    public void loadFragment(Constants.FragmentType fragmentType) {
        Fragment fragment = getFragmentByType(fragmentType);
        if (fragment != null) {
            fragmentManager.beginTransaction()
                    .replace(containerId, fragment)
                    .commit();
        }
    }

    private Fragment getFragmentByType(Constants.FragmentType fragmentType) {
        switch (fragmentType) {
            case HOME:
                return new HomeFragment();
            case MENU:
                return new MenuFragment();
            case CART:
                return new CartFragment();
            case PROFILE:
                return new ProfileFragment();
            default:
                return null;
        }
    }
} 