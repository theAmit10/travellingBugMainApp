package com.travel.travellingbug.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.PaymentDataModel;
import com.travel.travellingbug.ui.adapters.PaymentDataAdapter;

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

    PaymentDataAdapter paymentDataAdapter;

    RecyclerView paymentRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_and_refund);

        initData();
        getPaymentHistoryList();
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


    public void getPaymentHistoryList() {

        customDialog = new CustomDialog(this);
        customDialog.setCancelable(false);
        customDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLHelper.USER_PAYMENT_HISTORY, response -> {

            if (response != null) {
                if(response.length() > 0){
                    System.out.println("Payment data : "+response.toString());
                    System.out.println("Payment data length : "+response.length());

                    for(int i=0 ; i<response.length(); i++){
                        PaymentDataModel paymentDataModel = new PaymentDataModel();
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            JSONObject tripJsonObj  = jsonObject.optJSONObject("trip_detail");

                            if(tripJsonObj.optString("estimated_fare") != null){
                                paymentDataModel.setFare(tripJsonObj.optString("estimated_fare"));
                            }else {
                                paymentDataModel.setFare("0");
                            }


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


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        list.add(paymentDataModel);
                    }


                    paymentDataAdapter = new PaymentDataAdapter(getApplicationContext(), list);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
                    paymentRv.setAdapter(paymentDataAdapter);
                    paymentRv.setLayoutManager(linearLayoutManager);
                    paymentRv.setNestedScrollingEnabled(false);


                }else {
                    errorLayout.setVisibility(View.VISIBLE);
                }

            } else {
                errorLayout.setVisibility(View.VISIBLE);
            }

            customDialog.dismiss();

        }, error -> {
            customDialog.dismiss();
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        paymentRv.setLayoutManager(linearLayoutManager);
        paymentRv.setNestedScrollingEnabled(false);
    }
}