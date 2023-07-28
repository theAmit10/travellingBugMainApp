package com.travel.travellingbug.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.ui.adapters.DriverProfileViewPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DriverProfileActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;
    DriverProfileViewPagerAdapter profileViewPagerAdapter;

    ImageView profileImgeIv;

    ImageView backArrow;

    TextView userName,ratingVal;

    RatingBar listitemrating;

    String user_id = "";
    String request_id = "";
    private String[] titles = {"ABOUT", "REVIEWS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        initComponent();
        clickHandlerComponent();
        tabLayoutController();

        user_id  = getIntent().getStringExtra("user_id");
        request_id  = getIntent().getStringExtra("request_id");

        if(getIntent().getStringExtra("user_id") != null){
            getProfileData(user_id);
        }

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getProfileData(String user_id) {

        // Getting other details of profile

        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.GET_DETAILS_OF_ONE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("size : "+response.length());
                System.out.println("data : "+response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (response != null) {
                        System.out.println("data : "+jsonObject.toString());


                        userName.setText(jsonObject.optString("first_name"));
                        userName.setVisibility(View.VISIBLE);


                        if(jsonObject.optString("rating") != null){
                            System.out.println("rating : "+jsonObject.optString("rating"));
//                            listitemrating.setRating(Float.parseFloat(jsonObject.optString("rating")));
//                            ratingVal.setText(jsonObject.optString("rating"));

                            if(jsonObject.optString("rating").equalsIgnoreCase("null")){
                                listitemrating.setRating(Float.parseFloat("0"));
                                ratingVal.setText("( 0 Reviews )");
                            }else {

                                String rate_val = jsonObject.optString("rating");
                                listitemrating.setRating(Float.parseFloat(rate_val));
                                ratingVal.setText("( "+jsonObject.optString("noofrating")+" Reviews )");
                            }
                            listitemrating.setVisibility(View.VISIBLE);
                        }






                        Picasso.get().load(URLHelper.BASE + "storage/app/public/" + jsonObject.optString("avatar"))
                                .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(profileImgeIv);


                    }


                } catch (JSONException e) {
                        e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", user_id);
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

    private void tabLayoutController() {
        profileViewPagerAdapter = new DriverProfileViewPagerAdapter(this);
        viewPager.setAdapter(profileViewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> tab.setText(titles[position]))).attach();



        tabLayout.getTabAt(0).setText("About");
        tabLayout.getTabAt(1).setText("Reviews");

    }

    private void clickHandlerComponent() {
    }

    private void initComponent() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        profileImgeIv = findViewById(R.id.profileImgeIv);
        userName = findViewById(R.id.userName);
        ratingVal = findViewById(R.id.ratingVal);
        listitemrating =findViewById(R.id.listitemrating);
        backArrow =findViewById(R.id.backArrow);

    }

}