package com.travel.travellingbug.ui.activities;

import android.os.Bundle;
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
import com.travel.travellingbug.models.PassengerDataModel;
import com.travel.travellingbug.ui.adapters.PassengerDataAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FindRideDetails extends AppCompatActivity {

    RecyclerView passengerRV;
    ArrayList<PassengerDataModel> list;

    String noofseat="",request_id="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ride_details);
         initData();
        getIntentData();
        getPassengerData();
        setDataRecyclerView();

    }

    private void getIntentData() {
        noofseat = getIntent().getStringExtra("noofseat");
        request_id = getIntent().getStringExtra("request_id");
    }

    private void getPassengerData() {
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.BOOK_FOR_UPCOMMING_TRIPS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("size : "+response.length());
                System.out.println("data : "+response);
                String location;

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                    location = jsonObject1.optString("s_address") +" -> "+ jsonObject1.optString("d_address");


//                    JSONObject serviceObj = jsonArray.getJSONObject(position).optJSONObject("service_type");

                    JSONArray filterArray = jsonObject1.getJSONArray("filters");

                    System.out.println("filter length : "+filterArray.length());

                    if(filterArray != null ){
                        String name;

                        for(int i=0;i<filterArray.length();i++){
                            name = filterArray.getJSONObject(i).optString("first_name");
                            System.out.println("name: "+name);
                            name = filterArray.getJSONObject(i).getString("first_name");
                            System.out.println("name: "+name);

                            list.add(new PassengerDataModel(name,location));
                        }

                        PassengerDataAdapter passengerDataAdapter = new PassengerDataAdapter(getApplicationContext(), list);
                        passengerRV.setAdapter(passengerDataAdapter);

                    }




                } catch (JSONException e) {
                    System.out.println("Error : "+e.getMessage());
                }


//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    System.out.println("size : "+jsonArray.length());
//                    System.out.println("data : "+jsonArray);
//                    System.out.println("data : "+jsonArray.getString(0));
//
//
//                    if (response != null) {
//                        System.out.println("data : "+jsonArray.getString(0));
//                        upcomingsAdapter = new FindRidesActivity.UpcomingsAdapter(jsonArray);
//                        //  recyclerView.setHasFixedSize(true);
//                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());
//                        recyclerView.setLayoutManager(mLayoutManager);
//                        recyclerView.setItemAnimator(new DefaultItemAnimator());
//                        if (upcomingsAdapter != null && upcomingsAdapter.getItemCount() > 0) {
//                            recyclerView.setVisibility(View.VISIBLE);
//                            errorLayout.setVisibility(View.GONE);
//                            recyclerView.setAdapter(upcomingsAdapter);
//                        } else {
////                    errorLayout.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.GONE);
//                        }
//
//                    } else {
//                        errorLayout.setVisibility(View.VISIBLE);
//                        recyclerView.setVisibility(View.GONE);
//                    }
//
//                    for(int i=0 ; i<jsonArray.length(); i++){
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        System.out.println("data : "+jsonObject.toString());
//                        System.out.println("data : "+jsonObject.getString("s_address"));
//                    }
//
//                } catch (JSONException e) {
////                    throw new RuntimeException(e);
//                    displayMessage(e.toString());
//                    errorLayout.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.GONE);
//                }

//                Toast.makeText(FindRidesActivity.this, "Data Found succesfully..", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FindRideDetails.this, "Error Found", Toast.LENGTH_SHORT).show();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("request_id",request_id);
                params.put("noofseat", noofseat);
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

    private void setDataRecyclerView() {

        list = new ArrayList<>();

//        list.add(new PassengerDataModel("Goku","Delhi -> Goa"));
//        list.add(new PassengerDataModel("Goku","Delhi -> Goa"));
//        list.add(new PassengerDataModel("Goku","Delhi -> Goa"));
//        list.add(new PassengerDataModel("Goku","Delhi -> Goa"));
//        list.add(new PassengerDataModel("Goku","Delhi -> Goa"));
//        list.add(new PassengerDataModel("Goku","Delhi -> Goa"));
//        list.add(new PassengerDataModel("Goku","Delhi -> Goa"));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        passengerRV.setNestedScrollingEnabled(false);
        passengerRV.setLayoutManager(linearLayoutManager);
        PassengerDataAdapter passengerDataAdapter = new PassengerDataAdapter(getApplicationContext(), list);
        passengerRV.setAdapter(passengerDataAdapter);

    }

    private void initData() {
        passengerRV = findViewById(R.id.passengerRV);
    }


}