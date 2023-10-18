package com.travel.travellingbug.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.dmoral.toasty.Toasty;

public class FindRidesActivity extends AppCompatActivity {

    Button details, request;

    int count = 0;


    String noofseat = "", request_id = "";

    CustomDialog customDialog;

    ImageView backArrow;

    TextView date,time;

    String sc_address = "", dc_address = "", s_profileImage = "", s_name = "", s_carModleAndColor = "", s_date = "", s_time = "", s_fare = "", s_seat = "", s_id = "";


    boolean scheduleTrip = false;

    RecyclerView recyclerView;

    private ShimmerFrameLayout mFrameLayout;

    LinearLayout errorLayout;

    TextView from, destination;
    UpcomingsAdapter upcomingsAdapter;

    ProgressBar idPBLoading;
    NestedScrollView nestedSv;
    String s_latitude = "", s_longitude = "", d_latitude = "", d_longitude = "", s_address = "", d_address = "", service_type = "", distance = "", schedule_date = "", schedule_time = "", upcoming = "", use_wallet = "", payment_mode = "";
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_rides);

        idPBLoading = findViewById(R.id.idPBLoading);
        nestedSv = findViewById(R.id.nestedSv);


        initData();
        getIntentData();
        setDataOnComponent();
        clickHandler();

        // Initialize the ExecutorService with a fixed number of threads
        int numberOfThreads = 3; // You can adjust this number as per your needs
        executorService = Executors.newFixedThreadPool(numberOfThreads);

        sendRequestToGetProvider();


    }


    private String getDateForTop(String date) throws ParseException {
        Date d = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String dateName = new SimpleDateFormat("dd").format(cal.getTime());
        return dateName;
    }

    private String getYearForTop(String date) throws ParseException {
        Date d = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String yearName = new SimpleDateFormat("yyyy").format(cal.getTime());
        return yearName;
    }

    private String getTimeForTop(String date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        Date d = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH).parse(date);
        cal.setTime(d);
        String timeName = new SimpleDateFormat("hh:mm a").format(cal.getTime());
        return timeName;
    }

    private String getMonthForTop(String date) throws ParseException {
        Date d = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("M").format(cal.getTime());
        String name = getMonthName(Integer.parseInt(monthName));


        return name;
    }

    private void setDataOnComponent() {
        from.setText(s_address);
        destination.setText(d_address);


        String form = schedule_date+" "  + schedule_time+":00";

        System.out.println("date data : "+form);


//        2023-05-19 17:48:51


        try {
            date.setText(getDateForTop(form) + "th " + getMonthForTop(form) + " , " +getYearForTop(form));
        } catch (ParseException e) {
            date.setText(schedule_date);
            e.printStackTrace();
        }

//        date.setText(schedule_date);

//        time.setText(schedule_time);

//        String formTime = schedule_time;

        try {
            time.setText(getTimeForTop(form));
        } catch (ParseException e) {
            time.setText(schedule_time);
            e.printStackTrace();
        }
    }

    private void clickHandler() {

//        details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), FindRideDetails.class));
//            }
//        });
//
//        request.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showConfirmDialog();
//            }
//        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindRidesActivity.this, HomeScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FindRidesActivity.this, HomeScreenActivity.class);
        startActivity(intent);
    }

    private void initData() {
//        details = findViewById(R.id.details);
//        request = findViewById(R.id.request);
        recyclerView = findViewById(R.id.findRideRequestRv);
        mFrameLayout = findViewById(R.id.shimmerLayout);
        from = findViewById(R.id.from);
        destination = findViewById(R.id.destination);
        errorLayout = findViewById(R.id.errorLayout);
        backArrow = findViewById(R.id.backArrow);

        time = findViewById(R.id.time);
        date = findViewById(R.id.date);

    }

    private void getIntentData() {

        s_latitude = getIntent().getStringExtra("s_latitude");
        s_longitude = getIntent().getStringExtra("s_longitude");
        d_latitude = getIntent().getStringExtra("d_latitude");
        d_longitude = getIntent().getStringExtra("d_longitude");
        s_address = getIntent().getStringExtra("s_address");
        d_address = getIntent().getStringExtra("d_address");
        service_type = getIntent().getStringExtra("service_type");
        distance = getIntent().getStringExtra("distance");
        schedule_date = getIntent().getStringExtra("schedule_date");
        schedule_time = getIntent().getStringExtra("schedule_time");
        upcoming = getIntent().getStringExtra("upcoming");
        use_wallet = getIntent().getStringExtra("use_wallet");
        payment_mode = getIntent().getStringExtra("payment_mode");
        s_seat = getIntent().getStringExtra("seat_count");
        noofseat = getIntent().getStringExtra("seat_count");


    }

    private void showConfirmDialog() {

//        AlertDialog alertDialog = new AlertDialog.Builder(FirstActivity.getInstance()).create();
        Dialog confirmDialog = new Dialog(this);
        confirmDialog.setContentView(R.layout.schedule_dialog);

        TextView tvDone = confirmDialog.findViewById(R.id.tvDone);
        TextView bookingStatusTitleTv = confirmDialog.findViewById(R.id.bookingStatusTitleTv);
        TextView bookingStatusSubTitleTv = confirmDialog.findViewById(R.id.bookingStatusSubTitleTv);
        TextView tvDriverMsg = confirmDialog.findViewById(R.id.tvDriverMsg);

        bookingStatusTitleTv.setText("Requested Successful");

        bookingStatusSubTitleTv.setText("Your ride has been Requested successfully ");

        tvDriverMsg.setText("");

        confirmDialog.show();
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    public void displayMessage(String toastString) {
        Toasty.info(getApplicationContext(), toastString, Toast.LENGTH_SHORT, true).show();
    }

    public void sendRequestToGetProvider() {


        customDialog = new CustomDialog(this);
        customDialog.setCancelable(false);
        customDialog.show();

        executorService.execute(() -> {
            // Perform background task here

            StringRequest request = new StringRequest(Request.Method.POST, URLHelper.SEND_REQUEST_API_PROVIDER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Update the UI using runOnUiThread() or Handler
                    runOnUiThread(() -> {
                        // Update UI elements here
                        customDialog.dismiss();
                        customDialog.cancel();

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            System.out.println("FIND RIDE RESPONSE : "+response);

                            if (response != null) {
                                System.out.println("FIND RIDE RESPONSE not null : "+response);
                                upcomingsAdapter = new UpcomingsAdapter(jsonArray);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                if (upcomingsAdapter != null && upcomingsAdapter.getItemCount() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    errorLayout.setVisibility(View.GONE);
                                    recyclerView.setAdapter(upcomingsAdapter);
                                    mFrameLayout.startShimmer();
                                    mFrameLayout.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                    mFrameLayout.startShimmer();
                                }
                            } else {
                                mFrameLayout.startShimmer();
                            }
                        } catch (JSONException e) {
                            displayMessage("No Driver Found");
                            e.printStackTrace();
                            errorLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            mFrameLayout.setVisibility(View.GONE);
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(FindRidesActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                    customDialog.dismiss();
                    customDialog.cancel();
                }

            }) {


                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("s_latitude", s_latitude);
                    params.put("s_longitude", s_longitude);
                    params.put("d_latitude", d_latitude);
                    params.put("d_longitude", d_longitude);
                    params.put("s_address", s_address);
                    params.put("d_address", d_address);
                    params.put("service_type", service_type);
                    params.put("distance", distance);
                    params.put("schedule_date", schedule_date);
                    params.put("schedule_time", schedule_time);
                    params.put("upcoming", upcoming);
                    params.put("use_wallet", use_wallet);
                    params.put("payment_mode", payment_mode);

                    System.out.println("REQUEST PARAMS : "+params.toString());

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


        });






    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(executorService != null)
        {
            executorService.shutdown();
        }
    }

    @Override
    protected void onResume() {
        mFrameLayout.startShimmer();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mFrameLayout.stopShimmer();
        super.onPause();
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



    private class UpcomingsAdapter extends RecyclerView.Adapter<FindRidesActivity.UpcomingsAdapter.MyViewHolder> {
        JSONArray jsonArray;

        public UpcomingsAdapter(JSONArray array) {
            this.jsonArray = array;
        }

        public void append(JSONArray array) {
            try {
                for (int i = 0; i < array.length(); i++) {
                    this.jsonArray.put(array.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public FindRidesActivity.UpcomingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.design_find_ride, parent, false);
            return new FindRidesActivity.UpcomingsAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FindRidesActivity.UpcomingsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
//            Picasso.get().load(jsonArray.optJSONObject(position).optString("static_map")).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.tripImg);
            try {

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        int totalItemCount = layoutManager.getItemCount();
                        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        System.out.println("COUNT -> totalItemCount : "+totalItemCount);
                        System.out.println("COUNT -> lastVisibleItem : "+lastVisibleItem);

                        // Check if the user has reached the end of the list
                        if (lastVisibleItem == totalItemCount - 1) {
                            // Fetch more data here (e.g., next 20 items) and add them to your data source.
                            // Then notify the adapter that new data has been added.
                            // recyclerViewAdapter.addData(newData);
                        }
                    }
                });




                if (!jsonArray.optJSONObject(position).optString("schedule_at", "").isEmpty()) {
                    String form = jsonArray.optJSONObject(position).optString("schedule_at");
                    try {
//                        holder.tripDate.setText(getDate(form) + "th " + getMonth(form) + " " + "at " + getTime(form));
                        holder.tripDate.setText(getTime(form));
//                        holder.tripId.setText(jsonArray.optJSONObject(position).optString("booking_id"));

//                        holder.listitemrating.setRating(Float.parseFloat("3.0"));


                        holder.txtSource.setText(jsonArray.optJSONObject(position).optString("s_address"));
                        holder.txtDestination.setText(jsonArray.optJSONObject(position).optString("d_address"));
                        holder.availableSeat.setText(jsonArray.optJSONObject(position).optString("availablecapacity")+" Seat left");




//                        holder.fare.setText("₹ "+jsonArray.optJSONObject(position).optString("estimated_fare"));


                        request_id = jsonArray.optJSONObject(position).optString("id");
//                        if (jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("PENDING")) {
//                            holder.status.setBackgroundResource(R.drawable.auth_btn_yellow_bg);
//                        }
//                        holder.status.setText(jsonArray.optJSONObject(position).optString("status"));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            // for fare details
            try {
                StringRequest request = new StringRequest(Request.Method.GET, URLHelper.ESTIMATED_FARE_AND_DISTANCE + "?s_latitude=" + jsonArray.optJSONObject(position).optString("s_latitude") + "&s_longitude=" + jsonArray.optJSONObject(position).optString("s_longitude") + "&d_latitude=" + jsonArray.optJSONObject(position).optString("d_latitude") + "&d_longitude=" + jsonArray.optJSONObject(position).optString("d_longitude") + "&service_type=2", new Response.Listener<String>() {
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


//                                txt04InvoiceId.setText(status.optString("booking_id"));
//                                txt04BasePrice.setText(con + jsonObject.optString("base_price"));
//                                txt04Distance.setText(jsonObject.optString("distance") + " KM");
//                                txt04Tax.setText(con + jsonObject.optString("tax_price"));
//                                txt04Total.setText(con + jsonObject.optString("estimated_fare"));
//                                txt04PaymentMode.setText("CASH");
//                                txt04Commision.setText(con + jsonObject.optString("discount"));
//                                txtTotal.setText(con + jsonObject.optString("estimated_fare"));
//                                paymentTypeImg.setImageResource(R.drawable.money1);
//                                btn_confirm_payment.setVisibility(View.VISIBLE);

                                System.out.println("ESTIMATED FARE STATUS :" + response.toString());

                                try {
                                    System.out.println("Fare : "+con + jsonObject.optString("estimated_fare"));
                                    holder.tripAmount.setText(con + jsonObject.optString("estimated_fare"));

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



            }catch (Exception e){
                e.printStackTrace();
            }







            try {
                JSONObject serviceObj = jsonArray.getJSONObject(position).optJSONObject("provider_service");
//                if (serviceObj != null) {
//
//                    String vehicle_name = serviceObj.optString("service_model")+ " " + serviceObj.optString("service_name") +" | "+serviceObj.optString("service_color").toLowerCase();
//                    holder.car_name.setText(vehicle_name);
//                }
//                else {
//                    holder.car_name.setText("");
//                }

                if(serviceObj != null ){
                    if(!serviceObj.optString("service_model").equalsIgnoreCase("null") && !serviceObj.optString("service_name").equalsIgnoreCase("null") && !serviceObj.optString("service_color").equalsIgnoreCase("null") ){
                        String vehicle_name = serviceObj.optString("service_model")+ " " + serviceObj.optString("service_name") +" | "+serviceObj.optString("service_color").toLowerCase();
                        System.out.println("vehicle name : "+vehicle_name);
                        holder.car_name.setText(vehicle_name.trim());
                        s_name = vehicle_name;
                    }else {
                        holder.car_name.setText("");
                    }
                }else {
                    holder.car_name.setText("");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }



            try {
                JSONObject serviceObj = jsonArray.getJSONObject(position).optJSONObject("provider");
                if (serviceObj != null) {
//                    holder.car_name.setText(serviceObj.optString("name"));
                    //holder.tripAmount.setText(SharedHelper.getKey(context, "currency")+serviceObj.optString("price"));


//                    URLHelper.BASE + "storage/app/public/" + response.optString("picture")

                    Picasso.get().load(URLHelper.BASE + "storage/app/public/" + serviceObj.optString("avatar"))
                            .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(holder.driver_image);

                    holder.profileNameTv.setText(serviceObj.optString("first_name"));

                   if(!serviceObj.optString("rating").equalsIgnoreCase("null")){
                       holder.listitemrating.setRating(Float.parseFloat(serviceObj.optString("rating")));
                       holder.reviewCount.setText("( "+serviceObj.optString("noofrating") +" Reviews )");
                   }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }




            // if the user had already made a request on that ride than they can't request for it again
            if(jsonArray.optJSONObject(position).optJSONArray("filters").length() > 0 ){
                JSONArray filterJsonArray = jsonArray.optJSONObject(position).optJSONArray("filters");

                for(int i=0 ;i<filterJsonArray.length(); i++){
                    JSONObject jsonObject = filterJsonArray.optJSONObject(i);
                    if(jsonObject.optString("user_id").equalsIgnoreCase(SharedHelper.getKey(getApplicationContext(),"id"))){
                        holder.request.setVisibility(View.GONE);
                    }

                }
            }




            holder.request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (!jsonArray.optJSONObject(position).optString("schedule_at", "").isEmpty()) {
                            String form = jsonArray.optJSONObject(position).optString("schedule_at");
                            try {
                                s_date = getDate(form) + "th " + getMonth(form) + " " + getYear(form);
                                s_time = getTime(form);

//                                holder.tripDate.setText(getDate(form) + "th " + getMonth(form) + " " + "at " + getTime(form));
//                                holder.listitemrating.setRating(Float.parseFloat("3.0"));
//                                holder.txtSource.setText(jsonArray.optJSONObject(position).optString("s_address"));
//                                holder.txtDestination.setText(jsonArray.optJSONObject(position).optString("d_address"));

                                sc_address = jsonArray.optJSONObject(position).optString("s_address");
                                dc_address = jsonArray.optJSONObject(position).optString("d_address");
                                s_id = jsonArray.optJSONObject(position).optString("id");


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject serviceObj = jsonArray.getJSONObject(position).optJSONObject("provider_service");

                        if(serviceObj != null ){
                            if(!serviceObj.optString("service_model").equalsIgnoreCase("null") && !serviceObj.optString("service_name").equalsIgnoreCase("null") && !serviceObj.optString("service_color").equalsIgnoreCase("null") ){
                                String vehicle_name = serviceObj.optString("service_model")+ " " + serviceObj.optString("service_name") +" | "+serviceObj.optString("service_color").toLowerCase();
                                System.out.println("vehicle name : "+vehicle_name);
                                holder.car_name.setText(vehicle_name.trim());
                                s_carModleAndColor = vehicle_name;
                            }else {
                                holder.car_name.setText("");
                            }
                        }else {
                            holder.car_name.setText("");
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }








                    try {
                        JSONObject serviceObj = jsonArray.getJSONObject(position).optJSONObject("provider");
                        if (serviceObj != null) {

//                            Picasso.get().load(URLHelper.BASE + "storage/app/public/" + serviceObj.optString("avatar"))
//                                    .placeholder(R.drawable.car_select).error(R.drawable.car_select).into(holder.driver_image);
//                            holder.profileNameTv.setText(serviceObj.optString("first_name"));

                            s_profileImage = URLHelper.BASE + "storage/app/public/" + serviceObj.optString("avatar");
                            s_name = serviceObj.optString("first_name");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Log.e("Intent", "" + jsonArray.optJSONObject(position).toString());




                    Intent intent = new Intent(getApplicationContext(), ConfirmRideRequestActivity.class);


                    intent.putExtra("s_address", jsonArray.optJSONObject(position).optString("s_address"));
                    intent.putExtra("d_address", dc_address);
                    intent.putExtra("s_profileImage", s_profileImage);
                    intent.putExtra("s_name", s_name);
                    intent.putExtra("s_carModleAndColor", s_carModleAndColor);
                    intent.putExtra("s_date", s_date);
                    intent.putExtra("s_time", s_time);
                    intent.putExtra("s_fare", "₹ "+jsonArray.optJSONObject(position).optString("estimated_fare"));
                    intent.putExtra("s_seat", s_seat);
                    intent.putExtra("s_id", s_id);

                    intent.putExtra("s_latitude", jsonArray.optJSONObject(position).optString("s_latitude"));
                    intent.putExtra("s_longitude", jsonArray.optJSONObject(position).optString("s_longitude"));
                    intent.putExtra("d_latitude", jsonArray.optJSONObject(position).optString("d_latitude"));
                    intent.putExtra("d_longitude", jsonArray.optJSONObject(position).optString("d_longitude"));


                    startActivity(intent);


                }
            });


            holder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FindRidesActivity.this, FindRideDetails.class);
                    intent.putExtra("noofseat", noofseat);
                    intent.putExtra("request_id", jsonArray.optJSONObject(position).optString("id"));
                    intent.putExtra("user_id",jsonArray.optJSONObject(position).optJSONObject("provider").optString("id"));
                    startActivity(intent);
                }
            });




            holder.containerLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(FindRidesActivity.this, DriverProfileActivity.class);
                    intent.putExtra("user_id",jsonArray.optJSONObject(position).optJSONObject("provider").optString("id"));
                    intent.putExtra("request_id",jsonArray.optJSONObject(position).optString("id"));
                    startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tripTime, car_name;

            TextView tripDate, tripAmount, tripId, txtSource, txtDestination, status;

            ImageView tripImg, driver_image;
            Button btnCancel, btnStart;


            ImageView profileImageIv;
            TextView profileNameTv, reviewCount, fare, availableSeat, fromTv, destinationTv, startTimeVal, carTypeVal;
            RatingBar listitemrating;
            Button details, request;

            LinearLayout containerLL;


            public MyViewHolder(View itemView) {
                super(itemView);
//                tripDate = itemView.findViewById(R.id.tripDate);
                tripTime = itemView.findViewById(R.id.tripTime);


//                tripAmount = itemView.findViewById(R.id.tripAmount);
//                tripImg = itemView.findViewById(R.id.tripImg);
//                car_name = itemView.findViewById(R.id.car_name);
//                driver_image = itemView.findViewById(R.id.driver_image);
                btnCancel = itemView.findViewById(R.id.btnCancel);
                btnStart = itemView.findViewById(R.id.btnStart);
//                tripId = itemView.findViewById(R.id.tripid);
//                txtDestination = itemView.findViewById(R.id.txtDestination);
//                txtSource = itemView.findViewById(R.id.txtSource);
//                status = itemView.findViewById(R.id.status);
//                listitemrating = itemView.findViewById(R.id.listitemrating);


                driver_image = itemView.findViewById(R.id.driver_image);
                profileNameTv = itemView.findViewById(R.id.profileNameTv);
                reviewCount = itemView.findViewById(R.id.reviewCount);
                tripAmount = itemView.findViewById(R.id.fare);
                availableSeat = itemView.findViewById(R.id.availableSeat);
                txtSource = itemView.findViewById(R.id.fromTv);
                txtDestination = itemView.findViewById(R.id.destinationTv);
                tripDate = itemView.findViewById(R.id.startTimeVal);
                car_name = itemView.findViewById(R.id.carTypeVal);
                listitemrating = itemView.findViewById(R.id.listitemrating);
                details = itemView.findViewById(R.id.details);
                request = itemView.findViewById(R.id.request);
                containerLL = itemView.findViewById(R.id.containerLL);

//                fare = itemView.findViewById(R.id.fare);



//                itemView.setOnClickListener(view -> {
//                    Intent intent = new Intent(getApplicationContext(), DriverProfileActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("user_id", jsonArray.optJSONObject(getAdapterPosition()).optString("user_id"));
//                    intent.putExtra("request_id", jsonArray.optJSONObject(getAdapterPosition()).optString("id"));
//                    startActivity(intent);
//
//                });
            }
        }
    }


}