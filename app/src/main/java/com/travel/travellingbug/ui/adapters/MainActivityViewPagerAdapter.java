package com.travel.travellingbug.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.travel.travellingbug.ui.fragments.AccoutFragment;
import com.travel.travellingbug.ui.fragments.HolidayPackageFragment;
import com.travel.travellingbug.ui.fragments.PublishFragment;
import com.travel.travellingbug.ui.fragments.SearchFragment;
import com.travel.travellingbug.ui.fragments.YourRideFragment;


public class MainActivityViewPagerAdapter extends FragmentStateAdapter {

    private String[] titles = {"Search", "Publish","Your Ride","Holiday Package","Account"};


    public MainActivityViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SearchFragment();
            case 1:
                return new PublishFragment();
            case 2:
                return new YourRideFragment();
            case 3:
                return new HolidayPackageFragment();
            case 4:
                return new AccoutFragment();

        }
        return new SearchFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }


}

