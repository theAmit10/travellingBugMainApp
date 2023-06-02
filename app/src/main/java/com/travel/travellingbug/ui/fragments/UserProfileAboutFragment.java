package com.travel.travellingbug.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class UserProfileAboutFragment extends Fragment {


    TextView phoneTv, emailTv;

    String user_id = "";

    public UserProfileAboutFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_user_profile_about, container, false);



        initData(view);
        user_id  = getActivity().getIntent().getStringExtra("user_id");
        if(getActivity().getIntent().getStringExtra("user_id") != null){
            getProfileData(user_id);
        }



        return view;
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


//                        userName.setText(jsonObject.optString("first_name"));
//                        ratingVal.setText(jsonObject.optString("rating"));
//                        listitemrating.setRating(Float.parseFloat(jsonObject.optString("rating")));
//                        Picasso.get().load(URLHelper.BASE + "storage/app/public/" + jsonObject.optString("avatar"))
//                                .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(profileImgeIv);


                    }


                } catch (JSONException e) {

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
                params.put("id", user_id);
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
        phoneTv = view.findViewById(R.id.phoneTv);
        emailTv = view.findViewById(R.id.emailTv);
    }
}