package com.travel.travellingbug.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.RestInterface;
import com.travel.travellingbug.models.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class PickUpNotes extends AppCompatActivity {

    Context context = PickUpNotes.this;

    ImageView back_icon;
    EditText etMsg;
    CardView btnSend;
    Button btnNotest;
    Call<ResponseBody> pickupNotesCall;
    RestInterface restInterface;
    String requestWith = "XMLHttpRequest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        setContentView(R.layout.activity_pick_up_notes);

        restInterface = ServiceGenerator.createService(RestInterface.class);
        back_icon = findViewById(R.id.imgBack);
        etMsg = findViewById(R.id.etMsg);
        btnSend = findViewById(R.id.btnSend);
        btnNotest = findViewById(R.id.btnNotest);
        btnNotest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendNotes(etMsg.getText().toString());
                String requestId = getIntent().getStringExtra("request_id");
                String user_id = getIntent().getStringExtra("user_id");
                verifyVerificationCode(requestId,user_id,etMsg.getText().toString());
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendNotes(etMsg.getText().toString());

                String requestId = getIntent().getStringExtra("request_id");
                String user_id = getIntent().getStringExtra("user_id");
                verifyVerificationCode(requestId,user_id,etMsg.getText().toString());
            }
        });


        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void sendNotes(String message) {
        CustomDialog customDialog = new CustomDialog(PickUpNotes.this);
        customDialog.show();
        String auth = "Bearer " + SharedHelper.getKey(context, "access_token");
        String requestId = getIntent().getStringExtra("request_id");

        pickupNotesCall = restInterface.addPickUpNotes(requestWith, auth, message, requestId);
        pickupNotesCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                customDialog.dismiss();
                if (response.code() == 200) {
                    finish();
                    Toast.makeText(PickUpNotes.this, "Pickup Notes sent to driver", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                customDialog.dismiss();
            }
        });



    }


    private void verifyVerificationCode(String rideId, String user_id, String code){

        CustomDialog customDialog = new CustomDialog(PickUpNotes.this);
        customDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.CHECK_VERIFICATION_CODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                customDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (response != null) {
                        System.out.println("data : "+jsonObject.toString());

                        if(jsonObject.optString("success").equalsIgnoreCase("Verification code Matched")){
                            Toast.makeText(PickUpNotes.this, "Verify Success", Toast.LENGTH_SHORT).show();
                            SharedHelper.putKey(PickUpNotes.this,"otp_success","yes");
                            finish();
                        }else if(jsonObject.optString("error").equalsIgnoreCase("Wrong Verification code")){
                            Toast.makeText(PickUpNotes.this, "Wrong Verification code", Toast.LENGTH_SHORT).show();
                            SharedHelper.putKey(PickUpNotes.this,"otp_success","no");
                            finish();
                        }

//                        Toast.makeText(PickUpNotes.this, "data : "+response, Toast.LENGTH_SHORT).show();
                        System.out.println("OTP STATUS :"+response.toString() );

                    }

                } catch (JSONException e) {
                    Toast.makeText(PickUpNotes.this, "error", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error Found", Toast.LENGTH_SHORT).show();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("rideid", rideId);
                params.put("user_id", user_id);
                params.put("code", code);
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


}