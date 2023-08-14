package com.travel.travellingbug.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.helper.VolleyMultipartRequest;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class UpdatePreference extends AppCompatActivity {

    ImageView backArrow;
    TextView toolName;
    String parameter, value;
    String title, subtitle, id, title_id, update;
    EditText editTexttitle, editTextsubtitle;
    TextInputLayout text_input_layout_title, text_input_layout_subtitle;
    Button btnUpdate;
    Boolean isInternet;
    ConnectionHelper helper;

    Button btnAllowed,btnNotAllowed ;

    private String preferenceStatus = "";
    private String TAG = "UPDATEpREFERENCES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_preference);

        toolName = findViewById(R.id.toolName);
        backArrow = findViewById(R.id.backArrow);
        editTexttitle = findViewById(R.id.editTexttitle);
        editTextsubtitle = findViewById(R.id.editTextsubtitle);
        btnUpdate = findViewById(R.id.btnUpdate);
        text_input_layout_title = findViewById(R.id.text_input_layout_title);
        text_input_layout_subtitle = findViewById(R.id.text_input_layout_subtitle);
        btnNotAllowed = findViewById(R.id.btnNotAllowed);
        btnAllowed = findViewById(R.id.btnAllowed);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        btnAllowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferenceStatus.equalsIgnoreCase("") || preferenceStatus.equalsIgnoreCase("1")){
                    preferenceStatus = "0";
                    btnAllowed.setBackgroundResource(R.drawable.auth_btn_gray_bg);
                    btnNotAllowed.setBackgroundResource(R.drawable.auth_btn_red_bg);
                }
                else {
                    preferenceStatus = "";
                    btnAllowed.setBackgroundResource(R.drawable.auth_btn_green_bg);
                }

                Log.d(TAG, "onClick: status "+preferenceStatus);
            }
        });

        btnNotAllowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferenceStatus.equalsIgnoreCase("") || preferenceStatus.equalsIgnoreCase("0")){
                    preferenceStatus = "1";
                    btnNotAllowed.setBackgroundResource(R.drawable.auth_btn_gray_bg);
                    btnAllowed.setBackgroundResource(R.drawable.auth_btn_green_bg);

                }
                else {
                    preferenceStatus = "";
                    btnNotAllowed.setBackgroundResource(R.drawable.auth_btn_red_bg);
                }

                Log.d(TAG, "onClick: status "+preferenceStatus);
            }
        });

        helper = new ConnectionHelper(getApplicationContext());
        isInternet = helper.isConnectingToInternet();

        getIntentData();


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternet) {

//                    Toast.makeText(UpdatePreference.this, "UPDATE : "+update, Toast.LENGTH_SHORT).show();

                    if (update.equalsIgnoreCase("no")) {
                        if (!preferenceStatus.equalsIgnoreCase("")) {
                            addPreferences();
                        } else {
                            Toast.makeText(UpdatePreference.this, "Select Preference", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        if (!preferenceStatus.equalsIgnoreCase("")) {
                            updatePreferences();
//                            addPreferences();

                        } else {
                            Toast.makeText(UpdatePreference.this, "Select Preference", Toast.LENGTH_SHORT).show();
                        }


                    }


                }
            }


        });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void updatePreferences() {
        Dialog dialogCustom = new Dialog(UpdatePreference.this);
        dialogCustom.setContentView(R.layout.custom_dialog);
        dialogCustom.setCancelable(false);
        dialogCustom.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLHelper.PREFERENCES, response -> {
            dialogCustom.dismiss();
            String res = new String(response.data);
            //                JSONObject jsonObject = new JSONObject(res);
//                Log.e("update url : ", URLHelper.USER_PROFILE_API);
            Log.e("update preferences : ", res.toString());
//                SharedHelper.putKey(getApplicationContext(), "id", jsonObject.optString("id"));
//                SharedHelper.putKey(getApplicationContext(), "first_name", jsonObject.optString("first_name"));
////                    SharedHelper.putKey(getApplicationContext(), "last_name", jsonObject.optString("last_name"));
//                SharedHelper.putKey(getApplicationContext(), "email", jsonObject.optString("email"));
//                if (jsonObject.optString("avatar").equals("") || jsonObject.optString("avatar") == null) {
//                    SharedHelper.putKey(getApplicationContext(), "picture", "");
//                } else {
//                    if (jsonObject.optString("avatar").startsWith("http"))
//                        SharedHelper.putKey(getApplicationContext(), "picture", jsonObject.optString("avatar"));
//                    else
//                        SharedHelper.putKey(getApplicationContext(), "picture", URLHelper.BASE + "storage/app/public/" + jsonObject.optString("avatar"));
//                }
//                SharedHelper.putKey(getApplicationContext(), "sos", jsonObject.optString("sos"));
//                SharedHelper.putKey(getApplicationContext(), "gender", jsonObject.optString("gender"));
//                SharedHelper.putKey(getApplicationContext(), "mobile", jsonObject.optString("mobile"));
//                        SharedHelper.putKey(context, "wallet_balance", jsonObject.optString("wallet_balance"));
//                        SharedHelper.putKey(context, "payment_mode", jsonObject.optString("payment_mode"));
            callSuccess();
            Toast.makeText(UpdatePreference.this, "Preferences Updated", Toast.LENGTH_SHORT).show();

            //displayMessage(getString(R.string.update_success));


        }, error -> {
            if ((dialogCustom != null) && dialogCustom.isShowing())
                dialogCustom.dismiss();
            System.out.println("error : " + error.toString());
            System.out.println("error : " + error.getCause());
            System.out.println("error : " + error.getMessage());
            System.out.println("error : " + error.getStackTrace());
            displayMessage(getString(R.string.something_went_wrong));
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", title_id);
                params.put("subtitle", preferenceStatus);
                params.put("id", id);
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
        ClassLuxApp.getInstance().addToRequestQueue(volleyMultipartRequest);
    }

    private void addPreferences() {
        Dialog dialogCustom = new Dialog(UpdatePreference.this);
        dialogCustom.setContentView(R.layout.custom_dialog);
        dialogCustom.setCancelable(false);
        dialogCustom.show();
//        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLHelper.PREFERENCES, response -> {
//            dialogCustom.dismiss();
//            String res = new String(response.data);
//
//            Log.e("Added preferences : ", res.toString());
//
//            callSuccess();
//            Toast.makeText(UpdatePreference.this, "Preferences Added", Toast.LENGTH_SHORT).show();
//
//
//        }, error -> {
//            if ((dialogCustom != null) && dialogCustom.isShowing())
//                dialogCustom.dismiss();
//            error.printStackTrace();
//            displayMessage(getString(R.string.something_went_wrong));
//        }) {
//            @Override
//            public Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("title", id);
//                params.put("subtitle", preferenceStatus);
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("X-Requested-With", "XMLHttpRequest");
//                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
//                return headers;
//            }
//        };
//        ClassLuxApp.getInstance().addToRequestQueue(volleyMultipartRequest);



//        // Getting other details of profile

        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.PREFERENCES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialogCustom.dismiss();
                String res = new String(response.toString());

                Log.e("Added preferences : ", res.toString());

                callSuccess();
                Toast.makeText(UpdatePreference.this, "Preferences Added", Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ((dialogCustom != null) && dialogCustom.isShowing())
                    dialogCustom.dismiss();
                error.printStackTrace();
                displayMessage(getString(R.string.something_went_wrong));
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", id);
                params.put("subtitle", preferenceStatus);
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

    private void callSuccess() {
        Intent returnIntent = new Intent(getApplicationContext(), TravelPreferenceActivityMain.class);
        startActivity(returnIntent);
    }

    public void displayMessage(String toastString) {
        Log.e("displayMessage", "" + toastString);
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    private void getIntentData() {
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        subtitle = getIntent().getStringExtra("subtitle");
        title_id = getIntent().getStringExtra("title_id");
        update = getIntent().getStringExtra("update");

        editTexttitle.setText(title);
        editTextsubtitle.setText(subtitle);

        System.out.println(" preferecnce id" + id);
        System.out.println(" preferecnce title" + title);
        System.out.println(" preferecnce subtitle" + subtitle);
        System.out.println(" preferecnce update" + update);

//        if (parameter.equalsIgnoreCase("first_name")) {
//
//            toolName.setText(getString(R.string.update_name));
//            text_input_layout.setHelperText("This name will be shown to the driver during ride pickup");
//            editText.setHint("Name");
//            text_input_layout.setHint("Enter  Name");
//            editText.setText(value);
//            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//        }
//
//        if (parameter.equalsIgnoreCase("email")) {
//
//            toolName.setText(getString(R.string.update_email));
//            text_input_layout.setHelperText("It is updated to the your account");
//            editText.setHint(getString(R.string.email));
//            text_input_layout.setHint(getString(R.string.enter_email));
//            editText.setText(value);
//            editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//        }
//        if (parameter.equalsIgnoreCase("mobile")) {
//
//            toolName.setText(getString(R.string.update_mobile));
//            editText.setHint("Mobile No");
//            text_input_layout.setHint("Enter Mobile No");
//            editText.setText(value);
//            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//        }

    }


}