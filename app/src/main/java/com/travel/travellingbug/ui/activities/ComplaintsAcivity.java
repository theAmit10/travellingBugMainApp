package com.travel.travellingbug.ui.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.utills.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ComplaintsAcivity extends AppCompatActivity {

    private static final String[] HEAR_OPTIONS = {
            "Trips and Fare",
            "Payment",
            "App Usability",
            "Account",
            "Others"
    };

    ImageView ivBack;
    TextView tvSubmit;
    MaterialSpinner spRegister;
    EditText etComplaint;
    String complaintName = "Driver Late";

    Utilities utils = new Utilities();
    CustomDialog customDialog;
    Boolean isInternet;
    ConnectionHelper helper;

    String title = "";
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }


        setContentView(R.layout.activity_complaints);
        helper = new ConnectionHelper(ComplaintsAcivity.this);
        isInternet = helper.isConnectingToInternet();
        ivBack = findViewById(R.id.ivBack);
        tvSubmit = findViewById(R.id.tvSubmit);
        spRegister = findViewById(R.id.spRegister);
        etComplaint = findViewById(R.id.etComplaint);


        title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");


        ivBack.setOnClickListener(view -> onBackPressed());


//        spRegister.setItems(HEAR_OPTIONS);
        spRegister.setText(title);

//        spRegister.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>)
//                (view, position, id, item) -> {
//                    complaintName = item;
//                    Log.d("complaintName", complaintName);
//                });
//        Log.d("complaintName1", complaintName);

//        spRegister.setOnNothingSelectedListener(spinner -> {
//
//        });

        spRegister.setClickable(false);
        spRegister.setBackgroundResource(R.drawable.auth_btn_gray_bg);

        tvSubmit.setOnClickListener(view -> {
            if (etComplaint.getText().toString().equals("")) {
                displayMessage(getString(R.string.complaint_not_empty));
            } else {
                submitComplaint();
            }
        });
    }

    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    private void submitComplaint() {
        if (isInternet) {
            customDialog = new CustomDialog(ComplaintsAcivity.this);
            if (customDialog != null)
                customDialog.show();

//            String issue = etComplaint.getText().toString();
//            String issueType = complaintName;
//
//
//            JSONObject object = new JSONObject();
//            try {
////                object.put("user_id", id);
////                object.put("description", issue);
////                object.put("complaint_type", issueType);
//
//                object.put("title", id);
//                object.put("subject", issueType);
//                object.put("message", issue);
//                utils.print("input to complaint", "" + object);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST,
//                    URLHelper.USER_COMPLAINTS, object, response -> {
//                if ((customDialog != null) && customDialog.isShowing())
//                    customDialog.dismiss();
//                utils.print("ComplaintResponse", response.toString());
//                displayMessage(getString(R.string.complaint_submitted));
//                onBackPressed();
//
//            }, error -> {
//
//            }) {
//                @Override
//                public Map<String, String> getHeaders() {
//                    HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("X-Requested-With", "XMLHttpRequest");
//                    return headers;
//                }
//            };
//
//            ClassLuxApp.getInstance().addToRequestQueue(objectRequest);
//
//

            // Getting User details
            StringRequest request = new StringRequest(Request.Method.POST, URLHelper.USER_COMPLAINTS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    System.out.println("Sending User Complaint data");

                    try {
                        JSONObject jsonObjectUser = new JSONObject(response);

                        if (response != null) {
                            System.out.println("data : " + jsonObjectUser.toString());
                            if ((customDialog != null) && customDialog.isShowing())
                                customDialog.dismiss();
                            utils.print("ComplaintResponse", response.toString());
                            showConfirmDialog();


                        }


                    } catch (JSONException e) {
                        if ((customDialog != null) && customDialog.isShowing())
                            customDialog.dismiss();
                        displayMessage("Something went wrong");
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
//                                Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                        if ((customDialog != null) && customDialog.isShowing())
                            customDialog.dismiss();
                        error.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }) {
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("title", id);
                    params.put("subject", title);
                    params.put("message", etComplaint.getText().toString().trim());
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    try {
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return headers;
                }

            };

            ClassLuxApp.getInstance().addToRequestQueue(request);











        }else {
            Toast.makeText(this, "Please, check your internet connection ", Toast.LENGTH_SHORT).show();
        }







    }

    private void showConfirmDialog() {
        Dialog confirmDialog = new Dialog(this);
        confirmDialog.setContentView(R.layout.schedule_dialog);

        TextView tvDone = confirmDialog.findViewById(R.id.tvDone);
        TextView bookingStatusTitleTv = confirmDialog.findViewById(R.id.bookingStatusTitleTv);
        TextView bookingStatusSubTitleTv = confirmDialog.findViewById(R.id.bookingStatusSubTitleTv);
        TextView tvDriverMsg = confirmDialog.findViewById(R.id.tvDriverMsg);

        bookingStatusTitleTv.setText("Complaint Successful");

        bookingStatusSubTitleTv.setText("Your complaint has been sent successfully ");

        tvDriverMsg.setText("");

        confirmDialog.show();
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
