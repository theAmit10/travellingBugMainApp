package com.travel.travellingbug.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.InvoiceModel;
import com.travel.travellingbug.ui.adapters.TrackingLinkUsetActivityAdapter;

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

import es.dmoral.toasty.Toasty;

public class TrackLinkUserActivity extends AppCompatActivity {

    ArrayList<InvoiceModel> list = new ArrayList<>();

    Boolean isInternet;
    RecyclerView recyclerView;
    RelativeLayout errorLayout;
    ConnectionHelper helper;
    CustomDialog customDialog;
    View rootView;

    String userId = "", userName = "", rating = "", ratingVal = "", vehicleDetails = "", userProfileImage = "";

    String noofseat = "", request_id = "", s_address = "", d_address = "", s_date = "", s_time = "";

    String booking_id = "", status = "", payment_mode = "", estimated_fare = "", verification_code = "", static_map = "", first_name = "", mobile = "", avatar = "";
    ImageView backImg;
    LinearLayout toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_link_user);



        findViewByIdAndInitialize();

        if (isInternet) {
            getHistoryList();
        }


        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    public void findViewByIdAndInitialize() {
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        errorLayout = findViewById(R.id.errorLayout);
        errorLayout.setVisibility(View.GONE);
        helper = new ConnectionHelper(this);
        isInternet = helper.isConnectingToInternet();
        toolbar = findViewById(R.id.lnrTitle);
        backImg = findViewById(R.id.backArrow);
    }


    public void getHistoryList() {

        customDialog = new CustomDialog(TrackLinkUserActivity.this);
        customDialog.setCancelable(false);
        customDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, URLHelper.GET_ALL_RIDES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                customDialog.dismiss();
                customDialog.cancel();

                System.out.println("size : " + response.length());
                System.out.println("data : " + response);

                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            System.out.println("size : " + jsonArray.length());
                            System.out.println("data : " + jsonArray);
                            System.out.println("data : " + jsonArray.getString(0));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.optJSONObject(i);


                                if(jsonObject.optJSONObject("trip").optString("status").equalsIgnoreCase("STARTED") || jsonObject.optJSONObject("trip").optString("provider_status").equalsIgnoreCase("STARTED")){

                                    InvoiceModel invoiceModel = new InvoiceModel();


                                    JSONObject jsonObjectTrip = jsonObject.optJSONObject("trip");

                                    invoiceModel.setFromAddress(jsonObjectTrip.optString("s_address"));
                                    invoiceModel.setDestAddress(jsonObjectTrip.optString("d_address"));
                                    invoiceModel.setSlat(jsonObjectTrip.optString("s_latitude"));
                                    invoiceModel.setSlong(jsonObjectTrip.optString("s_longitude"));
                                    invoiceModel.setDlat(jsonObjectTrip.optString("d_latitude"));
                                    invoiceModel.setDlong(jsonObjectTrip.optString("d_longitude"));
                                    invoiceModel.setBookingId(jsonObjectTrip.optString("booking_id"));

                                    JSONObject providerJsonObject = jsonObjectTrip.getJSONObject("provider");
                                    try {
                                        invoiceModel.setUsername(providerJsonObject.optString("first_name"));

                                        invoiceModel.setRatingVal("( " + providerJsonObject.optString("noofrating") + " Reviews )");

                                        if(providerJsonObject.optString("noofrating").equalsIgnoreCase("0")){
                                            invoiceModel.setRating("0");
                                        }else {
                                            invoiceModel.setRating(providerJsonObject.optString("rating"));
                                        }

                                        invoiceModel.setImage(URLHelper.BASE + "storage/app/public/" + jsonObjectTrip.optJSONObject("provider").optString("avatar"));

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    if (!jsonObjectTrip.optString("schedule_at", "").isEmpty()) {
                                        String form = jsonObjectTrip.optString("schedule_at");
                                        try {

                                            invoiceModel.setTime(getDate(form) + "th " + getMonth(form) + " at " + getTime(form));
//                        holder.availableSeat.setText(jsonObjectTrip.optString("availablecapacity")+" Seat left");



                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                    // getting taken seat
                                    JSONArray filterJsonArray = jsonObjectTrip.optJSONArray("filters");
                                    if (filterJsonArray != null && filterJsonArray.length() > 0) {
                                        for (int j = 0; j < filterJsonArray.length(); j++) {
                                            JSONObject filterJsonObject = filterJsonArray.optJSONObject(j);
                                            System.out.println("j" + filterJsonObject.optString("user_id"));
                                            System.out.println("j" + SharedHelper.getKey(getApplicationContext(), "id"));
                                            if (filterJsonObject.optString("user_id").equalsIgnoreCase(SharedHelper.getKey(getApplicationContext(), "id"))) {

                                                invoiceModel.setSeat(filterJsonObject.optString("noofseats")+" Seat");
                                            }
                                        }
                                    }


                                    JSONObject jsonObjectServiceType = jsonObjectTrip.optJSONObject("service_type");

                                    invoiceModel.setFare("₹ "+jsonObjectServiceType.optString("fixed"));


                                    JSONObject providerServiceJsonObj = jsonObjectTrip.optJSONObject("provider_service");
                                    try {
                                        if(providerServiceJsonObj != null){
                                            if(!providerServiceJsonObj.optString("service_model").equalsIgnoreCase("null") && !providerServiceJsonObj.optString("service_name").equalsIgnoreCase("null") && !providerServiceJsonObj.optString("service_color").equalsIgnoreCase("null") ){
                                                String vehicle_name = providerServiceJsonObj.optString("service_model")+ " " + providerServiceJsonObj.optString("service_name") +" | "+providerServiceJsonObj.optString("service_color").toLowerCase();
                                                invoiceModel.setVehicleDetails(vehicle_name);

                                            }else {
                                                invoiceModel.setVehicleDetails("");
                                            }
                                        }else {
                                            invoiceModel.setVehicleDetails("");
                                        }


                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }


                                    String status_case = jsonObjectTrip.optString("status");
                                    String status_case_val = status_case.substring(0,1).toUpperCase() + status_case.substring(1).toLowerCase();
                                    invoiceModel.setStatus(status_case_val);


                                    list.add(invoiceModel);

                                    System.out.println("Invoice model list size : "+list.size());

                                }

                            }

                            System.out.println("Invoice model list size 2 : "+list.size());
                            if(list.size() > 0){
                                TrackingLinkUsetActivityAdapter trackingLinkUsetActivityAdapter = new TrackingLinkUsetActivityAdapter(getApplicationContext(),list);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                                recyclerView.setAdapter(trackingLinkUsetActivityAdapter);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setNestedScrollingEnabled(false);
                            }else {
                                errorLayout.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }




                        }


                    } catch (JSONException e) {

                        displayMessage(e.toString());
                        errorLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                customDialog.dismiss();
                customDialog.cancel();
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




//        customDialog = new CustomDialog(InvoiceActivity.this);
//        customDialog.setCancelable(false);
//        customDialog.show();
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLHelper.GET_ALL_RIDES, response -> {
//
//            if (response != null) {
//                postAdapter = new InvoiceActivity.PostAdapter(response);
//                recyclerView.setHasFixedSize(true);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()) {
//                    @Override
//                    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
//                        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.WRAP_CONTENT);
//                    }
//                });
//                if (postAdapter != null && postAdapter.getItemCount() > 0) {
//                    errorLayout.setVisibility(View.GONE);
//                    recyclerView.setAdapter(postAdapter);
//                } else {
//                    errorLayout.setVisibility(View.VISIBLE);
//                }
//
//            } else {
//                errorLayout.setVisibility(View.VISIBLE);
//            }
//
//            customDialog.dismiss();
//
//        }, error -> {
//            customDialog.dismiss();
//            errorLayout.setVisibility(View.VISIBLE);
//            displayMessage(getString(R.string.something_went_wrong));
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("X-Requested-With", "XMLHttpRequest");
//                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
//                return headers;
//            }
//        };
//
//        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    public void GoToBeginActivity() {
        SharedHelper.putKey(this, "loggedIn", getString(R.string.False));
        Intent mainIntent = new Intent(this, SplashScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
    }

    private String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("M").format(cal.getTime());
        String name = getMonthName(Integer.parseInt(monthName));


        return name;
    }

    public  String getMonthName(int month)
    {
        switch(month)
        {
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


}