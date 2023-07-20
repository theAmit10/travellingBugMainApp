package com.travel.travellingbug.ui.activities;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.InvoiceModel;
import com.travel.travellingbug.ui.adapters.InvoiceAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class InvoiceActivity extends AppCompatActivity {

    // for generating pdf
    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;
    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;
    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    ArrayList<InvoiceModel> list = new ArrayList<>();

    Boolean isInternet;
    InvoiceActivity.PostAdapter postAdapter;
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
        setContentView(R.layout.activity_invoice);

        findViewByIdAndInitialize();

        if (isInternet) {
            getHistoryList();
        }

//        backImg.setOnClickListener(v -> getFragmentManager().popBackStack());

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (checkPermission()) {
//            Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            System.out.println("Permission Granted");
        } else {
            requestPermission();
        }




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

        customDialog = new CustomDialog(InvoiceActivity.this);
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


                                if(jsonObject.optJSONObject("trip").optString("status").equalsIgnoreCase("COMPLETED") || jsonObject.optJSONObject("trip").optString("provider_status").equalsIgnoreCase("COMPLETED")){

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
                                InvoiceAdapter invoiceAdapter = new InvoiceAdapter(getApplicationContext(),list);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                                recyclerView.setAdapter(invoiceAdapter);
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



    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(InvoiceActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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

    private class PostAdapter extends RecyclerView.Adapter<InvoiceActivity.PostAdapter.MyViewHolder> {
        JSONArray jsonArray;
        JSONArray jsonArrayParent;

        public PostAdapter(JSONArray array) {
            this.jsonArrayParent = array;
//            try {
//
//                for (int i = 0; i < jsonArrayParent.length(); i++) {
////                    jsonArray.getJSONObject(position).optJSONObject("trip").optString("status").equalsIgnoreCase("COMPLETED")
//                    if(array.getJSONObject(i).optJSONObject("trip").optString("status").equalsIgnoreCase("COMPLETED")){
////                        this.jsonArray = array;
//                        this.jsonArray.put(array.get(i));
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        }

        public void append(JSONArray array) {
            try {
                for (int i = 0; i < jsonArrayParent.length(); i++) {
////                    jsonArray.getJSONObject(position).optJSONObject("trip").optString("status").equalsIgnoreCase("COMPLETED")
//                    if(array.getJSONObject(i).optJSONObject("trip").optString("status").equalsIgnoreCase("COMPLETED")){
//                    }
                    this.jsonArray.put(array.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public InvoiceActivity.PostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.design_invoice, parent, false);
            return new InvoiceActivity.PostAdapter.MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(InvoiceActivity.PostAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            try {

                System.out.println("invoice adpater ");
                try {
                    System.out.println("invoice adpater jsonArrayParent.length() : "+jsonArrayParent.length());
                    for (int i = 0; i < jsonArrayParent.length(); i++) {
                        if(jsonArrayParent.getJSONObject(i).optJSONObject("trip").optString("status").equalsIgnoreCase("COMPLETED")){
                            this.jsonArray.put(jsonArrayParent.get(i));
                        }
                    }
                    System.out.println("invoice adpater jsonArray.length() : "+jsonArray.length());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                for (int i = 0; i < jsonArray.length(); i++) {
//                    System.out.println("History data : " + jsonArray.get(i));
//                }

//                JSONObject jsonObjectTrip = new JSONObject(String.valueOf(jsonArray.getJSONObject(position).getJSONObject("trip")));


//                if(jsonArray.getJSONObject(position).optJSONObject("trip").optString("status").equalsIgnoreCase("COMPLETED")) {
//
//                }

                JSONObject jsonObjectTrip = jsonArray.getJSONObject(position).optJSONObject("trip");

                holder.txtSource.setText(jsonObjectTrip.optString("s_address"));
                holder.txtDestination.setText(jsonObjectTrip.optString("d_address"));

                JSONObject providerJsonObject = jsonObjectTrip.getJSONObject("provider");
                try {
                    holder.userName.setText(providerJsonObject.optString("first_name"));
                    holder.ratingVal.setText("( " + providerJsonObject.optString("noofrating") + " Reviews )");

                    if(providerJsonObject.optString("noofrating").equalsIgnoreCase("0")){
                        holder.listitemrating.setRating(Float.parseFloat("0"));
                    }else {
                        holder.listitemrating.setRating(Float.parseFloat(providerJsonObject.optString("rating")));
                    }

                    Picasso.get().load(URLHelper.BASE + "storage/app/public/" + jsonArray.getJSONObject(position).optJSONObject("trip").optJSONObject("provider").optString("avatar"))
                            .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(holder.profileImgeIv);

                }catch (Exception e){
                    e.printStackTrace();
                }

                if (!jsonObjectTrip.optString("schedule_at", "").isEmpty()) {
                    String form = jsonObjectTrip.optString("schedule_at");
                    try {
                        holder.datetime.setText(getDate(form) + "th " + getMonth(form) + " at " + getTime(form));
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
                            holder.availableSeat.setText(filterJsonObject.optString("noofseats")+" Seat");
                        }
                    }
                }




                JSONObject jsonObjectServiceType = jsonObjectTrip.optJSONObject("service_type");
                holder.fare.setText("₹ "+jsonObjectServiceType.optString("fixed"));








                JSONObject providerServiceJsonObj = jsonObjectTrip.optJSONObject("provider_service");
                try {
                    if(providerServiceJsonObj != null){
                        if(!providerServiceJsonObj.optString("service_model").equalsIgnoreCase("null") && !providerServiceJsonObj.optString("service_name").equalsIgnoreCase("null") && !providerServiceJsonObj.optString("service_color").equalsIgnoreCase("null") ){
                            String vehicle_name = providerServiceJsonObj.optString("service_model")+ " " + providerServiceJsonObj.optString("service_name") +" | "+providerServiceJsonObj.optString("service_color").toLowerCase();
                            holder.carTypeVal.setText(vehicle_name);
                        }else {
                            holder.carTypeVal.setText("");
                        }
                    }else {
                        holder.carTypeVal.setText("");
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }



//                holder.txtSource.setText(jsonArray.optJSONObject(position).optString("s_address"));
//                holder.txtDestination.setText(jsonArray.optJSONObject(position).optString("d_address"));


//                userId = jsonObjectTrip.optString("user_id");

                // Getting other details of profile

//                StringRequest request = new StringRequest(Request.Method.POST, URLHelper.GET_DETAILS_OF_ONE_USER, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        System.out.println("size : " + response.length());
//                        System.out.println("data : " + response);
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            if (response != null) {
//                                System.out.println("data : " + jsonObject.toString());
//                                userName = jsonObject.optString("first_name");
//                                userProfileImage = jsonObject.optString("avatar");
//                                rating = jsonObject.optString("rating");
//                                ratingVal = jsonObject.optString("rating");
//
//
////                                userName = itemView.findViewById(R.id.userName);
////                                ratingVal = itemView.findViewById(R.id.ratingVal);
////                                listitemrating = itemView.findViewById(R.id.listitemrating);
////                                profileImgeIv = itemView.findViewById(R.id.profileImgeIv);
//
//                                holder.userName.setText(jsonObject.optString("first_name"));
//                                holder.ratingVal.setText("( " + jsonObject.optString("rating") + " Reviews )");
//                                holder.listitemrating.setRating(Float.parseFloat(jsonObject.optString("rating")));
//
//                                Picasso.get().load(URLHelper.BASE + "storage/app/public/" + jsonObject.optString("avatar"))
//                                        .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(holder.profileImgeIv);
//
//
//                            }
//
//
//                        } catch (JSONException e) {
//                            displayMessage(e.toString());
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getContext(), "Error Found", Toast.LENGTH_SHORT).show();
//                    }
//
//                }) {
//
//
//                    @Override
//                    public Map<String, String> getParams() {
//                        Map<String, String> params = new HashMap<>();
//                        params.put("id", providerJsonObject.optString("id"));
//                        return params;
//                    }
//
//                    @Override
//                    public Map<String, String> getHeaders() {
//                        HashMap<String, String> headers = new HashMap<String, String>();
//                        headers.put("X-Requested-With", "XMLHttpRequest");
//                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(getContext(), "access_token"));
//                        return headers;
//                    }
//
//                };
//
//                ClassLuxApp.getInstance().addToRequestQueue(request);


//                holder.status.setText(jsonArray.optJSONObject(position).optString("status"));

//                if (jsonArray.optJSONObject(position).optJSONObject("trip").optString("status").equalsIgnoreCase("PENDING")) {
//                    holder.status.setBackgroundResource(R.drawable.auth_btn_yellow_bg);
//                    holder.status.setText(jsonArray.optJSONObject(position).optJSONObject("trip").optString("status"));
//
//                } else if(jsonArray.optJSONObject(position).optJSONObject("trip").optString("status").equalsIgnoreCase("CANCELLED")){
//                    holder.status.setBackgroundResource(R.drawable.auth_btn_gray_bg);
//                    holder.status.setText(jsonArray.optJSONObject(position).optJSONObject("trip").optString("status"));
//                }else {
//                    holder.status.setText(jsonArray.optJSONObject(position).optJSONObject("trip").optString("status"));
//                }

                if (jsonObjectTrip.optString("status").equalsIgnoreCase("PENDING")) {
                    holder.status.setBackgroundResource(R.drawable.auth_btn_yellow_bg);
//                    holder.status.setText(jsonObjectTrip.optString("status").toLowerCase());
                    String status_case = jsonObjectTrip.optString("status");
                    String status_case_val = status_case.substring(0,1).toUpperCase() + status_case.substring(1).toLowerCase();

                    holder.status.setText(status_case_val);

                } else if(jsonObjectTrip.optString("status").equalsIgnoreCase("CANCELLED")){
                    holder.status.setBackgroundResource(R.drawable.auth_btn_gray_bg);
//                    holder.status.setText(jsonObjectTrip.optString("status"));

                    String status_case = jsonObjectTrip.optString("status");
                    String status_case_val = status_case.substring(0,1).toUpperCase() + status_case.substring(1).toLowerCase();
                    holder.status.setText(status_case_val);

                }else if(jsonObjectTrip.optString("status").equalsIgnoreCase("STARTED")){
                    holder.status.setBackgroundResource(R.drawable.auth_btn_purple_bg);
//                    holder.status.setText(jsonObjectTrip.optString("status").toLowerCase());
                    String status_case = jsonObjectTrip.optString("status");
                    String status_case_val = status_case.substring(0,1).toUpperCase() + status_case.substring(1).toLowerCase();

                    holder.status.setText(status_case_val);
                }else if(jsonObjectTrip.optString("status").equalsIgnoreCase("COMPLETED")){
                    holder.status.setBackgroundResource(R.drawable.auth_btn_green_bg);
//                    holder.status.setText(jsonObjectTrip.optString("status").toLowerCase());
                    String status_case = jsonObjectTrip.optString("status");
                    String status_case_val = status_case.substring(0,1).toUpperCase() + status_case.substring(1).toLowerCase();

                    holder.status.setText(status_case_val);
                }else {
//                    holder.status.setText(jsonObjectTrip.optString("status").toLowerCase());
                    holder.status.setBackgroundResource(R.drawable.auth_btn_blue_bgs);
                    String status_case = jsonObjectTrip.optString("status");
                    String status_case_val = status_case.substring(0,1).toUpperCase() + status_case.substring(1).toLowerCase();

                    holder.status.setText(status_case_val);
                }


                //            holder.historyContainerLL.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), TrackActivity.class);
//                    intent.putExtra("flowValue", 3);
//                    intent.putExtra("request_id_from_trip", jsonArray.optJSONObject(position).optString("request_id"));
//                    startActivity(intent);
//
//                    return true;
//                }
//            });

                holder.invoiceDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (checkPermission()) {
                            Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                        } else {
                            requestPermission();
                        }




                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo_org);
                        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);



                        StringRequest request = new StringRequest(Request.Method.GET, URLHelper.ESTIMATED_FARE_AND_DISTANCE + "?s_latitude=" + jsonArray.optJSONObject(position).optJSONObject("trip").optString("s_latitude") + "&s_longitude=" + jsonArray.optJSONObject(position).optJSONObject("trip").optString("s_longitude") + "&d_latitude=" + jsonArray.optJSONObject(position).optJSONObject("trip").optString("s_latitude") + "&d_longitude=" + jsonArray.optJSONObject(position).optJSONObject("trip").optString("d_longitude") + "&service_type=2", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    if (response != null) {
                                        System.out.println("payment details estimated data : " + jsonObject.toString());
                                        jsonObject.optString("estimated_fare");
                                        jsonObject.optString("distance");
                                        jsonObject.optString("time");
                                        jsonObject.optString("tax_price");
                                        jsonObject.optString("base_price");
                                        jsonObject.optString("discount");
                                        jsonObject.optString("currency");

                                        String con = jsonObject.optString("currency") + " ";


//                                    txt04InvoiceId.setText(jsonArray.optJSONObject(position).optString("booking_id"));
//                                    txt04BasePrice.setText(con + jsonObject.optString("base_price"));
//                                    txt04Distance.setText(jsonObject.optString("distance") + " KM");
//                                    txt04Tax.setText(con + jsonObject.optString("tax_price"));
//                                    txt04Total.setText(con + jsonObject.optString("estimated_fare"));
//                                    txt04PaymentMode.setText("CASH");
//                                    txt04Commision.setText(con + jsonObject.optString("discount"));
//                                    txtTotal.setText(con + jsonObject.optString("estimated_fare"));
//                                    paymentTypeImg.setImageResource(R.drawable.money1);
//                                    btn_confirm_payment.setVisibility(View.VISIBLE);

                                        System.out.println("ESTIMATED FARE STATUS :" + response.toString());



                                        // creating an object variable
                                        // for our PDF document.
                                        PdfDocument pdfDocument = new PdfDocument();

                                        // two variables for paint "paint" is used
                                        // for drawing shapes and we will use "title"
                                        // for adding text in our PDF file.
                                        Paint paint = new Paint();
                                        Paint title = new Paint();

                                        // we are adding page info to our PDF file
                                        // in which we will be passing our pageWidth,
                                        // pageHeight and number of pages and after that
                                        // we are calling it to create our PDF.
                                        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

                                        // below line is used for setting
                                        // start page for our PDF file.
                                        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

                                        // creating a variable for canvas
                                        // from our page of PDF.
                                        Canvas canvas = myPage.getCanvas();

                                        // below line is used to draw our image on our PDF file.
                                        // the first parameter of our drawbitmap method is
                                        // our bitmap
                                        // second parameter is position from left
                                        // third parameter is position from top and last
                                        // one is our variable for paint.
                                        canvas.drawBitmap(scaledbmp, 56, 40, paint);

                                        // below line is used for adding typeface for
                                        // our text which we will be adding in our PDF file.
                                        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

                                        // below line is used for setting text size
                                        // which we will be displaying in our PDF file.
                                        title.setTextSize(15);

                                        // below line is sued for setting color
                                        // of our text inside our PDF file.
                                        title.setColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

                                        // below line is used to draw text in our PDF file.
                                        // the first parameter is our text, second parameter
                                        // is position from start, third parameter is position from top
                                        // and then we are passing our variable of paint which is title.
                                        canvas.drawText("Travelling Bug", 209, 80, title);
                                        canvas.drawText("", 209, 100, title);
                                        canvas.drawText("We believe that travel is not just a hobby, it's a way of life.", 209, 120, title);
                                        canvas.drawText("Our mission is to provide exceptional  travel experiences ", 209, 140, title);
                                        canvas.drawText("that inspire and enrich our clients'lives.", 209, 160, title);
                                        canvas.drawText("With our expertise and passion for travel,", 209, 180, title);
                                        canvas.drawText("we make your dream vacation a reality.", 209, 200, title);


                                        // similarly we are creating another text and in this
                                        // we are aligning this text to center of our PDF file.
                                        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                        title.setColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                                        title.setTextSize(24);


                                        // below line is used for setting
                                        // our text to center of PDF.
                                        title.setTextAlign(Paint.Align.CENTER);

                                        canvas.drawText("--------------------------------------------", 396, 540, title);
                                        canvas.drawText("INVOICE", 396, 560, title);
                                        canvas.drawText("--------------------------------------------", 396, 600, title);
                                        canvas.drawText("Booking ID             "+jsonArray.optJSONObject(position).optJSONObject("trip").optString("booking_id"), 396, 630, title);
                                        canvas.drawText("Base fare              "+con + jsonObject.optString("base_price"), 396, 670, title);
                                        canvas.drawText("Distance               "+jsonObject.optString("distance") + " KM", 396, 710, title);
                                        canvas.drawText("Tax                    "+con + jsonObject.optString("tax_price"), 396, 750, title);
                                        canvas.drawText("--------------------------------------------", 396, 790, title);
                                        canvas.drawText("Total                  "+con + jsonObject.optString("estimated_fare"), 396, 830, title);
                                        canvas.drawText("--------------------------------------------", 396, 870, title);

                                        // after adding all attributes to our
                                        // PDF file we will be finishing our page.
                                        pdfDocument.finishPage(myPage);

                                        // below line is used to set the name of
                                        // our PDF file and its path.
                                        File file = new File(Environment.getExternalStorageDirectory(), "TravellingBug"+jsonArray.optJSONObject(position).optJSONObject("trip").optString("booking_id")+".pdf");

                                        try {
                                            // after creating a file name we will
                                            // write our PDF file to that location.
                                            pdfDocument.writeTo(new FileOutputStream(file));

                                            // below line is to print toast message
                                            // on completion of PDF generation.
                                            Toast.makeText(getApplicationContext(), "PDF file saved successfully.", Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            // below line is used
                                            // to handle error
                                            e.printStackTrace();
                                        }
                                        // after storing our pdf to that
                                        // location we are closing our PDF file.
                                        pdfDocument.close();



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


                    }
                });

//
//            holder.historyContainerLL.setOnClickListener(view -> {
////                Intent intent = new Intent(getActivity(), HistoryDetails.class);
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                Log.e("Intent", "" + jsonArray.optJSONObject(position).toString());
////                intent.putExtra("post_value", jsonArray.optJSONObject(position).toString());
////                intent.putExtra("tag", "past_trips");
////                startActivity(intent);
//
////                Toast.makeText(getContext(), "user id: " + jsonArray.optJSONObject(position).optString("user_id"), Toast.LENGTH_SHORT).show();
////                Toast.makeText(getContext(), "id: " + jsonArray.optJSONObject(position).optString("id"), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), HistoryDetailsUser.class);
//                intent.putExtra("request_id", jsonArray.optJSONObject(position).optString("request_id"));
//                intent.putExtra("user_id" , jsonArray.optJSONObject(position).optString("user_id"));
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                Log.e("Intent", "" + jsonArray.optJSONObject(position).toString());
//                intent.putExtra("post_value", jsonArray.optJSONObject(position).toString());
//                intent.putExtra("tag", "past_trips");
//
////                request_id = jsonArray.optJSONObject(position).optString("id");
////
////                s_address = jsonArray.optJSONObject(position).optString("s_address");
////
////                d_address = jsonArray.optJSONObject(position).optString("d_address");
////
////                booking_id = jsonArray.optJSONObject(position).optString("booking_id");
////
////                status = jsonArray.optJSONObject(position).optString("status");
////
////                if(jsonArray.optJSONObject(position).optString("payment_mode") != null){
////                    payment_mode = jsonArray.optJSONObject(position).optString("payment_mode");
////                }
////
////                if(jsonArray.optJSONObject(position).optString("estimated_fare") != null){
////                    estimated_fare = jsonArray.optJSONObject(position).optString("estimated_fare");
////
////                }
////
////                if(jsonArray.optJSONObject(position).optString("verification_code") != null){
////                    verification_code = jsonArray.optJSONObject(position).optString("verification_code");
////                }else{
////                    verification_code = "0000";
////                }
////
////                if(jsonArray.optJSONObject(position).optString("static_map") != null){
////                    static_map = jsonArray.optJSONObject(position).optString("static_map");
////                }
////
////
//////                // getting firstname
//////                try {
//////                    JSONObject providerObj = jsonArray.getJSONObject(position).optJSONObject("provider");
//////                    if (providerObj != null) {
//////
//////                        first_name  = providerObj.optString("first_name");
//////                        rating  = providerObj.optString("rating");
//////                        avatar = providerObj.optString("avatar");
//////
//////
//////                    }
//////                } catch (JSONException e) {
//////                    e.printStackTrace();
//////                }
////
////
////                intent.putExtra("request_id", request_id);
////                intent.putExtra("s_address", s_address);
////                intent.putExtra("d_address", d_address);
////                intent.putExtra("booking_id", booking_id);
////                intent.putExtra("s_date", s_date);
////                intent.putExtra("s_time", s_time);
////                intent.putExtra("status", status);
////                intent.putExtra("payment_mode", payment_mode);
////                intent.putExtra("estimated_fare", estimated_fare);
////                intent.putExtra("verification_code", verification_code);
////                intent.putExtra("static_map", static_map);
////                intent.putExtra("first_name", userName);
////                intent.putExtra("rating", rating);
////                intent.putExtra("avatar", userProfileImage);
//
//                startActivity(intent);
//
//            });




            } catch (Exception e) {
                e.printStackTrace();
            }



        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView datetime, txtSource, txtDestination, status, userName, ratingVal, availableSeat, fare, carTypeVal;

            RatingBar listitemrating;

            ImageView profileImgeIv,invoiceDownload;
            Button rateRider;

            LinearLayout historyContainerLL;

            public MyViewHolder(View itemView) {
                super(itemView);

                datetime = itemView.findViewById(R.id.datetime);
                txtSource = itemView.findViewById(R.id.txtSource);
                txtDestination = itemView.findViewById(R.id.txtDestination);
                status = itemView.findViewById(R.id.status);
                rateRider = itemView.findViewById(R.id.rateRider);
                userName = itemView.findViewById(R.id.userName);
                ratingVal = itemView.findViewById(R.id.ratingVal);
                listitemrating = itemView.findViewById(R.id.listitemrating);
                profileImgeIv = itemView.findViewById(R.id.profileImgeIv);
//                historyContainerLL = itemView.findViewById(R.id.historyContainerLL);

                carTypeVal = itemView.findViewById(R.id.carTypeVal);
                fare = itemView.findViewById(R.id.fare);
                availableSeat = itemView.findViewById(R.id.availableSeat);
                invoiceDownload = itemView.findViewById(R.id.invoiceDownload);

//                itemView.setOnClickListener(view -> {
//                    Intent intent = new Intent(getActivity(), HistoryDetails.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    Log.e("Intent", "" + jsonArray.optJSONObject(getAdapterPosition()).toString());
//                    intent.putExtra("post_value", jsonArray.optJSONObject(getAdapterPosition()).toString());
//                    intent.putExtra("tag", "past_trips");
//                    startActivity(intent);
//                });

            }
        }
    }



}