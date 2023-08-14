package com.travel.travellingbug.ui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.helper.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class VehicleDetailsLicensePlateNumberActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    TextInputEditText licenseNumberETL;
    private static final String LICENSE_NUMBER_REGEX = "^[A-Z]{3}\\d{3}$";

    private String vehicle_type = "private_car";

//    private static final String LICENSE_NUMBER_REGEX_ONE = "^[A-Z]{2}[ -]{0,1}[0-9]{2}[ -]{0,1}(?:[A-Z])[ -]{0,1}[A-Z]{1,2}[ -]{0,1}[0-9]{1,4}$";
//    private static final String LICENSE_NUMBER_REGEX_TWO = "^[A-Z]{2}[ -]{0,1}[0-9]{2}[ -]{0,1}[A-Z]{1,2}[ -]{0,1}[0-9]{1,4}$";
//    private static final String LICENSE_NUMBER_REGEX_THREE = "^[A-Z]{3}[ -]{0,1}[0-9]{1,4}$";
//    private static final String LICENSE_NUMBER_REGEX_FOUR = "^[A-Z]{2}[ -]{0,1}[0-9]{2}[ -]{0,1}[0-9]{1,4}$";



    private static final Pattern pattern = Pattern.compile(LICENSE_NUMBER_REGEX);
//    private static final Pattern pattern_one = Pattern.compile(LICENSE_NUMBER_REGEX_ONE);
//    private static final Pattern pattern_two = Pattern.compile(LICENSE_NUMBER_REGEX_TWO);
//    private static final Pattern pattern_three = Pattern.compile(LICENSE_NUMBER_REGEX_THREE);
//    private static final Pattern pattern_four = Pattern.compile(LICENSE_NUMBER_REGEX_FOUR);



    ImageView backArrow;

    CountryCodePicker ccp;

    String license_number = "";

    CheckBox privateCarCheckBox,texiCarCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details_license_plate_number);

        initComponent();
        clickHandlerOnComponent();

        licenseNumberETL.setText(SharedHelper.getKey(getApplicationContext(), "service_number"));
        int textLength = licenseNumberETL.getText().length();
        licenseNumberETL.setSelection(textLength);

    }

    public static boolean isValidLicenseNumber(String licenseNumber) {
        // Trim any leading or trailing whitespace
        String trimmedLicenseNumber = licenseNumber.trim();

        // Convert to uppercase (assuming license numbers are case-insensitive)
        String uppercasedLicenseNumber = trimmedLicenseNumber.toUpperCase();

        Matcher matcher = pattern.matcher(uppercasedLicenseNumber);
//         matcher = pattern_one.matcher(uppercasedLicenseNumber);
//         matcher = pattern_two.matcher(uppercasedLicenseNumber);
//         matcher = pattern_three.matcher(uppercasedLicenseNumber);
//         matcher = pattern_four.matcher(uppercasedLicenseNumber);

        return matcher.matches();
    }

    private void updateProfileWithoutImage() {
        Dialog dialogCustom = new Dialog(VehicleDetailsLicensePlateNumberActivity.this);
        dialogCustom.setContentView(R.layout.custom_dialog);
        dialogCustom.setCancelable(false);
        dialogCustom.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLHelper.USER_PROFILE_API, response -> {
            dialogCustom.dismiss();
            String res = new String(response.data);
            try {
                JSONObject jsonObject = new JSONObject(res);
                Log.e("update url : ", URLHelper.USER_PROFILE_API);
                Log.e("update url data : ", res.toString());
//                SharedHelper.putKey(getApplicationContext(), "id", jsonObject.optString("id"));
//                SharedHelper.putKey(getApplicationContext(), "first_name", jsonObject.optString("first_name"));
//                    SharedHelper.putKey(getApplicationContext(), "last_name", jsonObject.optString("last_name"));
                SharedHelper.putKey(getApplicationContext(), "service_number", jsonObject.optString("service_number"));
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
                Toast.makeText(VehicleDetailsLicensePlateNumberActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                //displayMessage(getString(R.string.update_success));

            } catch (JSONException e) {
                e.printStackTrace();
                displayMessage(getString(R.string.something_went_wrong));
            }


        }, error -> {
            if ((dialogCustom != null) && dialogCustom.isShowing())
                dialogCustom.dismiss();
            displayMessage(getString(R.string.something_went_wrong));
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", SharedHelper.getKey(getApplicationContext(), "first_name"));
                params.put("last_name", "");
                params.put("email", SharedHelper.getKey(getApplicationContext(), "email"));
                params.put("mobile", SharedHelper.getKey(getApplicationContext(), "mobile"));
                params.put("avatar", "");
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

    private void callSuccess() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", "result");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void displayMessage(String toastString) {
        Log.e("displayMessage", "" + toastString);
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    private void clickHandlerOnComponent() {

        privateCarCheckBox.setChecked(true);
        license_number = licenseNumberETL.getText().toString();

        licenseNumberETL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                license_number = licenseNumberETL.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                license_number = licenseNumberETL.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        privateCarCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(privateCarCheckBox.isChecked())
                {
                    texiCarCheckBox.setChecked(false);
                }
                vehicle_type = "private_car";
            }
        });

        texiCarCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(texiCarCheckBox.isChecked())
                {
                    privateCarCheckBox.setChecked(false);
                    vehicle_type = "taxi";
                }
                vehicle_type = "taxi";
            }
        });





        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(isValidLicenseNumber(license_number)){
//                    Toast.makeText(VehicleDetailsLicensePlateNumberActivity.this, "Enter Vehicle Plate Number", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(VehicleDetailsLicensePlateNumberActivity.this, "Processing ", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(VehicleDetailsLicensePlateNumberActivity.this, VehicleDetailsBrandActivity.class);
//                    intent.putExtra("license_number", license_number);
//                    startActivity(intent);
//                }

                if(license_number.equalsIgnoreCase("") && license_number.length() < 4){
                    Toast.makeText(VehicleDetailsLicensePlateNumberActivity.this, "Enter Vehicle Plate Number", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(VehicleDetailsLicensePlateNumberActivity.this, "Processing ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VehicleDetailsLicensePlateNumberActivity.this, VehicleDetailsBrandActivity.class);
                    intent.putExtra("license_number", license_number.trim());
                    SharedHelper.putKey(getApplicationContext(),"vehicle_type",vehicle_type);
                    startActivity(intent);
                }



            }
        });

//        String userInput = "ABC123";
//
//        if (isValidLicenseNumber(userInput)) {
//            System.out.println("License number is valid.");
//        } else {
//            System.out.println("Invalid license number. Please enter a valid license number.");
//        }


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        System.out.println("COUNTRY  : "+ccp.getSelectedCountryName());
    }

    private void initComponent() {
        floatingActionButton = findViewById(R.id.floatingActionButton);
        licenseNumberETL = findViewById(R.id.licenseNumberETL);
        backArrow = findViewById(R.id.backArrow);
        ccp = findViewById(R.id.ccp);
        privateCarCheckBox = findViewById(R.id.privateCarCheckBox);
        texiCarCheckBox = findViewById(R.id.texiCarCheckBox);

    }
}