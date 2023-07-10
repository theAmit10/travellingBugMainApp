package com.travel.travellingbug.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.travel.travellingbug.R;
import com.travel.travellingbug.ui.adapters.RatingViewPagerAdapter;

public class ProviderReviewActivity extends AppCompatActivity {


    ImageView backArrow;




    ViewPager2 viewPager;
    TabLayout tabLayout;
    RatingViewPagerAdapter ratingViewPagerAdapter;
    private String[] titles = {"ABOUT", "REVIEWS"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_review);


        initData();

        tabLayoutController();



        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void tabLayoutController() {
        ratingViewPagerAdapter = new RatingViewPagerAdapter(this);
        viewPager.setAdapter(ratingViewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> tab.setText(titles[position]))).attach();



        tabLayout.getTabAt(0).setText("Received");
        tabLayout.getTabAt(1).setText("Given");

    }



    private void initData() {

        backArrow = findViewById(R.id.backArrow);




        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);


    }
}