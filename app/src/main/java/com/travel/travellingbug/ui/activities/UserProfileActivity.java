package com.travel.travellingbug.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.travel.travellingbug.R;
import com.travel.travellingbug.ui.adapters.UserProfileViewPagerAdapter;


public class UserProfileActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;
    UserProfileViewPagerAdapter profileViewPagerAdapter;
    private String[] titles = {"ABOUT", "REVIEWS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initComponent();
        clickHandlerComponent();
        tabLayoutController();
    }

    private void tabLayoutController() {
        profileViewPagerAdapter = new UserProfileViewPagerAdapter(this);
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