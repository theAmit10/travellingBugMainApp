package com.travel.travellingbug.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.travel.travellingbug.ui.fragments.OnGoingTrips;
import com.travel.travellingbug.ui.fragments.PastTrips;
import com.travel.travellingbug.ui.fragments.ProfileAboutFragment;
import com.travel.travellingbug.ui.fragments.ProfileAccountFragment;


public class YourRideViewPagerAdapter extends FragmentStateAdapter {

    private String[] titles = {"Upcomming Rider", "Your Ride"};


    public YourRideViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public YourRideViewPagerAdapter(FragmentManager childFragmentManager) {
        super(childFragmentManager.getPrimaryNavigationFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new OnGoingTrips();
            case 1:
                return new PastTrips();


        }
        return new ProfileAboutFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }


}
