package com.travel.travellingbug.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.travel.travellingbug.R;
import com.travel.travellingbug.ui.adapters.ProfileViewPagerAdapter;

public class AccoutFragment extends Fragment {


    public AccoutFragment() {
        // Required empty public constructor
    }

    ViewPager2 viewPager;
    TabLayout tabLayout;
    ProfileViewPagerAdapter profileViewPagerAdapter;
    private String[] titles = {"About", "Account"};



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_accout,container,false);

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        initComponent();
        tabLayoutController();
        clickHandlerComponent();


        // Inflate the layout for this fragment
        return view;
    }
    private void tabLayoutController() {
        profileViewPagerAdapter = new ProfileViewPagerAdapter(getActivity());
        viewPager.setAdapter(profileViewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);


        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> tab.setText(titles[position]))).attach();


        tabLayout.getTabAt(0).setText("About You");
        tabLayout.getTabAt(1).setText("Account");


    }

    private void clickHandlerComponent() {
    }

    private void initComponent() {


    }
}