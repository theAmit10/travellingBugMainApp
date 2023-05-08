package com.travel.travellingbug.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.helper.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class VehicleDetailsLicensePlateNumberActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    TextInputEditText licenseNumberETL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details_license_plate_number);

        initComponent();
        clickHandlerOnComponent();

        licenseNumberETL.setText(SharedHelper.getKey(getApplicationContext(),"service_number"));

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
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehicleDetailsLicensePlateNumberActivity.this, VehicleDetailsBrandActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initComponent() {
        floatingActionButton = findViewById(R.id.floatingActionButton);
        licenseNumberETL = findViewById(R.id.licenseNumberETL);

    }
}