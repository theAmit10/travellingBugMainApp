package com.travel.travellingbug.ui.fragments;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.PlacePredictions;
import com.travel.travellingbug.models.SearchHistoryModel;
import com.travel.travellingbug.ui.activities.FindRidesActivity;
import com.travel.travellingbug.ui.activities.SetAddressActivity;
import com.travel.travellingbug.ui.activities.UpdateProfile;
import com.travel.travellingbug.ui.adapters.SearchHistoryAdpater;
import com.travel.travellingbug.ui.adapters.SearchHistoryItemClickListener;
import com.travel.travellingbug.utills.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;



public class FSearch extends Fragment {
    Activity activity;
    Context context;
    TextView frmSource;
    TextView frmDest;
    TextView calendertv;
    TextView persontv;
    Button btnSearch;
    String passangerStr = "";
    int passenger_number = 1;
    private String scheduledDate = "";
    DatePickerDialog datePickerDialog;
    Utilities utils = new Utilities();
    String cd = "";
    String cm = "";
    String cy = "";
    DrawerLayout drawer;
    int NAV_DRAWER = 0;
    boolean afterToday = false;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST = 18945;

    public static String TAG =  "FSEARCH";
    PlacePredictions placePredictions;
    //Internet
    ConnectionHelper helper;

    String current_lat = "", current_lng = "", current_address = "", source_lat = "",
            source_lng = "", source_address = "",
            dest_lat = "", dest_lng = "", dest_address = "";
    String strPickLocation = "", strPickType = "";
    boolean doubleBackToExitPressedOnce = false;

    HomeFragmentListener listener;

    CustomDialog customDialog;

    ArrayList<SearchHistoryModel> mSearchHistoryModel;

    RelativeLayout searchHistoryRelativeLayout;
    RecyclerView searchHistoryRv;
    SearchHistoryItemClickListener searchHistoryItemClickListener;

    private static int deviceHeight;
    private static int deviceWidth;

    Boolean isInternet;
    String notificationTxt;
    boolean push = false;
    boolean isRunning = false, timerCompleted = false;

    ImageView imgMenu;

    ImageView homeScreenImage;

    View view;

    public FSearch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            notificationTxt = bundle.getString("Notification");

        }

        if (bundle != null) {
            push = bundle.getBoolean("push");
        }
        if (push) {
            isRunning = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_f_search, container, false);


            view = inflater.inflate(R.layout.fragment_f_search, container, false);
            ButterKnife.bind(this, view);



        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceHeight = displayMetrics.heightPixels;
        deviceWidth = displayMetrics.widthPixels;


        initializeViews(view);



//        if (activity != null && isAdded()) {
//            getProfile();
//            getDocList();
//
//        }

//        try {
//            getProfile();
//            getDocList();
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        if (activity != null && isAdded()) {
                new Handler().postDelayed(() -> {
//                    initializeViews(view);
                    clickHandler();
                    try {
                        getProfile();
                        getDocList();
                        getHomeScreenImage();

                        try {
                            if(SharedHelper.getKey(getContext(), "homeScreenImage") != null){
                                Picasso.get().load(SharedHelper.getKey(getContext(), "homeScreenImage")).error(R.drawable.travelling_home_screen).placeholder(R.drawable.travelling_home_screen).into(homeScreenImage);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }, 500);

        }




        if (SharedHelper.getKey(getContext(), "Old_User").equalsIgnoreCase("yes")) {
            // Setting Name First
            System.out.println("OldUser : " + SharedHelper.getKey(getContext(), "Old_User"));

        } else {
            if (SharedHelper.getKey(getContext(), "first_name").equalsIgnoreCase("null") || SharedHelper.getKey(getContext(), "first_name").equalsIgnoreCase("")) {
                Toast.makeText(getContext(), "Add your name to Continue", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), UpdateProfile.class);
                intent.putExtra("parameter", "first_name");
                intent.putExtra("value", "");
                startActivityForResult(intent, 1);
                getActivity().overridePendingTransition(R.anim.emoji_slide_down, R.anim.emoji_slide_up);
            }
        }


//        initializeViews(view);
//        clickHandler();

        // For Android 13 Notication Permission
        // Request the POST_NOTIFICATIONS permission.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if the user granted the permission.
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Send the notification.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);
            }

        }


        // Adding Current Date to the Calender Textview

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        // Check if the date is today and update the TextView accordingly
        if (isToday(currentDate)) {
            scheduledDate = formattedDate;
            calendertv.setText("Today");
        }



        searchHistoryItemClickListener = new SearchHistoryItemClickListener() {
            @Override
            public void onClick(int position, SearchHistoryModel searchHistoryModel) {

                frmSource.setText(searchHistoryModel.getFromAddress());
                frmDest.setText(searchHistoryModel.getDestAddress());
                source_address = searchHistoryModel.getFromAddress();
                dest_address = searchHistoryModel.getDestAddress();

                source_lat = searchHistoryModel.getSlat();
                source_lng = searchHistoryModel.getSlong();
                dest_lat = searchHistoryModel.getDlat();
                dest_lng = searchHistoryModel.getDlong();

                setValuesForSourceAndDestination();

            }
        };


        //setting search history
        searchHistoryRelativeLayout = view.findViewById(R.id.searchHistoryRelativeLayout);
        searchHistoryRv = view.findViewById(R.id.searchHistoryRv);

        try {

            if (SharedHelper.getKey(getContext(), "getSearchHistory") != null) {
                System.out.println("getSearchHistory : "+SharedHelper.getKey(getContext(), "getSearchHistory"));
                String json = SharedHelper.getKey(getContext(), "getSearchHistory");
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<SearchHistoryModel>>() {
                }.getType();
                mSearchHistoryModel = gson.fromJson(json, type);
                if(mSearchHistoryModel != null){
                    System.out.println("mSearchHistoryModel size : "+mSearchHistoryModel.size());
                    for(int i=0;i<mSearchHistoryModel.size(); i++){
                        System.out.println("mSearchHistoryModel add : "+mSearchHistoryModel.get(i).getFromAddress());
                        System.out.println("mSearchHistoryModel pass : "+mSearchHistoryModel.get(i).getPassenger());
                    }

                    ArrayList<SearchHistoryModel> desenList = new ArrayList<>();
                    int j = 0;
                    for(int i=mSearchHistoryModel.size()-1; i>=0; i--){
                        desenList.add(mSearchHistoryModel.get(i));
                        j++;
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    searchHistoryRv.setLayoutManager(linearLayoutManager);
                    searchHistoryRv.setNestedScrollingEnabled(false);
                    SearchHistoryAdpater searchHistoryAdpater = new SearchHistoryAdpater(getContext(), desenList,searchHistoryItemClickListener);
                    searchHistoryRv.setAdapter(searchHistoryAdpater);
                    searchHistoryRelativeLayout.setVisibility(View.VISIBLE);

                }




            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        // for Closing App
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN)
                return true;
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (doubleBackToExitPressedOnce) {

                    System.exit(0);
                    return false;
                }
                doubleBackToExitPressedOnce = true;
                exitConfirmation();

                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 5000);
                return true;
            }
            return false;
        });



        return  view;
    }


    public void setValuesForSourceAndDestination() {
        try {
            if (isInternet) {


                if (!source_lat.equalsIgnoreCase("")) {
                    if (!source_address.equalsIgnoreCase("")) {
                        frmSource.setText(source_address);


                    } else {
                        frmSource.setText(current_address);

                    }
                } else {
                    frmSource.setText(current_address);

                }



                /***************************************CHANGES HERE TO HIDE SOURCE ADDRESS AND DESTINATION ADDRESS TEXTVIEW***********************************************/

                if (!dest_lat.equalsIgnoreCase("")) {

                    frmDest.setText(dest_address);



                }



            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void exitConfirmation() {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmation")
                .setMessage("Do you really want to TravellingBug Services?")
                .setIcon(R.drawable
                        .app_logo_org)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> getActivity().finish())
                .setNegativeButton(android.R.string.no, null).show();
    }


    private void clickHandler() {

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NAV_DRAWER == 0) {
                    if (drawer != null)
                        drawer.openDrawer(GravityCompat.START);
                } else {
                    NAV_DRAWER = 0;
                    if (drawer != null)
                        drawer.closeDrawers();
                }
            }
        });

        persontv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPasengerDialog();
            }
        });

        frmSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetAddressActivity.class);
                intent.putExtra("cursor", "source");
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
                getActivity().overridePendingTransition(R.anim.emoji_slide_down, R.anim.emoji_slide_up);


            }
        });

        frmDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SetAddressActivity.class);
                intent.putExtra("cursor", "destination");
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
                getActivity().overridePendingTransition(R.anim.emoji_slide_down, R.anim.emoji_slide_up);
            }
        });



        calendertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(getActivity(),
                        (view, year, monthOfYear, dayOfMonth) -> {

                            // set day of month , month and year value in the edit text
                            String choosedMonth = "";
                            String choosedDate = "";

                            String choosedDateFormat = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                            scheduledDate = choosedDateFormat;

                            try {
                                choosedMonth = utils.getMonth(choosedDateFormat);
//                                cm = getMonth(choosedDateFormat);
                                cm = choosedMonth;
                                cy = String.valueOf(year);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (dayOfMonth < 10) {
                                choosedDate = "0" + dayOfMonth;
                                cd = choosedDate;
                            } else {
                                choosedDate = "" + dayOfMonth;
                                cd = choosedDate;
                            }
                            afterToday = Utilities.isAfterToday(year, monthOfYear, dayOfMonth);
                            calendertv.setText(choosedDate + "th " + getMonthName(Integer.parseInt(choosedMonth)) + " , " + year);
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate((System.currentTimeMillis() - 1000) + (1000 * 60 * 60 * 24 * 7));
                datePickerDialog.show();



            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("calendertext : " + calendertv.getText().toString());

                if (!frmSource.getText().toString().equalsIgnoreCase("") &&
                        !frmDest.getText().toString().equalsIgnoreCase("")
                        ) {

                    frmDest.setOnClickListener(null);
                    frmSource.setOnClickListener(null);

                    SharedHelper.putKey(getContext(), "name", "");

                    String currentTime = getCurrentTime();
//                    timeTextView.setText("Current Time: " + currentTime);


                    Intent intent3 = new Intent(getActivity(), FindRidesActivity.class);

                    intent3.putExtra("s_latitude", source_lat);
                    intent3.putExtra("s_longitude", source_lng);
                    intent3.putExtra("d_latitude", dest_lat);
                    intent3.putExtra("d_longitude", dest_lng);
                    intent3.putExtra("s_address", source_address);
                    intent3.putExtra("d_address", dest_address);
                    intent3.putExtra("service_type", "2");
                    intent3.putExtra("distance", "0");
                    intent3.putExtra("schedule_date", scheduledDate);
                    intent3.putExtra("schedule_time", currentTime);
                    intent3.putExtra("upcoming", "1");
                    intent3.putExtra("use_wallet", "0");
                    intent3.putExtra("payment_mode", "CASH");
                    intent3.putExtra("seat_count", persontv.getText());
                    startActivity(intent3);


                    Gson gson = new Gson();
                    String json = SharedHelper.getKey(getContext(),"getSearchHistory");
                    Type type = new TypeToken<ArrayList<SearchHistoryModel>>() {}.getType();
                    mSearchHistoryModel = gson.fromJson(json, type);

                    if (mSearchHistoryModel == null) {
                        mSearchHistoryModel = new ArrayList<>();
                    }
                    mSearchHistoryModel.add(new SearchHistoryModel(source_address , dest_address, persontv.getText().toString()+" passenger",source_lat,source_lng,dest_lat,dest_lng));
                    String updatedJson = gson.toJson(mSearchHistoryModel);
                    SharedHelper.putKey(getContext(), "getSearchHistory", updatedJson);

                    System.out.println("mSearchHistoryModel size 2 : STARTED");
                    System.out.println("mSearchHistoryModel size 2 : "+mSearchHistoryModel.size());
                    for(int i=0;i<mSearchHistoryModel.size(); i++){
                        System.out.println("mSearchHistoryModel add 2 : "+mSearchHistoryModel.get(i));
                        System.out.println("mSearchHistoryModel pass 2 : "+mSearchHistoryModel.get(i).getPassenger());
                    }

                } else {
                    Toast.makeText(getContext(), "Please select date and time", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        return currentTime;
    }

    public void getHomeScreenImage() {
        System.out.println("GETTING HOME SCREEN  STARTED : .... ");



        StringRequest request = new StringRequest(Request.Method.GET, URLHelper.HOME_SCREEN_IMAGE , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.length()> 0){
                    System.out.println("HOME SCREEN LENGTH size : "+response.length());
                    System.out.println("Request Data : "+response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        System.out.println("HOME SCREEN id : "+jsonArray.optJSONObject(jsonArray.length()-1).optString("id"));
                        System.out.println("HOME SCREEN image : "+jsonArray.optJSONObject(jsonArray.length()-1).optString("picture"));
                        SharedHelper.putKey(getContext(), "homeScreenImage", jsonArray.optJSONObject(jsonArray.length()-1).optString("picture"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                return headers;
            }

        };

        ClassLuxApp.getInstance().addToRequestQueue(request);

    }


    public void getProfile() {
        System.out.println("GETTING PROFILE STARTED : .... ");


        try{
            if(getContext() != null){
                if (isInternet) {
                    JSONObject object = new JSONObject();
                    JsonObjectRequest jsonObjectRequest = new
                            JsonObjectRequest(Request.Method.GET, URLHelper.USER_PROFILE_API,
                                    object, response -> {
                                Log.v("GetProfile", response.toString());




                                SharedHelper.putKey(getContext(), "id", response.optString("id"));
                                SharedHelper.putKey(getContext(), "first_name", response.optString("first_name"));
                                SharedHelper.putKey(getContext(), "last_name", response.optString("last_name"));
                                SharedHelper.putKey(getContext(), "email", response.optString("email"));
                                SharedHelper.putKey(getContext(), "picture", URLHelper.BASE + "storage/app/public/" + response.optString("picture"));
                                SharedHelper.putKey(getContext(), "gender", response.optString("gender"));
                                SharedHelper.putKey(getContext(), "sos", response.optString("sos"));
                                SharedHelper.putKey(getContext(), "mobile", response.optString("mobile"));
                                SharedHelper.putKey(getContext(), "refer_code", response.optString("refer_code"));
                                SharedHelper.putKey(getContext(), "wallet_balance", response.optString("wallet_balance"));
                                SharedHelper.putKey(getContext(), "payment_mode", response.optString("payment_mode"));
                                SharedHelper.putKey(getContext(), "currency", response.optString("currency"));


                                //                    SharedHelper.putKey(context, "currency", response.optString("payment_mode"));
                                SharedHelper.putKey(getContext(), "rating", response.optString("rating"));
                                SharedHelper.putKey(getContext(), "status", response.optString("status"));
                                SharedHelper.putKey(getContext(), "ulatitude", response.optString("latitude"));
                                SharedHelper.putKey(getContext(), "ulongitude", response.optString("longitude"));
                                SharedHelper.putKey(getContext(), "udevice_token", response.optString("device_token"));
                                SharedHelper.putKey(getContext(), "bio", response.optString("bio"));


                                SharedHelper.putKey(getContext(), "loggedIn", "true");
                                if (response.optString("avatar").startsWith("http"))
                                    SharedHelper.putKey(getContext(), "picture", response.optString("avatar"));
                                else
                                    SharedHelper.putKey(getContext(), "picture", URLHelper.BASE + "storage/app/public/" + response.optString("avatar"));

                                if (response.optJSONObject("service") != null) {
                                    try {
                                        JSONObject service = response.optJSONObject("service");

                                        SharedHelper.putKey(getContext(), "service_id", service.optString("id"));
                                        SharedHelper.putKey(getContext(), "service_status", service.optString("status"));
                                        SharedHelper.putKey(getContext(), "service_number", service.optString("service_number"));
                                        SharedHelper.putKey(getContext(), "service_model", service.optString("service_model"));
                                        SharedHelper.putKey(getContext(), "service_capacity", service.optString("service_capacity"));
                                        SharedHelper.putKey(getContext(), "service_year", service.optString("service_year"));
                                        SharedHelper.putKey(getContext(), "service_make", service.optString("service_make"));
                                        SharedHelper.putKey(getContext(), "service_name", service.optString("service_name"));
                                        SharedHelper.putKey(getContext(), "service_ac", service.optString("service_ac"));
                                        SharedHelper.putKey(getContext(), "service_color", service.optString("service_color"));


                                        if (service.optJSONObject("service_type") != null) {
                                            JSONObject serviceType = service.optJSONObject("service_type");
                                            SharedHelper.putKey(getContext(), "service", serviceType.optString("name"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }


                            }, error -> {
                                try {
                                    displayMessage(getContext().getString(R.string.something_went_wrong));
                                    error.printStackTrace();
                                    generateNewAccessToken();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() {
                                    HashMap<String, String> headers = new HashMap<String, String>();
                                    headers.put("X-Requested-With", "XMLHttpRequest");
                                    Log.e(TAG, "getHeaders: Token " + SharedHelper.getKey(getContext(), "access_token") + SharedHelper.getKey(getContext(), "token_type"));
                                    headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(getContext(), "access_token"));

                                    return headers;
                                }
                            };

                    ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
                } else {
                    displayMessage(getContext().getString(R.string.something_went_wrong_net));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        System.out.println("GETTING PROFILE ENDED : .... ");

    }


    private void generateNewAccessToken() {
        System.out.println("generating new access token");
        if (isInternet) {
            customDialog = new CustomDialog(getContext());
            customDialog.setCancelable(false);
            if (customDialog != null)
                customDialog.show();
            JSONObject object = new JSONObject();
            try {

                String phoneID = SharedHelper.getKey(getContext(), "mobile_number");
                String phone = phoneID.substring(1, phoneID.length());

                object.put("grant_type", "password");
                object.put("client_id", URLHelper.client_id);
                object.put("client_secret", URLHelper.client_secret);
                object.put("mobile", SharedHelper.getKey(getContext(), "mobile"));
                object.put("password", "12345678");
                object.put("scope", "");
                object.put("device_type", "android");
                object.put("device_id", SharedHelper.getKey(getContext(), "device_udid"));
                object.put("device_token", SharedHelper.getKey(getContext(), "device_token"));
                object.put("logged_in", "1");
                utils.print("InputToLoginAPI", "" + object);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new
                    JsonObjectRequest(Request.Method.POST,
                            URLHelper.login,
                            object,
                            response -> {
                                if ((customDialog != null) && customDialog.isShowing())
                                    customDialog.dismiss();
                                utils.print("LoginResponse", response.toString());
                                SharedHelper.putKey(getContext(),
                                        "access_token", response.optString("access_token"));
                                SharedHelper.putKey(getContext(),
                                        "refresh_token", response.optString("refresh_token"));
                                SharedHelper.putKey(getContext(),
                                        "token_type", response.optString("token_type"));


                                SharedHelper.putKey(getContext(), "loggedIn",
                                        getContext().getString(R.string.True));
//                                GoToMainActivity();


                            },
                            error -> {
                                if ((customDialog != null) && customDialog.isShowing())
                                    customDialog.dismiss();
                                displayMessage(getContext().getString(R.string.something_went_wrong));
                            }) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();

                            headers.put("X-Requested-With", "XMLHttpRequest");
                            return headers;
                        }
                    };

            ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            displayMessage(getContext().getString(R.string.something_went_wrong_net));
        }

    }


    private void getDocList() {

        CustomDialog customDialog = new CustomDialog(getContext());
        customDialog.setCancelable(false);
        customDialog.show();

        JsonArrayRequest jsonArrayRequest = new
                JsonArrayRequest(URLHelper.BASE + "api/provider/document/status",
                        response -> {

                            if (response != null) {
                                if (response.length() == 0) {
                                    SharedHelper.putKey(getContext(), "DocumentStatus", "no");
                                } else {
                                    SharedHelper.putKey(getContext(), "DocumentStatus", "yes");
                                }

                                Log.v("response doc", response + "doc");
                                Log.v("response doc length", String.valueOf(+response.length()));


                            } else {
                                SharedHelper.putKey(getContext(), "DocumentStatus", "no");
                            }

                            customDialog.dismiss();

                        }, error -> {
                    Log.v("DocumentsStatus Error", error.getMessage() + "");
                    customDialog.dismiss();
                    if(getContext() != null){
                        displayMessage(getContext().getString(R.string.something_went_wrong));
                    }

                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(getContext(), "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public void displayMessage(String toastString) {
        Toasty.info(getActivity(), toastString, Toasty.LENGTH_SHORT, true).show();
    }


    public String getMonthName(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";

            case 4:
                return "April";

            case 5:
                return "May";

            case 6:
                return "June";

            case 7:
                return "July";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";

        }
        return "";
    }

    private void initializeViews(View view) {
        frmSource = view.findViewById((R.id.frmSource));
        frmDest = view.findViewById((R.id.frmDest));
        calendertv = view.findViewById((R.id.calendertv));
        persontv = view.findViewById((R.id.persontv));
        btnSearch = view.findViewById((R.id.btnSearch));
        helper = new ConnectionHelper(getContext());
        isInternet = helper.isConnectingToInternet();
        imgMenu = view.findViewById(R.id.imgMenu);
        drawer = view.findViewById(R.id.drawer_layout);
        drawer = getActivity().findViewById(R.id.drawer_layout);
        homeScreenImage  = view.findViewById(R.id.imageView11);


    }



//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        getContext() = context;
//
//        try {
//            listener = (SearchFragment.HomeFragmentListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " must implement HomeFragmentListener");
//        }
//    }


    public interface HomeFragmentListener {
    }
    private void showAddPasengerDialog() {

        Dialog confirmDialog = new Dialog(getContext());
        confirmDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        confirmDialog.setContentView(R.layout.design_passenger_number);


        TextView passengerVal = confirmDialog.findViewById(R.id.passengerVal);
        ImageView removePassenger = confirmDialog.findViewById(R.id.removePassenger);
        ImageView addPassenger = confirmDialog.findViewById(R.id.addPassenger);
        ImageView backArrow = confirmDialog.findViewById(R.id.backArrow);

        FloatingActionButton nextBtn = confirmDialog.findViewById(R.id.nextBtn);

        passangerStr = String.valueOf(passenger_number);

        passengerVal.setText(passangerStr);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });

        removePassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passenger_number == 1) {
                    Toast.makeText(getContext(), "Minimum one passenger", Toast.LENGTH_SHORT).show();
                } else {
                    passenger_number -= 1;
                    passangerStr = String.valueOf(passenger_number);
                }

                passengerVal.setText(passangerStr);

            }
        });

        addPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passenger_number += 1;
                passangerStr = String.valueOf(passenger_number);
                passengerVal.setText(passangerStr);
            }
        });


        confirmDialog.show();
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                persontv.setText("" + passengerVal.getText());
                Toast.makeText(getContext(), "Taken Seat : " + passengerVal.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        try {
            listener = (FSearch.HomeFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement HomeFragmentListener");
        }
    }

    private boolean isToday(Date date) {
        Calendar today = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: " + requestCode + " Result Code " + resultCode);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST) {

            if (resultCode == Activity.RESULT_OK) {


                placePredictions = (PlacePredictions) data.getSerializableExtra("Location Address");
                strPickLocation = data.getExtras().getString("pick_location");
                strPickType = data.getExtras().getString("type");

                System.out.println("intent data  strPickType: " + strPickType);
                System.out.println("intent data  strPickLocation: " + strPickLocation);

//                rootView.findViewById(R.id.mapLayout).setAlpha(1);


//                if (strPickLocation.equalsIgnoreCase("yes")) {
////                    pick_first = true;
////                    mMap.clear();
////                    flowValue = 9;
////                    layoutChanges();
//                    float zoomLevel = 16.0f; //This goes up to 21
////                    stopAnim();
//                } else
//                {
                    if (placePredictions != null) {
                        if(strPickType.equalsIgnoreCase("source")){
                            System.out.println("WASU source  ");
                            if (!placePredictions.strSourceAddress.equalsIgnoreCase("")) {
                                source_lat = "" + placePredictions.strSourceLatitude;
                                source_lng = "" + placePredictions.strSourceLongitude;
                                source_address = placePredictions.strSourceAddress;

                                System.out.println("WASU Source  : "+source_address);
                                frmSource.setText(source_address);

//                            Toast.makeText(getContext(), "SA : " + source_address, Toast.LENGTH_SHORT).show();

//                                if (!placePredictions.strSourceLatitude.equalsIgnoreCase("")
//                                        && !placePredictions.strSourceLongitude.equalsIgnoreCase("")) {
//                                    System.out.println("SOURCE : " + source_address);
//
//                                    double latitude = Double.parseDouble(placePredictions.strSourceLatitude);
//                                    double longitude = Double.parseDouble(placePredictions.strSourceLongitude);
//                                    LatLng location = new LatLng(latitude, longitude);
//
//                                }

                            }else {
                                System.out.println("WASU Source else  ");
                            }

                        }else {
                            System.out.println("WASU Destination  ");
                            if (!placePredictions.strDestAddress.equalsIgnoreCase("")) {
                                dest_lat = "" + placePredictions.strDestLatitude;
                                dest_lng = "" + placePredictions.strDestLongitude;
                                dest_address = placePredictions.strDestAddress;

                                System.out.println("WASU Destination  : "+dest_address);

                                frmDest.setText(dest_address);
//                            dropLocationName = dest_address;

                                System.out.println("SOURCE DROP : " + dest_address);

                                SharedHelper.putKey(getContext(), "current_status", "2");
                                if (source_lat != null && source_lng != null && !source_lng.equalsIgnoreCase("")
                                        && !source_lat.equalsIgnoreCase("")) {
//                                String url = getUrl(Double.parseDouble(source_lat), Double.parseDouble(source_lng)
//                                        , Double.parseDouble(dest_lat), Double.parseDouble(dest_lng));

                                    current_lat = source_lat;
                                    current_lng = source_lng;

                                    LatLng location = new LatLng(Double.parseDouble(current_lat), Double.parseDouble(current_lng));


                                }

                            }else {
                                System.out.println("WASU Destination else  ");
                            }

                        }

//                        if (!placePredictions.strSourceAddress.equalsIgnoreCase("")) {
//                            source_lat = "" + placePredictions.strSourceLatitude;
//                            source_lng = "" + placePredictions.strSourceLongitude;
//                            source_address = placePredictions.strSourceAddress;
//
////                            Toast.makeText(getContext(), "SA : " + source_address, Toast.LENGTH_SHORT).show();
//
//                            if (!placePredictions.strSourceLatitude.equalsIgnoreCase("")
//                                    && !placePredictions.strSourceLongitude.equalsIgnoreCase("")) {
//                                System.out.println("SOURCE : " + source_address);
//
//                                double latitude = Double.parseDouble(placePredictions.strSourceLatitude);
//                                double longitude = Double.parseDouble(placePredictions.strSourceLongitude);
//                                LatLng location = new LatLng(latitude, longitude);
//
//                            }
//
//                        }
//                        if (!placePredictions.strDestAddress.equalsIgnoreCase("")) {
//                            dest_lat = "" + placePredictions.strDestLatitude;
//                            dest_lng = "" + placePredictions.strDestLongitude;
//                            dest_address = placePredictions.strDestAddress;
////                            dropLocationName = dest_address;
//
//                            System.out.println("SOURCE DROP : " + dest_address);
//
//                            SharedHelper.putKey(getContext(), "current_status", "2");
//                            if (source_lat != null && source_lng != null && !source_lng.equalsIgnoreCase("")
//                                    && !source_lat.equalsIgnoreCase("")) {
////                                String url = getUrl(Double.parseDouble(source_lat), Double.parseDouble(source_lng)
////                                        , Double.parseDouble(dest_lat), Double.parseDouble(dest_lng));
//
//                                current_lat = source_lat;
//                                current_lng = source_lng;
//                                //  getNewApproximateFare("1");
//                                //  getNewApproximateFare2("2");
////                                FSearch.FetchUrl fetchUrl = new SearchFragment.FetchUrl();
////                                fetchUrl.execute(url);
//                                LatLng location = new LatLng(Double.parseDouble(current_lat), Double.parseDouble(current_lng));
//
//
//                                //mMap.clear();
////                                if (sourceMarker != null)
////                                    sourceMarker.remove();
////                                MarkerOptions markerOptions = new MarkerOptions()
////                                        .anchor(0.5f, 0.75f)
////                                        .position(location)
////                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));
////                                marker = mMap.addMarker(markerOptions);
////                                sourceMarker = mMap.addMarker(markerOptions);
//                               /* CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(14).build();
//                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
//                            }
//                            if (!dest_lat.equalsIgnoreCase("") && !dest_lng.equalsIgnoreCase("")) {
////                                destLatLng = new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng));
////                                if (destinationMarker != null)
////                                    destinationMarker.remove();
////                                MarkerOptions destMarker = new MarkerOptions()
////                                        .position(destLatLng)
////                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_location));
////                                destinationMarker = mMap.addMarker(destMarker);
////                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
////                                builder.include(sourceMarker.getPosition());
////                                builder.include(destinationMarker.getPosition());
////                                LatLngBounds bounds = builder.build();
////                                int padding = 200; // offset from edges of the map in pixels
////                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
////                                mMap.moveCamera(cu);
//
//                                /*LatLng myLocation = new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng));
//                                CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(16).build();
//                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
//                            }
//                        }

//                        if (dest_address.equalsIgnoreCase("")) {
////                            flowValue = 1;
//                            // commenting below
//                            frmSource.setText(source_address);
//
////                            getValidZone();
////                            getServiceList();
//                        } else {
////                            flowValue = 1;
//
////                            if (cardInfoArrayList.size() > 0) {
////                                getCardDetailsForPayment(cardInfoArrayList.get(0));
////                                sourceDestLayout.setVisibility(View.GONE);
////                            }
////                            getValidZone();
////                            paymentLayout.setVisibility(View.GONE);
////                            getServiceList();
//                        }

//                        if (!dest_address.equalsIgnoreCase("") && !source_address.equalsIgnoreCase("")) {
//                            System.out.println("setting dest and source address");
////                            setValuesForSourceAndDestination();
//                        }

//                        layoutChanges();
                    }
//                }



            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }





    }
}