package com.travel.travellingbug.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.listeners.Connect;
import com.travel.travellingbug.ui.activities.login.LoginActivity;
import com.travel.travellingbug.ui.adapters.MainActivityViewPagerAdapter;
import com.travel.travellingbug.ui.fragments.AccoutFragment;
import com.travel.travellingbug.ui.fragments.DriverMapFragment;
import com.travel.travellingbug.ui.fragments.HolidayPackageFragment;
import com.travel.travellingbug.ui.fragments.PublishFragment;
import com.travel.travellingbug.ui.fragments.SearchFragment;
import com.travel.travellingbug.ui.fragments.SummaryFragment;
import com.travel.travellingbug.ui.fragments.YourRideFragment;
import com.travel.travellingbug.utills.CustomTypefaceSpan;
import com.travel.travellingbug.utills.ResponseListener;
import com.travel.travellingbug.utills.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class HomeScreenActivity extends AppCompatActivity implements
        SearchFragment.HomeFragmentListener,
        ResponseListener {

    ViewPager2 viewPager;
    TabLayout tabLayout;

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
    private TextView txtName, approvaltxt;

    private RatingBar rateRatingBar;
    private ImageView status;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private NotificationManager notificationManager;

    Animation slide_down, slide_up, slide_up_top, slide_up_down;


    private String[] titles = {"Search", "Publish", "Your Ride", "Holiday", "Account"};

    private int[] tabIcons = {
            R.drawable.nav_search_ic,
            R.drawable.nav_add_ic,
            R.drawable.nav_car_ic,
            R.drawable.nav_plane_ic,
            R.drawable.nav_account_ic
    };
    MainActivityViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        activity = this;
        context = getApplicationContext();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (SharedHelper.getKey(context, "login_by").equals("facebook"))
            FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home_screen);


        settingFragmentViewPager();

        int HeightWin = getNavigationBarHeight();
        System.out.println("height : "+HeightWin );

        slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        slide_up_top = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_top);
        slide_up_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_down);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 1:
                        HomeScreenActivity.this.runOnUiThread(() -> {
                            fragment = new PublishFragment();
                            FragmentManager manager = getSupportFragmentManager();
                            @SuppressLint("CommitTransaction")
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            transaction.replace(R.id.content, fragment);
                            transaction.commit();
                            fragmentManager = getSupportFragmentManager();
                        });
                        GoToFragment();
                        break;
                    case 2:

                        HomeScreenActivity.this.runOnUiThread(() -> {
                            fragment = new YourRideFragment();
                            FragmentManager manager = getSupportFragmentManager();
                            @SuppressLint("CommitTransaction")
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.slide_out_right);
                            transaction.replace(R.id.content, fragment);
                            transaction.commit();
                            fragmentManager = getSupportFragmentManager();
                        });
                        GoToFragment();
                        break;

                    case 3:

                        HomeScreenActivity.this.runOnUiThread(() -> {
                            fragment = new HolidayPackageFragment();
                            FragmentManager manager = getSupportFragmentManager();
                            @SuppressLint("CommitTransaction")
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.content, fragment);
                            transaction.commit();
                            fragmentManager = getSupportFragmentManager();
                        });
                        GoToFragment();
                        break;

                    case 4:

                        HomeScreenActivity.this.runOnUiThread(() -> {
                            fragment = new AccoutFragment();
                            FragmentManager manager = getSupportFragmentManager();
                            @SuppressLint("CommitTransaction")
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.content, fragment);
                            transaction.commit();
                            fragmentManager = getSupportFragmentManager();
                        });
                        GoToFragment();
                        break;
                    case 0:
                    default:
                        HomeScreenActivity.this.runOnUiThread(() -> {
                            fragment = new SearchFragment();
                            FragmentManager manager = getSupportFragmentManager();
                            @SuppressLint("CommitTransaction")
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.content, fragment);
                            transaction.commit();
                            fragmentManager = getSupportFragmentManager();
                        });
                        GoToFragment();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        Bundle extras = getIntent().getExtras();
        notificationManager = (NotificationManager)

                getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        if (extras != null) {
            push = extras.getBoolean("push");
        }

        map();

        Connect.addMyBooleanListener(() -> Toast.makeText(

                        getApplication(),
                        "Changed", Toast.LENGTH_SHORT).

                show());

        loadNavHeader();

        setUpNavigationView();

//        navHeader.setOnClickListener(view ->
//
//        {
//            drawer.closeDrawers();
//            new Handler().postDelayed(() -> startActivity(new Intent(activity,
//                    UserProfileActivity.class)), 250);
//        });
        if (

                getIntent().

                        getStringExtra("status") != null) {
            statustg = getIntent().getStringExtra("status");
        }


    }

    private int getNavigationBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    private void settingFragmentViewPager() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        txtName = navHeader.findViewById(R.id.usernameTxt);
        approvaltxt = navHeader.findViewById(R.id.status_txt);
        imgProfile = navHeader.findViewById(R.id.img_profile);
        rateRatingBar = navHeader.findViewById(R.id.rateRatingBar);
        status = navHeader.findViewById(R.id.status);


        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPagerAdapter = new MainActivityViewPagerAdapter(this);

        // to disable swipe between different fragment
        viewPager.setUserInputEnabled(false);

        viewPager.setAdapter(viewPagerAdapter);


        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> tab.setIcon(tabIcons[position]))).attach();


        tabLayout.getTabAt(0).setText("Search").setIcon(R.drawable.nav_search_ic).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_LABELED);
        tabLayout.getTabAt(1).setText("Publish").setIcon(R.drawable.nav_add_ic).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_LABELED);
        tabLayout.getTabAt(2).setText("Your Ride").setIcon(R.drawable.nav_car_ic);
        tabLayout.getTabAt(3).setText("Holiday").setIcon(R.drawable.nav_plane_ic);
        tabLayout.getTabAt(4).setText("Account").setIcon(R.drawable.nav_account_ic);




    }

    @Override
    public void getJSONArrayResult(String strTag, JSONArray arrayResponse) {

    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    if (CURRENT_TAG != TAG_HOME) {
                        fragment = new SearchFragment();
                        GoToFragment();
                    } else {
                        drawer.closeDrawers();
                    }
                    break;
                case R.id.nav_profile:
                    drawer.closeDrawers();
                    new Handler().postDelayed(() -> startActivity(new Intent(activity, DriverProfileActivity.class)),
                            250);
                    break;
                case R.id.nav_document:

                    startActivity(new Intent(activity, DocumentStatus.class));
                    break;

                //
                case R.id.nav_yourtrips:
                    drawer.closeDrawers();
                    new Handler().postDelayed(() -> startActivity(new Intent(HomeScreenActivity.this,
                            HistoryActivity.class)), 250);

                    break;
                case R.id.nav_withdraw:
                    drawer.closeDrawers();
//                    Intent intent = new Intent(HomeScreenActivity.this,TrackActivity.class);
//                    intent.putExtra("flowValue", 3);
//                    intent.putExtra("request_id_from_trip", "121");
//                    startActivity(intent);

                    break;
                case R.id.nav_notification:
                    drawer.closeDrawers();
                    new Handler().postDelayed(() -> startActivity(new Intent(HomeScreenActivity.this,
                            NotificationTab.class)), 250);


                    break;
                case R.id.nav_wallet:
                    navItemIndex = 2;
                    CURRENT_TAG = TAG_SUMMARY;
                    fragment = new SummaryFragment();
                    drawer.closeDrawers();
                    new Handler().postDelayed(() -> {
                        FragmentManager manager2 = getSupportFragmentManager();
                        @SuppressLint("CommitTransaction")
                        FragmentTransaction transaction1 = manager2.beginTransaction();
                        transaction1.replace(R.id.content, fragment);
                        transaction1.addToBackStack(null);
                        transaction1.commit();
                    }, 250);

                    //GoToFragment();
                    break;
                case R.id.nav_help:
//                    navItemIndex = 3;
//                    CURRENT_TAG = TAG_HELP;
//                    fragment = new Help();
//                    drawer.closeDrawers();
//                    new Handler().postDelayed(() -> {
//                        FragmentManager manager4 = getSupportFragmentManager();
//                        @SuppressLint("CommitTransaction")
//                        FragmentTransaction transaction2 = manager4.beginTransaction();
//                        transaction2.replace(R.id.content, fragment);
//                        transaction2.addToBackStack(null);
//                        transaction2.commit();
//                    }, 250);

                    drawer.closeDrawers();
                    new Handler().postDelayed(() -> startActivity(new Intent(HomeScreenActivity.this,
                            HelpActivity.class)), 250);

                    //GoToFragment();
                    break;
                case R.id.nav_earnings:
                    drawer.closeDrawers();
                    new Handler().postDelayed(() -> startActivity(new Intent(activity,
                            EarningActivity.class)), 250);
                    break;
                case R.id.nav_share:
                    drawer.closeDrawers();
                    navigateToShareScreen(URLHelper.APP_URL);
                    return true;
                case R.id.nav_logout:
                    showLogoutDialog();
                    return true;
                default:
                    navItemIndex = 0;
            }
            return true;
        });

        Menu m = navigationView.getMenu();

        for (int i = 0; i < m.size(); i++) {
            MenuItem menuItem = m.getItem(i);
            applyFontToMenuItem(menuItem);

        }
        ActionBarDrawerToggle actionBarDrawerToggle = new
                ActionBarDrawerToggle(this, drawer, toolbar,
                        R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            loadNavHeader();
                        }
                    }
                };
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/nunito_regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0,
                mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void loadNavHeader() {
        // name, website

        if(SharedHelper.getKey(context, "first_name").equalsIgnoreCase("null")){
            txtName.setText("user");
        }else {
            txtName.setText(SharedHelper.getKey(context, "first_name"));
        }

        if (SharedHelper.getKey(context, "approval_status").equals("new") ||
                SharedHelper.getKey(context, "approval_status").equals("onboarding")) {
            approvaltxt.setTextColor(Color.YELLOW);
            approvaltxt.setText(getText(R.string.waiting_for_approval));
            status.setImageResource(R.drawable.newuser);
        } else if (SharedHelper.getKey(context, "approval_status").equals("banned")) {
            approvaltxt.setTextColor(Color.RED);
            approvaltxt.setText(getText(R.string.banned));
            status.setImageResource(R.drawable.banned);
        } else {
            approvaltxt.setTextColor(Color.WHITE);
            approvaltxt.setText(getText(R.string.approved));
            status.setImageResource(R.drawable.approved);
        }

        utils.print("Profile_PIC", "" + SharedHelper.getKey(context, "picture") + " ");
        utils.print("vehicle PIC", "" + SharedHelper.getKey(context, "service_image") + " ");
        if (SharedHelper.getKey(context, "picture") != null
                && !SharedHelper.getKey(context, "picture").isEmpty()) {
            Picasso.get().load(SharedHelper.getKey(context, "picture"))
                    .placeholder(R.drawable.ic_dummy_user)
                    .error(R.drawable.ic_dummy_user)
                    .into(imgProfile);
        }
        if (SharedHelper.getKey(HomeScreenActivity.this, "rating") != null
                && SharedHelper.getKey(HomeScreenActivity.this, "rating") != "") {
            if(SharedHelper.getKey(HomeScreenActivity.this, "rating").equalsIgnoreCase("null")){
                rateRatingBar.setRating(Float.parseFloat("0"));
                rateRatingBar.setVisibility(View.GONE);
            }else {

                String rate_val = SharedHelper.getKey(HomeScreenActivity.this, "rating");
                int rate_valu = rate_val.indexOf(".");
                String rate_value = rate_val.substring(0,rate_valu);

                rateRatingBar.setRating(Float.parseFloat(rate_val));
                rateRatingBar.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                fragment = new SearchFragment();
                GoToFragment();
                return;
            } else {
                System.exit(0);
            }
        }

        super.onBackPressed();
    }


    private void map() {
        HomeScreenActivity.this.runOnUiThread(() -> {
            fragment = new SearchFragment();
            FragmentManager manager = getSupportFragmentManager();
            @SuppressLint("CommitTransaction")
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content, fragment);
            transaction.commit();
            fragmentManager = getSupportFragmentManager();
        });
    }

    public void GoToFragment() {
        HomeScreenActivity.this.runOnUiThread(() -> {
            drawer.closeDrawers();
            FragmentManager manager = getSupportFragmentManager();
            @SuppressLint("CommitTransaction")
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content, fragment);
            transaction.commit();
        });
    }

    public void navigateToShareScreen(String shareUrl) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl + " -via " + getString(R.string.app_name));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void enableLoc() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        mGoogleApiClient.connect();
                    }
                })
                .addOnConnectionFailedListener(connectionResult ->
                        utils.print("Location error", "Location error " +
                                connectionResult.getErrorCode())).build();
        mGoogleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(HomeScreenActivity.this, REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;
            }
        });
//	        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
       /* if (requestCode == fragment.REQUEST_LOCATION){
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }*/
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void logout() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", SharedHelper.getKey(context, "id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.POST,
                        URLHelper.LOGOUT,
                        object,
                        response -> {
//                            if (isMyServiceRunning(StatusCheckServie.class)) {
////                                stopService(new Intent(getApplicationContext(), StatusCheckServie.class));
//                            }
                            drawer.closeDrawers();
                            if (SharedHelper.getKey(context, "login_by").equals("facebook"))
                                LoginManager.getInstance().logOut();
                            if (SharedHelper.getKey(context, "login_by").equals("google"))
                                signOut();
                            if (!SharedHelper.getKey(HomeScreenActivity.this, "account_kit_token").equalsIgnoreCase("")) {
                                Log.e("MainActivity", "Account kit logout: " + SharedHelper.getKey(HomeScreenActivity.this, "account_kit_token"));

                                SharedHelper.putKey(HomeScreenActivity.this, "account_kit_token", "");
                            }


                            //SharedHelper.putKey(context, "email", "");
                            SharedHelper.clearSharedPreferences(HomeScreenActivity.this);
                            Intent goToLogin = new Intent(activity, LoginActivity.class);
                            goToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(goToLogin);
                            finishAffinity();
                        }, error -> {
                    displayMessage(getString(R.string.something_went_wrong));
                }) {
                    @Override
                    public java.util.Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        Log.e("getHeaders: Token", SharedHelper.getKey(context, "access_token") + SharedHelper.getKey(context, "token_type"));
                        headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(context, "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //taken from google api console (Web api client id)
//                .requestIdToken("795253286119-p5b084skjnl7sll3s24ha310iotin5k4.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {

                FirebaseAuth.getInstance().signOut();
                if (mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(status -> {
                        if (status.isSuccess()) {
                            Log.d("MainAct", "Google User Logged out");
                           /* Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();*/
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d("MAin", "Google API Client Connection Suspended");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        startService(new Intent(MainActivity.this, StatusCheckServie.class));
    }

    public void displayMessage(String toastString) {
        Log.e("displayMessage", "" + toastString);
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    private void showLogoutDialog() {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.logout));
            builder.setMessage(getString(R.string.exit_confirm));

            builder.setPositiveButton(R.string.yes,
                    (dialog, which) -> logout());

            builder.setNegativeButton(R.string.no, (dialog, which) -> {
                //Reset to previous seletion menu in navigation
                dialog.dismiss();
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            });
            builder.setCancelable(false);
            final AlertDialog dialog = builder.create();
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.setOnShowListener(arg -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            });
            dialog.show();
        }
    }
}