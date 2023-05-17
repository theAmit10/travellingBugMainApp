package com.travel.travellingbug.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.travel.travellingbug.ui.fragments.DriverProfileAboutFragment;
import com.travel.travellingbug.ui.fragments.DriverProfileReviewFragment;


public class DriverProfileViewPagerAdapter extends FragmentStateAdapter {

    private String[] titles = {"ABOUT", "Reviews"};


    public DriverProfileViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new DriverProfileAboutFragment();
            case 1:
                return new DriverProfileReviewFragment();


        }
        return new DriverProfileAboutFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }


}
