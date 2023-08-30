package com.travel.travellingbug.ui.activities;

import static com.travel.travellingbug.ClassLuxApp.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.HelpModel;
import com.travel.travellingbug.ui.adapters.HelpAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class HelpActivity extends AppCompatActivity {

    ImageView backArrow;
    CardView tripsAndFareCV,paymentCV,appUsabilityCV,accountCV;
    CustomDialog customDialog;
    ConnectionHelper helper;
    Boolean isInternet;
    RecyclerView helpRV;
    ArrayList<HelpModel> list = new ArrayList<>();
    RelativeLayout helpRVLayout;

    String TAG  = "HELP_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        initingView();

        getHelpDetails();

        onClick();

        settingAdapterForRecycleView();


    }

    private void onClick() {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        accountCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HelpActivity.this, "Account Help", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HelpActivity.this, ComplaintsAcivity.class);
                intent.putExtra("title","Account Help");
                startActivity(intent);
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            }
        });


        appUsabilityCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HelpActivity.this, "App Usability Help", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HelpActivity.this, ComplaintsAcivity.class);
                intent.putExtra("title","App Usability Help");
                startActivity(intent);
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            }
        });

        paymentCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HelpActivity.this, "Payment Help", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HelpActivity.this, ComplaintsAcivity.class);
                intent.putExtra("title","Payment Help");
                startActivity(intent);
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            }
        });

        tripsAndFareCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HelpActivity.this, "Account Help", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HelpActivity.this, ComplaintsAcivity.class);
                intent.putExtra("title","Account Help");
                startActivity(intent);
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            }
        });

    }

    private void settingAdapterForRecycleView() {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);

        HelpAdapter helpAdapter = new HelpAdapter(getApplicationContext(), list);
        helpRV.setLayoutManager(linearLayoutManager);
        helpRV.setAdapter(helpAdapter);
        helpRV.setNestedScrollingEnabled(false);


    }

    public void getHelpDetails() {
        customDialog = new CustomDialog(HelpActivity.this);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();
        //        mFrameLayout.startShimmer();
//        customDialog = new CustomDialog(getActivity());
//        customDialog.setCancelable(false);
//        customDialog.show();

        if (isInternet) {


            // Getting Provider Chatlist
            StringRequest request = new StringRequest(Request.Method.GET, URLHelper.HELP_TITLE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        System.out.println("HELP : "+response.length());
                        System.out.println("HELP : "+response.toString());
                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    Log.v("jsonObjectLength : ", String.valueOf(jsonArray.length()));
                                    Log.v("jsonObjectLength : ", String.valueOf(jsonArray.length()));

                                    JSONObject jsonObjectHelp = jsonArray.getJSONObject(i);
                                    Log.v("jsonObjectHelp : ", jsonObjectHelp.toString());
                                    Log.v("jsonObjectHelp : ", jsonObjectHelp.optString("title"));

                                    HelpModel helpModel = new HelpModel();


                                    if (!jsonObjectHelp.optString("title").equalsIgnoreCase("null") && !jsonObjectHelp.optString("title").equalsIgnoreCase(null)) {
                                        helpModel.setTitle(jsonObjectHelp.optString("title"));
                                    }

                                    if (!jsonObjectHelp.optString("id").equalsIgnoreCase("null") &&  !jsonObjectHelp.optString("id").equalsIgnoreCase(null)) {
                                        helpModel.setId(jsonObjectHelp.optString("id"));
                                    }

                                    if ( !jsonObjectHelp.optString("subtitle").equalsIgnoreCase("null")) {
                                        helpModel.setDescription(jsonObjectHelp.optString("subtitle"));
                                    } else {
                                        helpModel.setDescription("");
                                    }



                                    list.add(helpModel);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    if (customDialog != null)
                                        customDialog.dismiss();
                                }
                            }


                            HelpAdapter helpAdapter = new HelpAdapter(getApplicationContext(), list);
                            if (customDialog != null)
                                customDialog.dismiss();
                            helpRV.setAdapter(helpAdapter);

                        } else {
                            if (customDialog != null)
                                customDialog.dismiss();


                        }

                    } catch (JSONException e) {
                        if(customDialog.isShowing()){
                            customDialog.dismiss();
                        }
                        displayMessage("Something went wrong");
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }

            }) {



                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "Bearer " + SharedHelper.getKey(getContext(), "access_token"));
                    return headers;
                }

            };

            ClassLuxApp.getInstance().addToRequestQueue(request);


        } else {
            displayMessage(getString(R.string.something_went_wrong_net));
        }







//        JSONArray jsonArray = new JSONArray();
////            list = new ArrayList<>();
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLHelper.HELP_TITLE,
//                jsonArray, response -> {
//
//
//            System.out.println("HELP length() : "+response.length());
//            System.out.println("HELP : "+response.toString());
//            if (response.length() > 0) {
//
//
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//
//                        Log.v("jsonObjectLength : ", String.valueOf(response.length()));
//                        Log.v("jsonObjectLength : ", String.valueOf(jsonArray.length()));
//
//                        JSONObject jsonObjectHelp = response.getJSONObject(i);
//                        Log.v("jsonObjectHelp : ", jsonObjectHelp.toString());
//                        Log.v("jsonObjectHelp : ", jsonObjectHelp.optString("title"));
//
//                        HelpModel helpModel = new HelpModel();
//
//
//                        if (!jsonObjectHelp.optString("title").equalsIgnoreCase("null") && !jsonObjectHelp.optString("title").equalsIgnoreCase(null)) {
//                            helpModel.setTitle(jsonObjectHelp.optString("title"));
//                        }
//
//                        if (!jsonObjectHelp.optString("id").equalsIgnoreCase("null") &&  !jsonObjectHelp.optString("id").equalsIgnoreCase(null)) {
//                            helpModel.setId(jsonObjectHelp.optString("id"));
//                        }
//
//                        if ( !jsonObjectHelp.optString("subtitle").equalsIgnoreCase("null")) {
//                            helpModel.setDescription(jsonObjectHelp.optString("subtitle"));
//                        } else {
//                            helpModel.setDescription("");
//                        }
//
//
//
//                        list.add(helpModel);
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//                HelpAdapter helpAdapter = new HelpAdapter(getApplicationContext(), list);
//                if (customDialog != null)
//                    customDialog.dismiss();
//                helpRV.setAdapter(helpAdapter);
//
//            } else {
//                if (customDialog != null)
//                    customDialog.dismiss();
//
//
//            }
//
//
//        }, error -> {
//            error.printStackTrace();
//            displayMessage(getString(R.string.something_went_wrong));
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("X-Requested-With", "XMLHttpRequest");
//                Log.e(TAG, "getHeaders: Token" + SharedHelper.getKey(getApplicationContext(), "access_token") + SharedHelper.getKey(getContext(), "token_type"));
//                headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(getApplicationContext(), "access_token"));
//                return headers;
//            }
//        };
//
//        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);






    }

    public void displayMessage(String toastString) {
        Toasty.info(getApplicationContext(), toastString, Toast.LENGTH_SHORT, true).show();
    }


    private void initingView() {
        backArrow = findViewById(R.id.backArrow);
        accountCV = findViewById(R.id.accountCV);
        appUsabilityCV = findViewById(R.id.appUsabilityCV);
        paymentCV = findViewById(R.id.paymentCV);
        tripsAndFareCV = findViewById(R.id.tripsAndFareCV);

        helpRV = findViewById(R.id.helpRV);
        helper = new ConnectionHelper(getApplicationContext());
        isInternet = helper.isConnectingToInternet();
        helpRVLayout = findViewById(R.id.helpRVLayout);
    }
}