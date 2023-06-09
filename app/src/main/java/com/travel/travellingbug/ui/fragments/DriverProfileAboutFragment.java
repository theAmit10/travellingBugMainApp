package com.travel.travellingbug.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.chat.UserChatActivity;
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


public class DriverProfileAboutFragment extends Fragment {


    TextView dropLocation, sourceTv, startTimeVal, returnTimeVal, carTypeVal, vehicleCapacityVal, airConditionerVal, perSeatPerKmVal, vehicleName, vehicleNumber;
    ImageView vehicleImage;

    Button chatButton, continueButton;

    String user_id = "";
    String request_id = "";

    String providerFirstName = "";
    String providerId = "";
    String providerMobileNo = "";


    public DriverProfileAboutFragment() {
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
//        return inflater.inflate(R.layout.fragment_driver_profile_about, container, false);

        View view = inflater.inflate(R.layout.fragment_driver_profile_about, container, false);


        initData(view);
        user_id = getActivity().getIntent().getStringExtra("user_id");
        request_id = getActivity().getIntent().getStringExtra("request_id");
        if (getActivity().getIntent().getStringExtra("request_id") != null) {
            getProfileData(request_id);
        }


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(getContext(), "VAL : " + providerMobileNo, Toast.LENGTH_SHORT).show();
                System.out.println("val : " + providerMobileNo);




                if (providerMobileNo != null && !providerMobileNo.equalsIgnoreCase("null") && providerMobileNo.length() > 0) {
                    System.out.println("val if 1 : " + providerMobileNo);
                    try {
                        Intent intentCall = new Intent(Intent.ACTION_CALL);
                        intentCall.setData(Uri.parse("tel:" + providerMobileNo));
                        startActivity(intentCall);
                        System.out.println("val if 2 : " + providerMobileNo);

                    }catch (Exception e){
                        e.printStackTrace();
                    }


//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        System.out.println("val if 2 : " + providerMobileNo);
//                        getActivity().requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 2);
//                    } else {
//                        System.out.println("val else 1 : " + providerMobileNo);
//                        Intent intentCall = new Intent(Intent.ACTION_CALL);
//                        intentCall.setData(Uri.parse("tel:" + providerMobileNo));
//                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            System.out.println("val 2: " + providerMobileNo);
//                            return;
//                        }
//                        System.out.println("val 3 : " + providerMobileNo);
//                        getActivity().startActivity(intentCall);
//                        System.out.println("val 4: " + providerMobileNo);
//
//
//                    }
                } else {
                    System.out.println("val error : " + providerMobileNo);
                    Toast.makeText(getContext(), "User do not have a valid mobile number", Toast.LENGTH_SHORT).show();
                }


            }
        });


//        continueButton.setOnClickListener(v -> {
//            if (providerMobileNo != null && !providerMobileNo.equalsIgnoreCase("null") && providerMobileNo.length() > 0) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 2);
//                } else {
//                    Intent intentCall = new Intent(Intent.ACTION_CALL);
//                    intentCall.setData(Uri.parse("tel:" + providerMobileNo));
//                    if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return;
//                    }
//                    startActivity(intentCall);
//                }
//            } else {
////                displayMessage(getString(R.string.user_no_mobile));
//                Toast.makeText(getContext(), "User do not have a valid mobile number", Toast.LENGTH_SHORT).show();
//            }
//        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserChatActivity.class);
                intent.putExtra("requestId", request_id);
                intent.putExtra("providerId", providerId);
                intent.putExtra("userId", user_id);
                intent.putExtra("userName", providerFirstName);
                intent.putExtra("messageType", "pu");
                startActivity(intent);

            }
        });

        return view;
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

    private void getProfileData(String user_id) {

        // Getting other details of profile

        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.MY_PUBLISH_UPCOMMING_TRIPS_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("size : " + response.length());
                System.out.println("data : " + response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);

                    if (response != null) {
                        System.out.println("single trip Data : " + jsonObject.toString());

                        // setting data
                        sourceTv.setText(jsonObject.optString("s_address"));
                        dropLocation.setText(jsonObject.optString("d_address"));

                        String time = jsonObject.optString("schedule_at");

                        try {
                            startTimeVal.setText(getTime(time));
                        } catch (Exception e) {
                            startTimeVal.setText("");
                            e.printStackTrace();
                        }

                        try {
                            if (jsonObject.optString("returnschedule_at") != null) {
                                if (jsonObject.optString("returnschedule_at").equalsIgnoreCase("null")) {
                                    returnTimeVal.setText("No");
                                } else {

                                    String returnTime = jsonObject.optString("returnschedule_at");
                                    returnTimeVal.setText(getTime(returnTime));
                                }
                            }

                        } catch (Exception e) {
                            returnTimeVal.setText("No");
                            e.printStackTrace();
                        }


                        vehicleCapacityVal.setText(jsonObject.optString("availablecapacity") + " Seat left");


                        JSONObject providerJsonObj = jsonObject.optJSONObject("provider");
                        try {
                            providerFirstName = providerJsonObj.optString("first_name");
                            providerId = providerJsonObj.optString("id");
                            providerMobileNo = providerJsonObj.optString("mobile");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


//                        if (jsonObject.optString("email").equalsIgnoreCase("")) {
//                            emailTv.setText("No email Available");
//                        } else {
//                            emailTv.setText(jsonObject.optString("email"));
//                        }
//
//                        if (jsonObject.optString("mobile") != null) {
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
                params.put("request_id", request_id);
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

        dropLocation = view.findViewById(R.id.dropLocation);
        sourceTv = view.findViewById(R.id.sourceTv);

        startTimeVal = view.findViewById(R.id.startTimeVal);
        returnTimeVal = view.findViewById(R.id.returnTimeVal);
        carTypeVal = view.findViewById(R.id.carTypeVal);
        vehicleCapacityVal = view.findViewById(R.id.vehicleCapacityVal);
        airConditionerVal = view.findViewById(R.id.airConditionerVal);
        perSeatPerKmVal = view.findViewById(R.id.perSeatPerKmVal);
        vehicleName = view.findViewById(R.id.vehicleName);
        vehicleNumber = view.findViewById(R.id.vehicleNumber);
        vehicleImage = view.findViewById(R.id.vehicleImage);

        continueButton = view.findViewById(R.id.continueButton);
        chatButton = view.findViewById(R.id.chatButton);

    }
}