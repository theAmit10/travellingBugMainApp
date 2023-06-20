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



    ArrayList<TravelPreferenceFindRideDetialsModel> travelPreferenceList;
    ArrayList<FRDModel> frdModelArrayList;

    ImageView backArrow;

    String noofseat = "", request_id = "";

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
    }



    private void getPassengerData() {
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.UPCOMMING_TRIPS_DETAILS_ONE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("size : " + response.length());
                System.out.println("data : " + response);
                String location;

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() > 0) {

                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        JSONArray filtersJsonArray = jsonObject.getJSONArray("filters");

                        location = jsonArray.getJSONObject(0).optString("s_address") + " -> " + jsonArray.getJSONObject(0).optString("d_address");
                        System.out.println("location " + location);

                        if (filtersJsonArray != null && filtersJsonArray.length() > 0) {
                            System.out.println("filter length : " + filtersJsonArray.length());

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
                            passengerRV.setAdapter(passengerDataAdapter);

                        } else {
                            allAvailabelSeatTv.setVisibility(View.VISIBLE);
                            passengerRV.setVisibility(View.GONE);
                        }


                    }
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    JSONObject jsonObject1 = jsonObject.optJSONObject("data");
//                    location = jsonObject1.optString("s_address") +" -> "+ jsonObject1.optString("d_address");
//
//
////                    JSONObject serviceObj = jsonArray.getJSONObject(position).optJSONObject("service_type");
//
//                    JSONArray filterArray = jsonObject1.getJSONArray("filters");
//
//                    System.out.println("filter length : "+filterArray.length());

//                    if(filterArray != null ){
//                        String name;
//
//                        for(int i=0;i<filterArray.length();i++){
//
//                            if(!filterArray.getJSONObject(i).optString("status").equalsIgnoreCase("CANCELLED")){
//                                name = filterArray.getJSONObject(i).optString("first_name");
//                                System.out.println("name opt : "+name);
//                                name = filterArray.getJSONObject(i).getString("first_name");
//                                System.out.println("name get : "+name);
//                                list.add(new PassengerDataModel(name,location));
//                            }
//                        }
//
//                        PassengerDataAdapter passengerDataAdapter = new PassengerDataAdapter(getApplicationContext(), list);
//                        passengerRV.setAdapter(passengerDataAdapter);
//
//                    }


                } catch (JSONException e) {
                    System.out.println("Error : " + e.getMessage());
                }


//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    System.out.println("size : "+jsonArray.length());
//                    System.out.println("data : "+jsonArray);
//                    System.out.println("data : "+jsonArray.getString(0));
//
//
//                    if (response != null) {
//                        System.out.println("data : "+jsonArray.getString(0));
//                        upcomingsAdapter = new FindRidesActivity.UpcomingsAdapter(jsonArray);
//                        //  recyclerView.setHasFixedSize(true);
//                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());
//                        recyclerView.setLayoutManager(mLayoutManager);
//                        recyclerView.setItemAnimator(new DefaultItemAnimator());
//                        if (upcomingsAdapter != null && upcomingsAdapter.getItemCount() > 0) {
//                            recyclerView.setVisibility(View.VISIBLE);
//                            errorLayout.setVisibility(View.GONE);
//                            recyclerView.setAdapter(upcomingsAdapter);
//                        } else {
////                    errorLayout.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.GONE);
//                        }
//
//                    } else {
//                        errorLayout.setVisibility(View.VISIBLE);
//                        recyclerView.setVisibility(View.GONE);
//                    }
//
//                    for(int i=0 ; i<jsonArray.length(); i++){
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        System.out.println("data : "+jsonObject.toString());
//                        System.out.println("data : "+jsonObject.getString("s_address"));
//                    }
//
//                } catch (JSONException e) {
////                    throw new RuntimeException(e);
//                    displayMessage(e.toString());
//                    errorLayout.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.GONE);
//                }

//                Toast.makeText(FindRidesActivity.this, "Data Found succesfully..", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FindRideDetails.this, "Error Found", Toast.LENGTH_SHORT).show();
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

        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();
//            list = new ArrayList<>();

        stopOverModelArrayList = new ArrayList<>();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLHelper.PREFERENCES_TITLE,
                jsonArray, response -> {
            Log.v("GetPreferencesTitle", response.toString());

            if (response.length() > 0) {

                SharedHelper.putKey(getApplicationContext(),"TravelPreferenceStatus", String.valueOf(response.length()));

                for (int i = 0; i < response.length(); i++) {
                    try {

                        Log.v("jsonObjectLength : ", String.valueOf(response.length()));
                        Log.v("jsonObjectLength : ", String.valueOf(jsonArray.length()));

                        JSONObject jsonObjectPreference = response.getJSONObject(i);
                        Log.v("jsonObjectPreference : ", jsonObjectPreference.toString());
                        Log.v("jsonObjectPreference : ", jsonObjectPreference.optString("title"));

//                            if(!jsonObjectPreference.optString("title").equalsIgnoreCase("null") || !jsonObjectPreference.optString("title").equalsIgnoreCase("") || !jsonObjectPreference.optString("title").equalsIgnoreCase(null)){
//                                titles = jsonObjectPreference.optString("title");
//                            }

//                            list.add(new FRDModel(jsonObjectPreference.optString("title")));

                        stopOverModelArrayList.add(new StopOverModel(jsonObjectPreference.optString("title")));


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

//                    VerifyIdMainActivityAdapter verifyIdMainActivityAdapter = new VerifyIdMainActivityAdapter(getApplicationContext(), list);
//                    FRDAdapter frdAdapter = new FRDAdapter(getContext(), list);
//                    recyclerViewPreference.setAdapter(frdAdapter);







                stepOverAdapter = new StepOverAdapter(getContext(), stopOverModelArrayList);
                recyclerViewPreference.setHasFixedSize(true);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//                recyclerViewPreference.setLayoutManager(linearLayoutManager);
                recyclerViewPreference.setLayoutManager(gridLayoutManager);
                recyclerViewPreference.setAdapter(stepOverAdapter);
                recyclerViewPreference.setNestedScrollingEnabled(false);



            }

        }, error -> {
//                displayMessage(getString(R.string.something_went_wrong));
            Toast.makeText(this, "Error:", Toast.LENGTH_SHORT).show();
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
//        travelPreferencesFRDRV = findViewById(R.id.travelPreferencesFRDRV);
//        recyclerViewPreference = findViewById(R.id.recyclerViewPreference);
        allAvailabelSeatTv = findViewById(R.id.allAvailabelSeatTv);
        backArrow = findViewById(R.id.backArrow);

        helper = new ConnectionHelper(getApplicationContext());
        isInternet = helper.isConnectingToInternet();
    }


}