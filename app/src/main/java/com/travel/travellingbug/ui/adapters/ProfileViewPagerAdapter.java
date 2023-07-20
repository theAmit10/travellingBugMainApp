package com.travel.travellingbug.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.travel.travellingbug.ui.fragments.ProfileAboutFragment;
import com.travel.travellingbug.ui.fragments.ProfileAccountFragment;


public class ProfileViewPagerAdapter extends FragmentStateAdapter {

    private String[] titles = {"About", "Account"};


    public ProfileViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

//    public ProfileViewPagerAdapter(FragmentManager childFragmentManager) {
//        super(childFragmentManager.getPrimaryNavigationFragment());
//    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ProfileAboutFragment();
            case 1:
                return new ProfileAccountFragment();


        }
        return new ProfileAboutFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }


}
