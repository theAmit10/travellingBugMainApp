package com.travel.travellingbug.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.travel.travellingbug.R;
import com.travel.travellingbug.ui.adapters.YourRideViewPagerAdapter;


public class YourRideFragment extends Fragment {



    boolean doubleBackToExitPressedOnce = false;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    YourRideViewPagerAdapter yourRideViewPagerAdapter;
    private String[] titles = {"Upcomming Rides", "Your Ride"};


    public YourRideFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_your_ride, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        initComponent();
        tabLayoutController();
        clickHandlerComponent();


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN)
                return true;
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (doubleBackToExitPressedOnce) {
                    getActivity().finish();
                    return false;
                }
                doubleBackToExitPressedOnce = true;
                Toast.makeText(getActivity(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 5000);
                return true;
            }
            return false;
        });

        return  view;
    }

    private void tabLayoutController() {
        yourRideViewPagerAdapter = new YourRideViewPagerAdapter(getActivity());
        viewPager.setAdapter(yourRideViewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);


        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> tab.setText(titles[position]))).attach();


        tabLayout.getTabAt(0).setText("Upcoming Ride");
        tabLayout.getTabAt(1).setText("Completed Ride");


    }

    private void clickHandlerComponent() {
    }

    private void initComponent() {


    }
}