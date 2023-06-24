package com.travel.travellingbug.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.UserProfileReviewDataModel;
import com.travel.travellingbug.ui.adapters.UserProfileReviewDataAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProviderReviewActivity extends AppCompatActivity {

    String user_id = "";

    LinearLayout  reviewDataLl;
    RelativeLayout errorLayout;

    ImageView backArrow;
    RecyclerView fragmentDriverReviewRV;
    ArrayList<UserProfileReviewDataModel> list = new ArrayList<>();
    UserProfileReviewDataAdapter adapter;
    ProgressBar fiveStarProgress, fourStarProgress, threeStarProgress, twoStarProgress, oneStarProgress;
    TextView fiveStarTotalVal, fourStarTotalVal, threeStarTotalVal, twoStarTotalVal, oneStarTotalVal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_review);


        initData();
        user_id = SharedHelper.getKey(getApplicationContext(), "id");

        getProfileData(user_id);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
        return monthName;
    }

    private String getDate(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String dateName = new SimpleDateFormat("dd").format(cal.getTime());
        return dateName;
    }

    private String getYear(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String yearName = new SimpleDateFormat("yyyy").format(cal.getTime());
        return yearName;
    }

    private String getTime(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String timeName = new SimpleDateFormat("hh:mm a").format(cal.getTime());
        return timeName;
    }

    private void getProfileData(String user_id) {

        // Getting other details of profile

        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.USER_REVIEW_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("size : " + response.length());
                System.out.println("data : " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (response != null) {
                        System.out.println("data : " + jsonObject.toString());


                        try {
                            fiveStarTotalVal.setText(jsonObject.optString("five_star_count"));
                            fourStarTotalVal.setText(jsonObject.optString("four_star_count"));
                            threeStarTotalVal.setText(jsonObject.optString("three_star_count"));
                            twoStarTotalVal.setText(jsonObject.optString("two_star_count"));
                            oneStarTotalVal.setText(jsonObject.optString("one_star_count"));

                            fiveStarProgress.setProgress(Integer.parseInt(String.valueOf(fiveStarTotalVal.getText())));
                            fourStarProgress.setProgress(Integer.parseInt(String.valueOf(fourStarTotalVal.getText())));
                            threeStarProgress.setProgress(Integer.parseInt(String.valueOf(threeStarTotalVal.getText())));
                            twoStarProgress.setProgress(Integer.parseInt(String.valueOf(twoStarTotalVal.getText())));
                            oneStarProgress.setProgress(Integer.parseInt(String.valueOf(oneStarTotalVal.getText())));


                            JSONArray dataJsonArray = jsonObject.getJSONArray("Data");
                            if (dataJsonArray.length() > 0) {
                                for (int i = 0; i < dataJsonArray.length(); i++) {
                                    JSONObject dataJsonObj = dataJsonArray.getJSONObject(i);

                                    UserProfileReviewDataModel userProfileReviewDataModel = new UserProfileReviewDataModel();
                                    String user_rating = dataJsonObj.getString("user_rating");
                                    String user_comment = dataJsonObj.getString("user_comment");
                                    String first_name = dataJsonObj.getString("first_name");
                                    String avatar = dataJsonObj.getString("avatar");
//                                    String created_at_time =  dataJsonObj.getString("created_at");


                                    String form = dataJsonObj.getString("created_at");
                                    String s_date = getDate(form) + "th " + getMonth(form) + " " + getYear(form);


                                    list.add(new UserProfileReviewDataModel(user_rating, user_comment, first_name, avatar, s_date));


                                }
                                reviewDataLl.setVisibility(View.VISIBLE);
                                adapter = new UserProfileReviewDataAdapter(getApplicationContext(), list);
                                fragmentDriverReviewRV.setAdapter(adapter);
                            }else {
                                errorLayout.setVisibility(View.VISIBLE);
                            }


                        } catch (Exception e) {
                            errorLayout.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }


                    }


                } catch (JSONException e) {
                    errorLayout.setVisibility(View.VISIBLE);
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
                params.put("user_id", user_id);
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

    private void initData() {


        errorLayout = findViewById(R.id.errorLayout);
        reviewDataLl = findViewById(R.id.reviewDataLl);

        backArrow = findViewById(R.id.backArrow);


        fiveStarProgress = findViewById(R.id.fiveStarProgress);
        fourStarProgress = findViewById(R.id.fourStarProgress);
        threeStarProgress = findViewById(R.id.threeStarProgress);
        twoStarProgress = findViewById(R.id.twoStarProgress);
        oneStarProgress = findViewById(R.id.oneStarProgress);

        fiveStarTotalVal = findViewById(R.id.fiveStarTotalVal);
        fourStarTotalVal = findViewById(R.id.fourStarTotalVal);
        threeStarTotalVal = findViewById(R.id.threeStarTotalVal);
        twoStarTotalVal = findViewById(R.id.twoStarTotalVal);
        oneStarTotalVal = findViewById(R.id.oneStarTotalVal);

        fragmentDriverReviewRV = findViewById(R.id.fragmentDriverReviewRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        fragmentDriverReviewRV.setLayoutManager(linearLayoutManager);
        fragmentDriverReviewRV.setNestedScrollingEnabled(false);


    }
}