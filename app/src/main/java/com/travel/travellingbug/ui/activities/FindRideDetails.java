package com.travel.travellingbug.ui.activities;

import static com.travel.travellingbug.ClassLuxApp.getContext;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.FRDModel;
import com.travel.travellingbug.models.PassengerDataModel;
import com.travel.travellingbug.models.StopOverModel;
import com.travel.travellingbug.models.TravelPreferenceFindRideDetialsModel;
import com.travel.travellingbug.models.VerifyIdMainActivityModel;
import com.travel.travellingbug.ui.adapters.PassengerDataAdapter;
import com.travel.travellingbug.ui.adapters.StepOverAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class FindRideDetails extends AppCompatActivity {

    RecyclerView passengerRV;

    RecyclerView recyclerViewPreference;
    ArrayList<PassengerDataModel> list;

    ArrayList<VerifyIdMainActivityModel> driverPreferenceList;



    ArrayList<TravelPreferenceFindRideDetialsModel> travelPreferenceList;
    ArrayList<FRDModel> frdModelArrayList;

    ImageView backArrow;

    String noofseat = "", request_id = "",user_id= "";

    TextView allAvailabelSeatTv;

    ConnectionHelper helper;
    Boolean isInternet;

    CustomDialog customDialog;

    String titles = "";
    String subTitle = "";
    String id = "";

    String title_id = "";

    ArrayList<FRDModel> listPreference;


    StepOverAdapter stepOverAdapter;

    private ShimmerFrameLayout mFrameLayout;
    private ShimmerFrameLayout mFrameLayout2;
    ArrayList<StopOverModel> stopOverModelArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ride_details);
        initData();
        getIntentData();

        frdModelArrayList = new ArrayList<>();


        getPassengerData();
        setDataRecyclerView();

        recyclerViewPreference = findViewById(R.id.recyclerViewPreference);
        getPreferencesTitle();


//        getPreferencesTitle();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent = new Intent(getContext(), TestingActivity.class);
//                startActivity(intent);
            }
        });

    }

    private void getIntentData() {
        noofseat = getIntent().getStringExtra("noofseat");
        request_id = getIntent().getStringExtra("request_id");
        user_id = getIntent().getStringExtra("user_id");
    }



    private void getPassengerData() {
        mFrameLayout.startShimmer();
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.UPCOMMING_TRIPS_DETAILS_ONE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String location;

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() > 0) {

                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        JSONArray filtersJsonArray = jsonObject.getJSONArray("filters");

                        location = jsonArray.getJSONObject(0).optString("s_address") + " --> " + jsonArray.getJSONObject(0).optString("d_address");

                        if (filtersJsonArray != null && filtersJsonArray.length() > 0) {

                            for (int i = 0; i < filtersJsonArray.length(); i++) {
                                String name;
                                if (!filtersJsonArray.getJSONObject(i).optString("status").equalsIgnoreCase("CANCELLED")) {
                                    name = filtersJsonArray.getJSONObject(i).optString("first_name");
                                    System.out.println("name opt : " + name);
                                    name = filtersJsonArray.getJSONObject(i).getString("first_name");
                                    System.out.println("name get : " + name);
                                    list.add(new PassengerDataModel(name, location));
                                }
                            }

                            PassengerDataAdapter passengerDataAdapter = new PassengerDataAdapter(getApplicationContext(), list);
                            mFrameLayout.setVisibility(View.GONE);
                            passengerRV.setVisibility(View.VISIBLE);
                            passengerRV.setAdapter(passengerDataAdapter);

                        } else {
                            allAvailabelSeatTv.setVisibility(View.VISIBLE);
                            passengerRV.setVisibility(View.GONE);
                            mFrameLayout.setVisibility(View.GONE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FindRideDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("request_id", request_id);
                return params;
            }

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

//    public void getPreferencesTitle() {
//        customDialog = new CustomDialog(FindRideDetails.this);
//        customDialog.setCancelable(false);
//        if (customDialog != null)
//            customDialog.show();
//
//        if (isInternet) {
//            JSONObject object = new JSONObject();
//            JSONArray jsonArray = new JSONArray();
//            travelPreferenceList = new ArrayList<>();
//
//
//            @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLHelper.PREFERENCES_TITLE,
//                    jsonArray, response -> {
//                Log.v("GetPreferences", response.toString());
//
//                if (response.length() > 0) {
////                    errorLayout.setVisibility(View.GONE);
//                    travelPreferencesFRDRV.setVisibility(View.VISIBLE);
//                    SharedHelper.putKey(getApplicationContext(), "TravelPreferenceStatus", String.valueOf(response.length()));
//
//                    for (int i = 0; i < response.length(); i++) {
//                        try {
//
//                            Log.v("jsonObjectLength : ", String.valueOf(response.length()));
//                            Log.v("jsonObjectLength : ", String.valueOf(jsonArray.length()));
//
//                            JSONObject jsonObjectPreference = response.getJSONObject(i);
//                            Log.v("jsonObjectPreference : ", jsonObjectPreference.toString());
//                            Log.v("jsonObjectPreference : ", jsonObjectPreference.optString("title"));
////                            Log.v("jsonObjectPreference : ", jsonObjectPreference.optString("subtitle"));
//
//
//                            if (!jsonObjectPreference.optString("title").equalsIgnoreCase("null") || !jsonObjectPreference.optString("title").equalsIgnoreCase("") || !jsonObjectPreference.optString("title").equalsIgnoreCase(null)) {
//                                titles = jsonObjectPreference.optString("title");
//                            }
//
////                            if(!jsonObjectPreference.optString("subtitle").equalsIgnoreCase("null") || !jsonObjectPreference.optString("subtitle").equalsIgnoreCase("") || !jsonObjectPreference.optString("subtitle").equalsIgnoreCase(null)){
////                                subTitle = jsonObjectPreference.optString("subtitle");
////                            }
//
//
//                            titles = jsonObjectPreference.optString("title");
////                            subTitle = jsonObjectPreference.optString("subtitle");
////                            subTitle = "Add Value";
////                            id = jsonObjectPreference.optString("title_id");
////                            title_id = jsonObjectPreference.optString("title_id");
//
//
//                            frdModelArrayList.add(new FRDModel(titles));
//
//
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//
////                    TravelPreferenceFindRideDetialsAdapter travelPreferenceFindRideDetialsAdapter = new TravelPreferenceFindRideDetialsAdapter(getApplicationContext(), travelPreferenceList);
//                    if (customDialog != null)
//                        customDialog.dismiss();
//
////                    System.out.println("PREFERENCES SIZE : " + travelPreferenceList.size());
////                    for (int i = 0; i < travelPreferenceList.size(); i++) {
////                        System.out.println("PREFERENCES  : " + travelPreferenceList.get(i).getTitle());
////                    }
////                    travelPreferenceRV.setAdapter(travelPreferenceFindRideDetialsAdapter);
//                    LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//                    FRDAdapter frdAdapter = new FRDAdapter(getContext(), frdModelArrayList);
//                    travelPreferencesFRDRV.setLayoutManager(linearLayoutManager2);
//                    travelPreferencesFRDRV.setAdapter(frdAdapter);
//                    travelPreferencesFRDRV.setNestedScrollingEnabled(false);
////                    travelPreferenceFindRideDetialsAdapter.notifyDataSetChanged();
//
//                } else {
//                    if (customDialog != null)
//                        customDialog.dismiss();
////                    travelPreferenceRV.setVisibility(View.GONE);
//                    Toast.makeText(this, "NO ITEM", Toast.LENGTH_SHORT).show();
////                    errorLayout.setVisibility(View.VISIBLE);
//
//                }
//
//
//            }, error -> {
//                displayMessage(getString(R.string.something_went_wrong));
//            }) {
//                @Override
//                public Map<String, String> getHeaders() {
//                    HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("X-Requested-With", "XMLHttpRequest");
//                    Log.e(TAG, "getHeaders: Token" + SharedHelper.getKey(getApplicationContext(), "access_token") + SharedHelper.getKey(getContext(), "token_type"));
//                    headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(getApplicationContext(), "access_token"));
//                    return headers;
//                }
//            };
//
//            ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);
//
//
//        } else {
//            displayMessage(getString(R.string.something_went_wrong_net));
//        }
//
//    }

    public void getPreferencesTitle() {
        customDialog = new CustomDialog(this);
        if(!customDialog.isShowing()){
            customDialog.show();
        }

        mFrameLayout2.startShimmer();

        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();
//            list = new ArrayList<>();

        stopOverModelArrayList = new ArrayList<>();
        driverPreferenceList = new ArrayList<>();



        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLHelper.PREFERENCES_TITLE,
                jsonArray, response -> {
            Log.v("GetPreferencesTitle", response.toString());

            if (response.length() > 0) {



//                for (int i = 0; i < response.length(); i++) {
//                    try {
//
//                        Log.v("jsonObjectLength : ", String.valueOf(response.length()));
//                        Log.v("jsonObjectLength : ", String.valueOf(jsonArray.length()));
//
//                        JSONObject jsonObjectPreference = response.getJSONObject(i);
//                        Log.v("jsonObjectPreference : ", jsonObjectPreference.toString());
//                        Log.v("jsonObjectPreference : ", jsonObjectPreference.optString("title"));
//
//
//                        stopOverModelArrayList.add(new StopOverModel(jsonObjectPreference.optString("title")));
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }


                for (int i = 0; i < response.length(); i++) {
                    try {

                        Log.v("jsonObjectLength : ", String.valueOf(response.length()));
                        Log.v("jsonObjectLength : ", String.valueOf(jsonArray.length()));

                        JSONObject jsonObjectPreference = response.getJSONObject(i);
                        Log.v("jsonObjectPreference : ", jsonObjectPreference.toString());
                        Log.v("jsonObjectPreference : ", jsonObjectPreference.optString("title"));
//                            Log.v("jsonObjectPreference : ", jsonObjectPreference.optString("subtitle"));

                        StopOverModel verifyIdMainActivityModel = new StopOverModel();


                        if (!jsonObjectPreference.optString("title").equalsIgnoreCase("null") || !jsonObjectPreference.optString("title").equalsIgnoreCase("") || !jsonObjectPreference.optString("title").equalsIgnoreCase(null)) {
                            titles = jsonObjectPreference.optString("title");
                            verifyIdMainActivityModel.setTitle(jsonObjectPreference.optString("title"));
                        }

                        if (!jsonObjectPreference.optString("title_id").equalsIgnoreCase("null") || !jsonObjectPreference.optString("title_id").equalsIgnoreCase("") || !jsonObjectPreference.optString("title_id").equalsIgnoreCase(null)) {
                            subTitle = jsonObjectPreference.optString("title_id");
                        }

                        if (jsonObjectPreference.optString("picture") != null && !jsonObjectPreference.optString("picture").equalsIgnoreCase("null")) {
                            verifyIdMainActivityModel.setAllowed(jsonObjectPreference.optString("picture"));
                        } else {
                            verifyIdMainActivityModel.setAllowed("");
                        }

                        if (jsonObjectPreference.optString("picture") != null && !jsonObjectPreference.optString("picture").equalsIgnoreCase("null")) {
                            verifyIdMainActivityModel.setAllowed(jsonObjectPreference.optString("picture"));
                        } else {
                            verifyIdMainActivityModel.setAllowed("");
                        }

                        if (jsonObjectPreference.optString("picture1") != null && !jsonObjectPreference.optString("picture1").equalsIgnoreCase("null")) {
                            verifyIdMainActivityModel.setNotAllowed(jsonObjectPreference.optString("picture1"));
                        } else {
                            verifyIdMainActivityModel.setNotAllowed("");
                        }


                        verifyIdMainActivityModel.setId(jsonObjectPreference.optString("title_id"));
                        verifyIdMainActivityModel.setTitle_id(jsonObjectPreference.optString("title_id"));
                        verifyIdMainActivityModel.setDescription(jsonObjectPreference.optString("subtitle"));


//                            list.add(new VerifyIdMainActivityModel(titles, subTitle, id, title_id));
                        stopOverModelArrayList.add(verifyIdMainActivityModel);


                    } catch (JSONException e) {
                        customDialog.dismiss();
                        e.printStackTrace();
                    }
                }



                customDialog.dismiss();
                StepOverAdapter stepOverAdapter = new StepOverAdapter(getContext(), stopOverModelArrayList,user_id);
                recyclerViewPreference.setHasFixedSize(true);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                gridLayoutManager.setOrientation(RecyclerView.VERTICAL);

                recyclerViewPreference.setLayoutManager(gridLayoutManager);
                mFrameLayout2.setVisibility(View.GONE);
                recyclerViewPreference.setVisibility(View.VISIBLE);
                recyclerViewPreference.setAdapter(stepOverAdapter);
                recyclerViewPreference.setNestedScrollingEnabled(false);



            }

        }, error -> {
//                displayMessage(getString(R.string.something_went_wrong));
            customDialog.dismiss();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                Log.e(DocUploadActivity.TAG, "getHeaders: Token" + SharedHelper.getKey(getApplicationContext(), "access_token") + SharedHelper.getKey(getContext(), "token_type"));
                headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                return headers;
            }
        };

        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);




    }

    public void displayMessage(String toastString) {
        Toasty.info(getApplicationContext(), toastString, Toast.LENGTH_SHORT, true).show();
    }

    private void setDataRecyclerView() {

        list = new ArrayList<>();


        // FOR PASSENGER
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        passengerRV.setNestedScrollingEnabled(false);
        passengerRV.setLayoutManager(linearLayoutManager);
        PassengerDataAdapter passengerDataAdapter = new PassengerDataAdapter(getApplicationContext(), list);
        passengerRV.setAdapter(passengerDataAdapter);


        //  FOR TRAVEL PREFERENCES

//        frdModelArrayList = new ArrayList<>();
//
//        frdModelArrayList.add(new FRDModel("MUSIC"));
//        frdModelArrayList.add(new FRDModel("Smoking"));
//        frdModelArrayList.add(new FRDModel("Driking"));
//        frdModelArrayList.add(new FRDModel("Dancing"));
//
//        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
//        travelPreferencesFRDRV.setNestedScrollingEnabled(false);
//        travelPreferencesFRDRV.setLayoutManager(linearLayoutManager2);
//        FRDAdapter frdAdapter = new FRDAdapter(getApplicationContext(), frdModelArrayList);
//        travelPreferencesFRDRV.setAdapter(frdAdapter);

    }

    private void initData() {
        passengerRV = findViewById(R.id.passengerRV);
        mFrameLayout = findViewById(R.id.shimmerLayout);
        mFrameLayout2 = findViewById(R.id.shimmerLayout2);

//        travelPreferencesFRDRV = findViewById(R.id.travelPreferencesFRDRV);
//        recyclerViewPreference = findViewById(R.id.recyclerViewPreference);
        allAvailabelSeatTv = findViewById(R.id.allAvailabelSeatTv);
        backArrow = findViewById(R.id.backArrow);

        helper = new ConnectionHelper(getApplicationContext());
        isInternet = helper.isConnectingToInternet();
    }


}