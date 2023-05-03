package com.travel.travellingbug.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import com.travel.travellingbug.R;
import com.travel.travellingbug.ui.adapters.MainActivityViewPagerAdapter;
import com.travel.travellingbug.ui.fragments.PublishFragment;
import com.travel.travellingbug.utills.ResponseListener;

import org.json.JSONArray;

public class HomeScreenActivity extends AppCompatActivity implements
        PublishFragment.HomeFragmentListener,
        ResponseListener {

    ViewPager2 viewPager;
    TabLayout tabLayout;



    private String[] titles = {"Search", "Publish", "Your Ride", "Holiday", "Account"};

    private int[] tabIcons = {
            R.drawable.search,
            R.drawable.publish,
            R.drawable.bg_car,
            R.drawable.bn_holiday_package,
            R.drawable.bn_account
    };
    MainActivityViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        settingFragmentViewPager();



    }

    private void settingFragmentViewPager() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPagerAdapter = new MainActivityViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);


        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> tab.setIcon(tabIcons[position]))).attach();


        tabLayout.getTabAt(0).setText("Search").setIcon(R.drawable.search).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_LABELED);
        tabLayout.getTabAt(1).setText("Publish").setIcon(R.drawable.publish).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_LABELED);
        tabLayout.getTabAt(2).setText("Your Ride").setIcon(R.drawable.bg_car);
        tabLayout.getTabAt(3).setText("Holiday").setIcon(R.drawable.bn_holiday_package);
        tabLayout.getTabAt(4).setText("Account").setIcon(R.drawable.bn_account);


    }

    @Override
    public void getJSONArrayResult(String strTag, JSONArray arrayResponse) {

    }
}