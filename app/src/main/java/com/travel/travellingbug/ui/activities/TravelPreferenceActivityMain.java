package com.travel.travellingbug.ui.activities;

import static com.travel.travellingbug.ClassLuxApp.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.VerifyIdMainActivityModel;
import com.travel.travellingbug.ui.adapters.VerifyIdMainActivityAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class TravelPreferenceActivityMain extends AppCompatActivity {

    RecyclerView travelPreferenceRV;
    ArrayList<VerifyIdMainActivityModel> list;

    GoogleApiClient mGoogleApiClient;

    CustomDialog customDialog;

    private static final String TAG = "TRAVEL PREFERENCE";
    private static final int SELECT_PHOTO = 100;
    public static int deviceHeight;
    public static int deviceWidth;

    //    CustomDialog customDialog;
    ConnectionHelper helper;
    Boolean isInternet;

    String titles = "";
    String subTitle = "";
    String id = "";

    ImageView backArrow;

    Button addPreferences;
    LinearLayout errorLayout;
    RelativeLayout travelPreferenceRVLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_preference_main);

        initComponenet();
        getProfile();
        settingAdapterForRecycleView();
    }

    private void settingAdapterForRecycleView() {


        // adding data to the list

//        list = new ArrayList<>();
//
//        list.add(new VerifyIdMainActivityModel("Chattiness","I'm Chatty when i feel confortable "));
//        list.add(new VerifyIdMainActivityModel("Music","I'll jam dependent on mood"));
//        list.add(new VerifyIdMainActivityModel("Smoking","Cigarette break outside the car are ok."));
//        list.add(new VerifyIdMainActivityModel("Pet's","I'll travel with pets depending on the animal"));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        VerifyIdMainActivityAdapter verifyIdMainActivityAdapter = new VerifyIdMainActivityAdapter(getApplicationContext(), list);
        travelPreferenceRV.setLayoutManager(linearLayoutManager);
        travelPreferenceRV.setAdapter(verifyIdMainActivityAdapter);
        travelPreferenceRV.setNestedScrollingEnabled(false);


    }

    private void initComponenet() {
        travelPreferenceRV = findViewById(R.id.travelPreferenceRV);

        helper = new ConnectionHelper(getApplicationContext());
        isInternet = helper.isConnectingToInternet();

        backArrow = findViewById(R.id.backArrow);
        errorLayout = findViewById(R.id.errorLayout);
        addPreferences = findViewById(R.id.addPreferences);
        travelPreferenceRVLayout = findViewById(R.id.travelPreferenceRVLayout);

        addPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UpdatePreference.class);
                intent.putExtra("id", "0");
                intent.putExtra("title", "Title");
                intent.putExtra("subtitle", "Subtitle");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void displayMessage(String toastString) {
        Toasty.info(getApplicationContext(), toastString, Toast.LENGTH_SHORT, true).show();
    }

    public void getProfile() {
        customDialog = new CustomDialog(TravelPreferenceActivityMain.this);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();

        if (isInternet) {
            JSONObject object = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            list = new ArrayList<>();


            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLHelper.PREFERENCES,
                    jsonArray, response -> {
                Log.v("GetPreferences", response.toString());

                if (response.length() > 0) {
                    errorLayout.setVisibility(View.GONE);
                    travelPreferenceRVLayout.setVisibility(View.VISIBLE);
                    SharedHelper.putKey(getApplicationContext(),"TravelPreferenceStatus", String.valueOf(response.length()));

                    for (int i = 0; i < response.length(); i++) {
                        try {

                            Log.v("jsonObjectLength : ", String.valueOf(response.length()));
                            Log.v("jsonObjectLength : ", String.valueOf(jsonArray.length()));

                            JSONObject jsonObjectPreference = response.getJSONObject(i);
                            Log.v("jsonObjectPreference : ", jsonObjectPreference.toString());
                            Log.v("jsonObjectPreference : ", jsonObjectPreference.optString("title"));
                            Log.v("jsonObjectPreference : ", jsonObjectPreference.optString("subtitle"));


//                        if(!jsonObjectPreference.optString("title").equalsIgnoreCase("null") || !jsonObjectPreference.optString("title").equalsIgnoreCase("") || !jsonObjectPreference.optString("title").equalsIgnoreCase(null)){
//                            titles = jsonObjectPreference.optString("title");
//                        }
//
//                        if(!jsonObjectPreference.optString("subtitle").equalsIgnoreCase("null") || !jsonObjectPreference.optString("subtitle").equalsIgnoreCase("") || !jsonObjectPreference.optString("subtitle").equalsIgnoreCase(null)){
//                            subTitle = jsonObjectPreference.optString("subtitle");
//                        }


                            titles = jsonObjectPreference.optString("title");
                            subTitle = jsonObjectPreference.optString("subtitle");
                            id = jsonObjectPreference.optString("id");


                            list.add(new VerifyIdMainActivityModel(titles, subTitle, id));


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    VerifyIdMainActivityAdapter verifyIdMainActivityAdapter = new VerifyIdMainActivityAdapter(getApplicationContext(), list);
                    if (customDialog != null)
                        customDialog.dismiss();
                    travelPreferenceRV.setAdapter(verifyIdMainActivityAdapter);

                } else {
                    travelPreferenceRVLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);

                }


            }, error -> {
                displayMessage(getString(R.string.something_went_wrong));
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    Log.e(TAG, "getHeaders: Token" + SharedHelper.getKey(getApplicationContext(), "access_token") + SharedHelper.getKey(getContext(), "token_type"));
                    headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                    return headers;
                }
            };

            ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);


        } else {
            displayMessage(getString(R.string.something_went_wrong_net));
        }

    }

}