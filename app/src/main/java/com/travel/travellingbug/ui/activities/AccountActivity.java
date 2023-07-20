package com.travel.travellingbug.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.travel.travellingbug.R;
import com.travel.travellingbug.ui.adapters.ProfileViewPagerAdapter;

public class AccountActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;
    ProfileViewPagerAdapter profileViewPagerAdapter;
    private String[] titles = {"About", "Account"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        initComponent();
        tabLayoutController();
        clickHandlerComponent();
    }

    private void tabLayoutController() {
        profileViewPagerAdapter = new ProfileViewPagerAdapter(this);
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