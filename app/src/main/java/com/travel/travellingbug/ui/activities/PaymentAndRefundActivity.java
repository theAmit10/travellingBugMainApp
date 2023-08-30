package com.travel.travellingbug.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.PaymentDataModel;
import com.travel.travellingbug.ui.adapters.PaymentDataAdapter;

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

public class PaymentAndRefundActivity extends AppCompatActivity {

    ImageView backArrow;
    CustomDialog customDialog;
    RelativeLayout errorLayout;

    ArrayList<PaymentDataModel> list = new ArrayList<>();
    ArrayList<PaymentDataModel> providerList = new ArrayList<>();

    PaymentDataAdapter paymentDataAdapter;

    RecyclerView paymentRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_and_refund);

        initData();
        getPaymentHistoryList();

//        PaymentDataAdapter paymentDataAdapter = new PaymentDataAdapter(getApplicationContext(), list);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
//        paymentRv.setAdapter(paymentDataAdapter);
//        paymentRv.setLayoutManager(linearLayoutManager);
//        paymentRv.setNestedScrollingEnabled(false);
        clickHandler();
    }

    private String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("M").format(cal.getTime());
        String name = getMonthName(Integer.parseInt(monthName));


        return name;
    }

    public String getMonthName(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";

            case 4:
                return "April";

            case 5:
                return "May";

            case 6:
                return "June";

            case 7:
                return "July";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";

        }
        return "";
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


    public void getPaymentHistoryList() {

        customDialog = new CustomDialog(this);
        customDialog.setCancelable(false);
        customDialog.show();


        System.out.println("GETPAYMENTHISTORY : -> ");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLHelper.USER_PAYMENT_HISTORY, response -> {

            if (response != null) {
                if(response.length() > 0){
                    System.out.println("Payment data : "+response.toString());
                    System.out.println("Payment data USER length : "+response.length());

                    for(int i=0 ; i<response.length(); i++){
                        try {
                            PaymentDataModel paymentDataModel = new PaymentDataModel();
                            JSONObject jsonObject = response.getJSONObject(i);
                            JSONObject tripJsonObj  = jsonObject.optJSONObject("trip_detail");

                            if(tripJsonObj.optString("estimated_fare") != null){
                                paymentDataModel.setFare(tripJsonObj.optString("estimated_fare"));
                            }else {
                                paymentDataModel.setFare("0");
                            }

                            paymentDataModel.setType("Paid for a Ride");


                            try {
                                String form = tripJsonObj.optString("schedule_at");
                                paymentDataModel.setTime(getDate(form) + "th " + getMonth(form) + " " + "at " + getTime(form));
                            } catch (ParseException e) {
                                paymentDataModel.setTime(tripJsonObj.optString("schedule_at"));
                                e.printStackTrace();
                            }


                            JSONObject providerJsonObj = tripJsonObj.optJSONObject("provider");

                            if(providerJsonObj != null){
                                paymentDataModel.setUsername(providerJsonObj.optString("first_name"));
                                String image = URLHelper.BASE + "storage/app/public/" + providerJsonObj.optString("avatar");
                                paymentDataModel.setProfileImage(image);
                                paymentDataModel.setUserid(providerJsonObj.optString("id"));
                            }



                            StringRequest request = new StringRequest(Request.Method.GET, URLHelper.ESTIMATED_FARE_AND_DISTANCE + "?s_latitude=" + tripJsonObj.optString("s_latitude") + "&s_longitude=" + tripJsonObj.optString("s_longitude") + "&d_latitude=" + tripJsonObj.optString("d_latitude") + "&d_longitude=" + tripJsonObj.optString("d_longitude") + "&service_type=2", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {


                                    try {
                                        JSONObject jsonObjectFare = new JSONObject(response);

                                        if (response != null) {
                                            System.out.println("payment details estimated data : " + jsonObjectFare.toString());
                                            jsonObjectFare.optString("estimated_fare");
                                            jsonObjectFare.optString("distance");
                                            jsonObjectFare.optString("time");
                                            jsonObjectFare.optString("tax_price");
                                            jsonObjectFare.optString("base_price");
                                            jsonObjectFare.optString("discount");
                                            jsonObjectFare.optString("currency");

                                            String con = jsonObjectFare.optString("currency") + " ";


                                            System.out.println("ESTIMATED FARE STATUS :" + response.toString());

                                            try {
                                                System.out.println("Fare : "+con + jsonObjectFare.optString("estimated_fare"));

                                                Double fares = Double.valueOf(jsonObjectFare.optString("estimated_fare"));
                                                int no_of_seat = Integer.parseInt(jsonObject.optString("noofseats"));
                                                Double c_fare = fares * no_of_seat;
                                                String calculated_fare = con + c_fare;


                                                paymentDataModel.setFare(calculated_fare);
                                                paymentDataAdapter.notifyDataSetChanged();



                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }







                                        }

                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    try {
                                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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






                            list.add(paymentDataModel);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    if(list.size() > 0){
                        paymentDataAdapter = new PaymentDataAdapter(getApplicationContext(), list);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        paymentRv.setAdapter(paymentDataAdapter);
                        paymentRv.setLayoutManager(linearLayoutManager);
                        paymentRv.setNestedScrollingEnabled(false);
                    }else {
                        paymentRv.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                    }





                }else {
                    paymentRv.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                }

            } else {
                paymentRv.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }

            customDialog.dismiss();

        }, error -> {
            customDialog.dismiss();
            paymentRv.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                return headers;
            }
        };

        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);


//        #######################################################


        // for provider payment history
        JsonArrayRequest jsonArrayRequestProvider = new JsonArrayRequest(URLHelper.PROVIDER_PAYMENT_HISTORY, response -> {

            if (response != null) {
                if (response.length() > 0) {
                    System.out.println("Payment data PROVIDER length : " + response.length());

                    for (int i = 0; i < response.length(); i++) {

                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            JSONObject tripJsonObj = jsonObject.optJSONObject("trip_detail");


                            if (tripJsonObj.getJSONArray("filters").length() > 0 ) {

                                JSONArray filterArray = tripJsonObj.getJSONArray("filters");
                                if (filterArray.length() > 0) {
                                    for (int j = 0; j < filterArray.length(); j++) {

                                        PaymentDataModel paymentDataModel = new PaymentDataModel();
                                        JSONObject filterJsonObj = filterArray.getJSONObject(j);

                                        if (filterJsonObj.optString("payment_status").equalsIgnoreCase("success")) {



                                            // Getting User details
                                            System.out.println("GETTING USER PROFILE ID - >  "+filterJsonObj.optString("user_id"));
                                            StringRequest request = new StringRequest(Request.Method.POST, URLHelper.GET_DETAILS_OF_ONE_USER, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                    try {
                                                        JSONObject jsonObjectUser = new JSONObject(response);

                                                        if (response != null) {

                                                            if (tripJsonObj.optString("estimated_fare") != null) {
                                                                paymentDataModel.setFare(tripJsonObj.optString("estimated_fare"));
                                                            } else {
                                                                paymentDataModel.setFare("0");
                                                            }

                                                            paymentDataModel.setType("Ride Payment");


                                                            try {
                                                                String form = tripJsonObj.optString("schedule_at");
                                                                paymentDataModel.setTime(getDate(form) + "th " + getMonth(form) + " " + "at " + getTime(form));
                                                            } catch (ParseException e) {
                                                                paymentDataModel.setTime(tripJsonObj.optString("schedule_at"));
                                                                e.printStackTrace();
                                                            }

                                                            paymentDataModel.setUsername(jsonObjectUser.optString("first_name"));
                                                            String image = URLHelper.BASE + "storage/app/public/" + jsonObjectUser.optString("avatar");
                                                            paymentDataModel.setProfileImage(image);
                                                            paymentDataModel.setUserid(jsonObjectUser.optString("id"));




                                                            StringRequest request = new StringRequest(Request.Method.GET, URLHelper.ESTIMATED_FARE_AND_DISTANCE + "?s_latitude=" + tripJsonObj.optString("s_latitude") + "&s_longitude=" + tripJsonObj.optString("s_longitude") + "&d_latitude=" + tripJsonObj.optString("d_latitude") + "&d_longitude=" + tripJsonObj.optString("d_longitude") + "&service_type=2", new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {


                                                                    try {
                                                                        JSONObject jsonObjectFare = new JSONObject(response);

                                                                        if (response != null) {
                                                                            System.out.println("payment details estimated data : " + jsonObjectFare.toString());
                                                                            jsonObjectFare.optString("estimated_fare");
                                                                            jsonObjectFare.optString("distance");
                                                                            jsonObjectFare.optString("time");
                                                                            jsonObjectFare.optString("tax_price");
                                                                            jsonObjectFare.optString("base_price");
                                                                            jsonObjectFare.optString("discount");
                                                                            jsonObjectFare.optString("currency");

                                                                            String con = jsonObjectFare.optString("currency") + " ";


                                                                            System.out.println("ESTIMATED FARE STATUS :" + response.toString());

                                                                            try {
                                                                                System.out.println("Fare : "+con + jsonObjectFare.optString("estimated_fare"));

                                                                                Double fares = Double.valueOf(jsonObjectFare.optString("estimated_fare"));
                                                                                int no_of_seat = Integer.parseInt(filterJsonObj.optString("noofseats"));
                                                                                Double c_fare = fares * no_of_seat;
                                                                                String calculated_fare = con + c_fare;


                                                                                paymentDataModel.setFare(calculated_fare);
                                                                                paymentDataAdapter.notifyDataSetChanged();



                                                                            }catch (Exception e){
                                                                                e.printStackTrace();
                                                                            }







                                                                        }

                                                                    } catch (JSONException e) {

                                                                        e.printStackTrace();
                                                                    }


                                                                }
                                                            }, new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {

                                                                    try {
                                                                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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








                                                            list.add(paymentDataModel);
                                                            paymentDataAdapter.notifyDataSetChanged();

                                                        }


                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    error.printStackTrace();
                                                }

                                            }) {


                                                @Override
                                                public Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("id", filterJsonObj.optString("user_id"));
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

                                    }

                                    paymentDataAdapter = new PaymentDataAdapter(getApplicationContext(), list);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                                    paymentRv.setAdapter(paymentDataAdapter);
                                    paymentRv.setLayoutManager(linearLayoutManager);
                                    paymentRv.setNestedScrollingEnabled(false);
                                    System.out.println("LENGTH AFTER ADDING BOTH DATA : -> : " + list.size());

                                    if(list.size() == 0){
                                        paymentRv.setVisibility(View.GONE);
                                        errorLayout.setVisibility(View.VISIBLE);
                                    }

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
            if (customDialog.isShowing()) {
                customDialog.dismiss();
            }
        }, error -> {
            if (customDialog.isShowing()) {
                customDialog.dismiss();
            }
            paymentRv.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                return headers;
            }
        };

        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequestProvider);


    }


    private void clickHandler() {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        backArrow = findViewById(R.id.backArrow);
        paymentRv = findViewById(R.id.paymentRv);
        errorLayout = findViewById(R.id.errorLayout);

        paymentRv = findViewById(R.id.paymentRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        paymentRv.setLayoutManager(linearLayoutManager);
        paymentRv.setNestedScrollingEnabled(false);

    }
}