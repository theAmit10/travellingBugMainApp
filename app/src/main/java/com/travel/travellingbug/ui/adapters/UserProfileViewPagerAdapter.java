package com.travel.travellingbug.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.travel.travellingbug.ui.fragments.UserProfileAboutFragment;
import com.travel.travellingbug.ui.fragments.UserProfileReviewFragment;


public class UserProfileViewPagerAdapter extends FragmentStateAdapter {

    private String[] titles = {"About", "Reviews"};


    public UserProfileViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new UserProfileAboutFragment();
            case 1:
                return new UserProfileReviewFragment();


        }
        return new UserProfileAboutFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }


}

