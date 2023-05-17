package com.travel.travellingbug.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.travel.travellingbug.R;
import com.travel.travellingbug.ui.adapters.DriverProfileViewPagerAdapter;

public class DriverProfileActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;
    DriverProfileViewPagerAdapter profileViewPagerAdapter;
    private String[] titles = {"ABOUT", "ACCOUNT"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        initComponent();
        clickHandlerComponent();
        tabLayoutController();
    }

    private void tabLayoutController() {
        profileViewPagerAdapter = new DriverProfileViewPagerAdapter(this);
        viewPager.setAdapter(profileViewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> tab.setText(titles[position]))).attach();



        tabLayout.getTabAt(0).setText("About");
        tabLayout.getTabAt(1).setText("Reviews");

    }

    private void clickHandlerComponent() {
    }

    private void initComponent() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

    }

}