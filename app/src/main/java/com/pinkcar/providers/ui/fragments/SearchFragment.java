package com.pinkcar.providers.ui.fragments;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Intent.getIntent;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.facebook.FacebookSdk.getApplicationContext;

import static cn.jzvd.JZUtils.getWindow;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.pinkcar.providers.R;
import com.pinkcar.providers.helper.SharedHelper;
import com.pinkcar.providers.listeners.Connect;
import com.pinkcar.providers.ui.activities.Profile;
import com.pinkcar.providers.ui.adapters.MainActivityViewPagerAdapter;
import com.pinkcar.providers.utills.Utilities;


public class SearchFragment extends Fragment {

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_SUMMARY = "summary";
    private static final String TAG_HELP = "help";
    private static final int REQUEST_LOCATION = 1450;
    public static FragmentManager fragmentManager;
    public static String statustg = "";
    // index to identify current nav menu item
    public int navItemIndex = 0;
    public String CURRENT_TAG = TAG_HOME;
    Fragment fragment;
    Activity activity;
    Context context;
    Toolbar toolbar;
    Utilities utils = new Utilities();
    boolean push = false;
    GoogleApiClient mGoogleApiClient;
    DriverMapFragment lFrag;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgProfile;
    private TextView txtName, approvaltxt, tvRate;
    private ImageView status;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private NotificationManager notificationManager;

    // for fraement Change
    ViewPager2 viewPager;
    TabLayout tabLayout;


    private String[] titles = {"Search", "Publish","Your Ride","Holiday Package","Account"};
    private int[] tabIcons = {
            R.drawable.search,
            R.drawable.publish,
            R.drawable.bg_car,
            R.drawable.bn_holiday_package,
            R.drawable.bn_account
    };
    MainActivityViewPagerAdapter viewPagerAdapter;



    public SearchFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_search, container, false);





        return view;
    }
}