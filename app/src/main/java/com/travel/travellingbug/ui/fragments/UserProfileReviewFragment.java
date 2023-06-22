package com.travel.travellingbug.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UserProfileReviewFragment extends Fragment {

    String user_id = "";

    ProgressBar fiveStarProgress,fourStarProgress,threeStarProgress,twoStarProgress,oneStarProgress;
    TextView fiveStarTotalVal,fourStarTotalVal,threeStarTotalVal,twoStarTotalVal,oneStarTotalVal;

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



                        }catch (Exception e){
                            e.printStackTrace();
                        }




//                        if(jsonObject.optString("email").equalsIgnoreCase("")){
//                            emailTv.setText("No email Available");
//                        }else {
//                            emailTv.setText(jsonObject.optString("email"));
//                        }
//
//                        if(jsonObject.optString("mobile") != null){
//                            phoneTv.setText(jsonObject.optString("mobile"));
//                        }

//                        userName.setText(jsonObject.optString("first_name"));
//                        ratingVal.setText(jsonObject.optString("rating"));
//                        listitemrating.setRating(Float.parseFloat(jsonObject.optString("rating")));
//                        Picasso.get().load(URLHelper.BASE + "storage/app/public/" + jsonObject.optString("avatar"))
//                                .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(profileImgeIv);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "Error Found", Toast.LENGTH_SHORT).show();
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

    }
}