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
import com.travel.travellingbug.ui.adapters.UserProfileViewPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UserProfileActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;

    ImageView profileImgeIv;

    ImageView backArrow;

    TextView userName,ratingVal;

    RatingBar listitemrating;

    String user_id = "";
    String first_name = "",rating = "",rating_val = "",profile_image = "";
    UserProfileViewPagerAdapter profileViewPagerAdapter;
    private String[] titles = {"ABOUT", "REVIEWS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initComponent();
        getIntentData();

        clickHandlerComponent();
        tabLayoutController();



    }

    private void getIntentData() {


        user_id  = getIntent().getStringExtra("user_id");
        first_name  = getIntent().getStringExtra("first_name");
        rating  = getIntent().getStringExtra("rating");
        rating_val  = getIntent().getStringExtra("rating_val");
        profile_image  = getIntent().getStringExtra("profile_image");

        System.out.println("intent data : "+user_id+" , "+first_name+" , "+rating+" , "+rating_val+" , "+profile_image+" , ");



        try {
            ratingVal.setText(rating_val);
            listitemrating.setRating(Float.parseFloat(rating));
            Picasso.get().load(profile_image)
                    .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(profileImgeIv);

            userName.setText(first_name);
        }catch (Exception e){
            e.printStackTrace();
        }


//        if(getIntent().getStringExtra("user_id") != null){
//            getProfileData(user_id);
//        }
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
                       ratingVal.setText(jsonObject.optString("rating"));
                       listitemrating.setRating(Float.parseFloat(jsonObject.optString("rating")));
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
                Toast.makeText(getApplicationContext(), "Error Found", Toast.LENGTH_SHORT).show();
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
        profileViewPagerAdapter = new UserProfileViewPagerAdapter(this);
        viewPager.setAdapter(profileViewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> tab.setText(titles[position]))).attach();

        tabLayout.getTabAt(0).setText("About");
        tabLayout.getTabAt(1).setText("Reviews");

    }

    private void clickHandlerComponent() {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initComponent() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        profileImgeIv = findViewById(R.id.profileImgeIv);
        userName = findViewById(R.id.userName);
        ratingVal = findViewById(R.id.ratingVal);
        listitemrating = findViewById(R.id.listitemrating);
        backArrow = findViewById(R.id.backArrow);


    }
}