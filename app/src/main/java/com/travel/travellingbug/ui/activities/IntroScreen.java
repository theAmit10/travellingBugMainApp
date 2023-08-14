package com.travel.travellingbug.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.IntroModel;
import com.travel.travellingbug.ui.activities.login.SignUp;
import com.travel.travellingbug.ui.adapters.IntroAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

public class IntroScreen extends AppCompatActivity {

    ViewPager my_pager;
    TabLayout my_tablayout;
    ArrayList<IntroModel> introModelArrayList;
    FloatingActionButton floatingActionButton;
    TextView skipTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);

        initComponent();
        componentClickHandler();
        introViewPagerListner();
    }

    private void introViewPagerListner() {
        introModelArrayList = new ArrayList<>();
        CustomDialog customDialog = new CustomDialog(IntroScreen.this);
        customDialog.show();
        customDialog.setCancelable(false);

        // getting intro list

        try {
            StringRequest request = new StringRequest(Request.Method.GET, URLHelper.INTRO_SCREEN_API , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        if (response != null) {
                            System.out.println("WOrking on intro api data");
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length() > 0){
                                for(int i=0 ; i<jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    IntroModel introModel = new IntroModel();

                                    if(jsonObject.optString("title") != null && !jsonObject.optString("title").equalsIgnoreCase("null"))
                                    {
                                        introModel.setThe_caption_Title(jsonObject.optString("title"));
                                    }else{
                                        introModel.setThe_caption_Title("");
                                    }

                                    if(jsonObject.optString("subtitle") != null && !jsonObject.optString("subtitle").equalsIgnoreCase("null"))
                                    {
                                        introModel.setThe_caption_Title_description(jsonObject.optString("subtitle"));
                                    }else{
                                        introModel.setThe_caption_Title_description("");
                                    }

                                    if(jsonObject.optString("picture") != null && !jsonObject.optString("picture").equalsIgnoreCase("null"))
                                    {
                                        introModel.setFeatured_image(jsonObject.optString("picture"));
                                    }else {
                                        introModel.setFeatured_image("");
                                    }


                                    introModelArrayList.add(introModel);

                                }



                                IntroAdapter itemsPager_adapter = new IntroAdapter(getApplicationContext(), (ArrayList<IntroModel>) introModelArrayList);
                                customDialog.dismiss();
                                my_pager.setAdapter(itemsPager_adapter);

                                // The_slide_timer
                                java.util.Timer timer = new java.util.Timer();
                                timer.scheduleAtFixedRate(new The_slide_timer(), 2000, 3000);
                                my_tablayout.setupWithViewPager(my_pager, true);
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        customDialog.dismiss();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customDialog.dismiss();
                    try {
                        introModelArrayList.add(new IntroModel(R.drawable.intro_one, "Select Location", "We move much too fast, and too frequently, to pause to savor landscapes or avoid disfiguring clutter."));
                        introModelArrayList.add(new IntroModel(R.drawable.intro_two, "Choose Your Ride", "We move much too fast, and too frequently"));
                        introModelArrayList.add(new IntroModel(R.drawable.intro_three, "Enjoy Your Ride", "\"What a ride!\" is usually said after something very exciting happens. It is a very casual and light-hearted expression"));

                        IntroAdapter itemsPager_adapter = new IntroAdapter(getApplicationContext(), (ArrayList<IntroModel>) introModelArrayList);
                        my_pager.setAdapter(itemsPager_adapter);

                        // The_slide_timer
                        java.util.Timer timer = new java.util.Timer();
                        timer.scheduleAtFixedRate(new The_slide_timer(), 2000, 3000);
                        my_tablayout.setupWithViewPager(my_pager, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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



        }catch (Exception e){
            e.printStackTrace();
        }







//        introModelArrayList.add(new IntroModel(R.drawable.intro_one, "Select Location", "We move much too fast, and too frequently, to pause to savor landscapes or avoid disfiguring clutter."));
//        introModelArrayList.add(new IntroModel(R.drawable.intro_two, "Choose Your Ride", "We move much too fast, and too frequently"));
//        introModelArrayList.add(new IntroModel(R.drawable.intro_three, "Enjoy Your Ride", "\"What a ride!\" is usually said after something very exciting happens. It is a very casual and light-hearted expression"));
//
//        IntroAdapter itemsPager_adapter = new IntroAdapter(getApplicationContext(), (ArrayList<IntroModel>) introModelArrayList);
//        my_pager.setAdapter(itemsPager_adapter);
//
//        // The_slide_timer
//        java.util.Timer timer = new java.util.Timer();
//        timer.scheduleAtFixedRate(new The_slide_timer(), 2000, 3000);
//        my_tablayout.setupWithViewPager(my_pager, true);

    }

    public class The_slide_timer extends TimerTask {
        @Override
        public void run() {


            IntroScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (my_pager.getCurrentItem() < introModelArrayList.size() - 1) {
                        my_pager.setCurrentItem(my_pager.getCurrentItem() + 1);
                    } else
                        my_pager.setCurrentItem(0);
                }
            });


        }
    }

    private void componentClickHandler() {
        skipTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedHelper.putKey(getApplicationContext(),"selectedlanguage","true");
                Intent intent =  new Intent(IntroScreen.this, SignUp.class);
                startActivity(intent);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedHelper.putKey(getApplicationContext(),"selectedlanguage","true");
                Intent intent =  new Intent(IntroScreen.this, SignUp.class);
                startActivity(intent);
            }
        });

    }

    private void initComponent() {
        my_pager = findViewById(R.id.my_pager);
        my_tablayout = findViewById(R.id.my_tablayout);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        skipTv = findViewById(R.id.skipTv);
    }
}