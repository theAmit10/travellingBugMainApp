package com.travel.travellingbug.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.travel.travellingbug.ui.fragments.RatingGivenFragment;
import com.travel.travellingbug.ui.fragments.RatingReceivedFragment;


public class RatingViewPagerAdapter extends FragmentStateAdapter {

    private String[] titles = {"Received", "Given"};


    public RatingViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new RatingReceivedFragment();
            case 1:
                return new RatingGivenFragment();


        }
        return new RatingReceivedFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }


}
