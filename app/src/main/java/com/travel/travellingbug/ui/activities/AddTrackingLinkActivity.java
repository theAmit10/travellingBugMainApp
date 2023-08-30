package com.travel.travellingbug.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddTrackingLinkActivity extends AppCompatActivity {

    ImageView backArrow;
    TextView generateTrackingLinkTv;
    Button btnSubmit,btnShareLink;

    CustomDialog customDialog;

    Boolean isInternet;
    ConnectionHelper helper;

    String latitude="";
    String longitude="";
    String rideid="";
    String provider_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tracking_link);

        initData();
        clickHandler();
        getIntentData();
        checkTrackingLinkAvailableOrNot();
//        generateTrackingLink();


    }

    private void getIntentData() {
        try {
            latitude = getIntent().getStringExtra("latitude");
            longitude = getIntent().getStringExtra("longitude");
            rideid = getIntent().getStringExtra("rideid");
            provider_id = getIntent().getStringExtra("provider_id");

            if(getIntent().getStringExtra("rideid") == null){
                btnSubmit.setVisibility(View.GONE);
                btnShareLink.setVisibility(View.VISIBLE);
            }else {
                btnShareLink.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void checkTrackingLinkAvailableOrNot() {
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.UPCOMMING_TRIPS_DETAILS_ONE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Single trip details : " + response);
                String location;

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        if(jsonArray.getJSONObject(0).optString("track_lat").equalsIgnoreCase("null") && jsonArray.getJSONObject(0).optString("track_long").equalsIgnoreCase("null") ){
                            btnSubmit.setText("Add Tracking Link");
                            btnSubmit.setClickable(true);
                        }else {
                            btnSubmit.setText("Activated");
                            btnSubmit.setClickable(false);
                        }




                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(AddTrackingLinkActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("request_id", rideid);
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

    private void generateTrackingLink() {
        customDialog = new CustomDialog(this);
        customDialog.setCancelable(false);
        customDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.PUBLISH_TRACKING_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response != null){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.optJSONObject(0);
                        if (jsonObject != null) {
                            System.out.println("Added Track Data : " + jsonObject.toString());
                            btnSubmit.setText("Activated");
                            btnSubmit.setClickable(false);
                        }

                        customDialog.dismiss();

                    } catch (JSONException e) {
                        customDialog.dismiss();

                        btnSubmit.setText("Submit");
                        e.printStackTrace();

                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customDialog.dismiss();
                btnSubmit.setText("Submit");
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("rideid", rideid);
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

    private void clickHandler() {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        generateTrackingLinkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AddTrackingLinkActivity.this, TrackActivityDriver.class);
////                shareUrl+ current_lat + "," + current_lng
//                intent.putExtra("address",URLHelper.REDIRECT_SHARE_URL +latitude+","+longitude);
//                startActivity(intent);
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    generateTrackingLink();
//                    btnSubmit.setBackgroundResource(R.drawable.auth_btn_gray_bg);
            }
        });

        btnShareLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String shareUrl = URLHelper.TRACKING_LINK_SHARE_URL;

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String name = SharedHelper.getKey(getApplicationContext(), "first_name");
                    String text = "TravellingBug App,\n" + name + "\nwould like to share a trip with you at \n" + shareUrl + provider_id;
                    intent.putExtra(Intent.EXTRA_SUBJECT, "TravellingBug App");
                    intent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(intent, "Share Via"));


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Share applications not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initData() {
        backArrow = findViewById(R.id.backArrow);
        generateTrackingLinkTv = findViewById(R.id.generateTrackingLinkTv);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnShareLink = findViewById(R.id.btnShareLink);


        helper = new ConnectionHelper(getApplicationContext());
        isInternet = helper.isConnectingToInternet();
    }
}