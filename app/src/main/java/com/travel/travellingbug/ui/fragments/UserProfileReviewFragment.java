package com.travel.travellingbug.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
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


public class UserProfileReviewFragment extends Fragment {

    String user_id = "";

    RecyclerView fragmentDriverReviewRV;
    ArrayList<UserProfileReviewDataModel> list = new ArrayList<>();
    UserProfileReviewDataAdapter adapter;

    ProgressBar fiveStarProgress,fourStarProgress,threeStarProgress,twoStarProgress,oneStarProgress;
    TextView fiveStarTotalVal,fourStarTotalVal,threeStarTotalVal,twoStarTotalVal,oneStarTotalVal;




    LinearLayout reviewDataLl;
    RelativeLayout errorLayout;
    ConstraintLayout bottomContainer;

    private ShimmerFrameLayout mFrameLayout;




    public UserProfileReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile_review, container, false);

        initData(view);
        user_id = getActivity().getIntent().getStringExtra("user_id");
        if (getActivity().getIntent().getStringExtra("user_id") != null) {
            getProfileData(user_id);
        }
        return  view;
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
        System.out.println("User_id PO : "+user_id);
        mFrameLayout.startShimmer();

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
                            if(dataJsonArray.length() > 0){
                                for(int i=0 ; i<dataJsonArray.length(); i++){
                                    JSONObject dataJsonObj = dataJsonArray.getJSONObject(i);

                                    UserProfileReviewDataModel userProfileReviewDataModel = new UserProfileReviewDataModel();
                                    String user_rating =  dataJsonObj.getString("provider_rating");
                                    String user_comment =  dataJsonObj.getString("provider_comment");
                                    String first_name =  dataJsonObj.getString("first_name");
                                    String avatar =  dataJsonObj.getString("avatar");
//                                    String created_at_time =  dataJsonObj.getString("created_at");


                                    String form = dataJsonObj.getString("created_at");
                                    String s_date = getDate(form) + "th " + getMonth(form) + " " + getYear(form);



                                    list.add(new UserProfileReviewDataModel(user_rating,user_comment,first_name,avatar,s_date));


                                }


                                adapter = new UserProfileReviewDataAdapter(getContext(),list);
                                fragmentDriverReviewRV.setAdapter(adapter);
                                mFrameLayout.setVisibility(View.GONE);
                                reviewDataLl.setVisibility(View.VISIBLE);
                            }else {
                                errorLayout.setVisibility(View.VISIBLE);
                                mFrameLayout.setVisibility(View.GONE);

                            }





                        }catch (Exception e){
                            e.printStackTrace();
                            errorLayout.setVisibility(View.VISIBLE);
                            mFrameLayout.setVisibility(View.GONE);
                        }


                    }


                } catch (JSONException e) {
                    errorLayout.setVisibility(View.VISIBLE);
                    mFrameLayout.setVisibility(View.GONE);
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
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getContext(), "access_token"));
                return headers;
            }

        };

        ClassLuxApp.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onPause() {
        mFrameLayout.stopShimmer();
        super.onPause();
    }

    @Override
    public void onResume() {
        mFrameLayout.startShimmer();
        super.onResume();
    }

    private void initData(View view) {


        fiveStarProgress = view.findViewById(R.id.fiveStarProgress);
        fourStarProgress = view.findViewById(R.id.fourStarProgress);
        threeStarProgress = view.findViewById(R.id.threeStarProgress);
        twoStarProgress = view.findViewById(R.id.twoStarProgress);
        oneStarProgress = view.findViewById(R.id.oneStarProgress);

        fiveStarTotalVal = view.findViewById(R.id.fiveStarTotalVal);
        fourStarTotalVal = view.findViewById(R.id.fourStarTotalVal);
        threeStarTotalVal = view.findViewById(R.id.threeStarTotalVal);
        twoStarTotalVal = view.findViewById(R.id.twoStarTotalVal);
        oneStarTotalVal = view.findViewById(R.id.oneStarTotalVal);

        reviewDataLl = view.findViewById(R.id.reviewDataLl);
        errorLayout = view.findViewById(R.id.errorLayout);

        mFrameLayout = view.findViewById(R.id.shimmerLayout);
//        bottomContainer = view.findViewById(R.id.bottomContainer);





        fragmentDriverReviewRV = view.findViewById(R.id.fragmentDriverReviewRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        fragmentDriverReviewRV.setLayoutManager(linearLayoutManager);
        fragmentDriverReviewRV.setNestedScrollingEnabled(false);


    }
}