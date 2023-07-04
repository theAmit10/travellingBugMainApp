package com.travel.travellingbug.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.ui.activities.HistoryDetailsUser;
import com.travel.travellingbug.ui.activities.SplashScreen;
import com.travel.travellingbug.ui.activities.TrackActivity;

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

import es.dmoral.toasty.Toasty;

public class PastTrips extends Fragment {
    Boolean isInternet;
    PostAdapter postAdapter;
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


    public PastTrips() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_past_trips, container, false);
        findViewByIdAndInitialize();

        if (isInternet) {
            getHistoryList();
        }

        backImg.setOnClickListener(v -> getFragmentManager().popBackStack());
        Bundle bundle = getArguments();
        String toolbar = null;
        if (bundle != null)
            toolbar = bundle.getString("toolbar");

        if (toolbar != null && toolbar.length() > 0) {
            this.toolbar.setVisibility(View.VISIBLE);
        }

        return rootView;
    }


    public void getHistoryList() {

        customDialog = new CustomDialog(getActivity());
        customDialog.setCancelable(false);
        customDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLHelper.GET_ALL_RIDES, response -> {

            if (response != null) {
                postAdapter = new PostAdapter(response);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()) {
                    @Override
                    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                });
                if (postAdapter != null && postAdapter.getItemCount() > 0) {
                    errorLayout.setVisibility(View.GONE);
                    recyclerView.setAdapter(postAdapter);
                } else {
                    errorLayout.setVisibility(View.VISIBLE);
                }

            } else {
                errorLayout.setVisibility(View.VISIBLE);
            }

            customDialog.dismiss();

        }, error -> {
            customDialog.dismiss();
            errorLayout.setVisibility(View.VISIBLE);
            displayMessage(getString(R.string.something_went_wrong));
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getActivity(), "access_token"));
                return headers;
            }
        };

        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public void findViewByIdAndInitialize() {
        recyclerView = rootView.findViewById(R.id.recyclerView);
        errorLayout = rootView.findViewById(R.id.errorLayout);
        errorLayout.setVisibility(View.GONE);
        helper = new ConnectionHelper(getActivity());
        isInternet = helper.isConnectingToInternet();
        toolbar = rootView.findViewById(R.id.lnrTitle);
        backImg = rootView.findViewById(R.id.backArrow);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void displayMessage(String toastString) {
        Toasty.info(getActivity(), toastString, Toast.LENGTH_SHORT, true).show();
    }

    public void GoToBeginActivity() {
        SharedHelper.putKey(getActivity(), "loggedIn", getString(R.string.False));
        Intent mainIntent = new Intent(getActivity(), SplashScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        getActivity().finish();
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

    private class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
        JSONArray jsonArray;

        public PostAdapter(JSONArray array) {
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
        public PostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.new_history_list_item, parent, false);
            return new MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(PostAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            try {

//                for (int i = 0; i < jsonArray.length(); i++) {
//                    System.out.println("History data : " + jsonArray.get(i));
//                }

//                JSONObject jsonObjectTrip = new JSONObject(String.valueOf(jsonArray.getJSONObject(position).getJSONObject("trip")));
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
                        System.out.println("j" + SharedHelper.getKey(getContext(), "id"));
                        if (filterJsonObject.optString("user_id").equalsIgnoreCase(SharedHelper.getKey(getContext(), "id"))) {
                            holder.availableSeat.setText(filterJsonObject.optString("noofseats")+" Seat");
                        }
                    }
                }




                            JSONObject jsonObjectServiceType = jsonObjectTrip.optJSONObject("service_type");
                holder.fare.setText("₹ "+jsonObjectServiceType.optString("fixed"));








                JSONObject providerServiceJsonObj = jsonObjectTrip.optJSONObject("provider_service");
                try {

                    if(!providerServiceJsonObj.optString("service_model").equalsIgnoreCase("null") && !providerServiceJsonObj.optString("service_name").equalsIgnoreCase("null") && !providerServiceJsonObj.optString("service_color").equalsIgnoreCase("null") ){
                        String vehicle_name = providerServiceJsonObj.optString("service_model")+ " " + providerServiceJsonObj.optString("service_name") +" | "+providerServiceJsonObj.optString("service_color").toLowerCase();
                        holder.carTypeVal.setText(vehicle_name);
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





            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.historyContainerLL.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(getContext(), TrackActivity.class);
                    intent.putExtra("flowValue", 3);
                    intent.putExtra("request_id_from_trip", jsonArray.optJSONObject(position).optString("request_id"));
                    startActivity(intent);

                    return true;
                }
            });


            holder.historyContainerLL.setOnClickListener(view -> {
//                Intent intent = new Intent(getActivity(), HistoryDetails.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                Log.e("Intent", "" + jsonArray.optJSONObject(position).toString());
//                intent.putExtra("post_value", jsonArray.optJSONObject(position).toString());
//                intent.putExtra("tag", "past_trips");
//                startActivity(intent);

//                Toast.makeText(getContext(), "user id: " + jsonArray.optJSONObject(position).optString("user_id"), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(), "id: " + jsonArray.optJSONObject(position).optString("id"), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), HistoryDetailsUser.class);
                intent.putExtra("request_id", jsonArray.optJSONObject(position).optString("request_id"));
                intent.putExtra("user_id" , jsonArray.optJSONObject(position).optString("user_id"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.e("Intent", "" + jsonArray.optJSONObject(position).toString());
                intent.putExtra("post_value", jsonArray.optJSONObject(position).toString());
                intent.putExtra("tag", "past_trips");

//                request_id = jsonArray.optJSONObject(position).optString("id");
//
//                s_address = jsonArray.optJSONObject(position).optString("s_address");
//
//                d_address = jsonArray.optJSONObject(position).optString("d_address");
//
//                booking_id = jsonArray.optJSONObject(position).optString("booking_id");
//
//                status = jsonArray.optJSONObject(position).optString("status");
//
//                if(jsonArray.optJSONObject(position).optString("payment_mode") != null){
//                    payment_mode = jsonArray.optJSONObject(position).optString("payment_mode");
//                }
//
//                if(jsonArray.optJSONObject(position).optString("estimated_fare") != null){
//                    estimated_fare = jsonArray.optJSONObject(position).optString("estimated_fare");
//
//                }
//
//                if(jsonArray.optJSONObject(position).optString("verification_code") != null){
//                    verification_code = jsonArray.optJSONObject(position).optString("verification_code");
//                }else{
//                    verification_code = "0000";
//                }
//
//                if(jsonArray.optJSONObject(position).optString("static_map") != null){
//                    static_map = jsonArray.optJSONObject(position).optString("static_map");
//                }
//
//
////                // getting firstname
////                try {
////                    JSONObject providerObj = jsonArray.getJSONObject(position).optJSONObject("provider");
////                    if (providerObj != null) {
////
////                        first_name  = providerObj.optString("first_name");
////                        rating  = providerObj.optString("rating");
////                        avatar = providerObj.optString("avatar");
////
////
////                    }
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
//
//
//                intent.putExtra("request_id", request_id);
//                intent.putExtra("s_address", s_address);
//                intent.putExtra("d_address", d_address);
//                intent.putExtra("booking_id", booking_id);
//                intent.putExtra("s_date", s_date);
//                intent.putExtra("s_time", s_time);
//                intent.putExtra("status", status);
//                intent.putExtra("payment_mode", payment_mode);
//                intent.putExtra("estimated_fare", estimated_fare);
//                intent.putExtra("verification_code", verification_code);
//                intent.putExtra("static_map", static_map);
//                intent.putExtra("first_name", userName);
//                intent.putExtra("rating", rating);
//                intent.putExtra("avatar", userProfileImage);

                startActivity(intent);

            });

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView datetime, txtSource, txtDestination, status, userName, ratingVal, availableSeat, fare, carTypeVal;

            RatingBar listitemrating;

            ImageView profileImgeIv;
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
                historyContainerLL = itemView.findViewById(R.id.historyContainerLL);

                carTypeVal = itemView.findViewById(R.id.carTypeVal);
                fare = itemView.findViewById(R.id.fare);
                availableSeat = itemView.findViewById(R.id.availableSeat);

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
