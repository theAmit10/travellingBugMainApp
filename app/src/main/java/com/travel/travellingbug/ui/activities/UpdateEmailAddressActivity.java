package com.travel.travellingbug.ui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.google.android.material.textfield.TextInputLayout;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.helper.VolleyMultipartRequest;
import com.travel.travellingbug.ui.activities.login.GoogleLoginActivity;
import com.travel.travellingbug.ui.activities.login.GoogleUpdateActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class UpdateEmailAddressActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView backArrow;
    TextView toolName;
    String parameter, value;
    TextView editText;
    TextInputLayout text_input_layout;
    Button btnUpdate,getGoogleAccount;
    Boolean isInternet;
    ConnectionHelper helper;

    LinearLayout emailContainerll;

    public static int APP_REQUEST_CODE = 99;

    ArrayList<String> checker = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        setContentView(R.layout.activity_update_email_address);
        toolName = findViewById(R.id.toolName);
        backArrow = findViewById(R.id.backArrow);
        editText = findViewById(R.id.editText);
        btnUpdate = findViewById(R.id.btnUpdate);
        getGoogleAccount = findViewById(R.id.getGoogleAccount);
        text_input_layout = findViewById(R.id.text_input_layout);
        emailContainerll = findViewById(R.id.emailContainerll);
        backArrow.setOnClickListener(this);

        helper = new ConnectionHelper(getApplicationContext());
        isInternet = helper.isConnectingToInternet();
        getIntentData();
        btnUpdate.setOnClickListener(this);

        getGoogleAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(UpdateEmailAddressActivity.this, GoogleUpdateActivity.class), APP_REQUEST_CODE);

            }
        });

        emailContainerll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateEmailAddressActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(UpdateEmailAddressActivity.this, GoogleUpdateActivity.class), APP_REQUEST_CODE);
            }
        });


    }

    private void getIntentData() {
        parameter = getIntent().getStringExtra("parameter");
        value = getIntent().getStringExtra("value");


        if (parameter.equalsIgnoreCase("first_name")) {

            toolName.setText(getString(R.string.update_name));
            text_input_layout.setHelperText("This name will be shown to the driver during ride pickup");

            editText.setHint("Enter Name");
//            text_input_layout.setHint("Enter  Name");
            editText.setText(value);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        }

        if (parameter.equalsIgnoreCase("email")) {

            toolName.setText(getString(R.string.update_email));
            editText.setHint("Add google account");
            text_input_layout.setHelperText("It is updated to the your account");
//            editText.setHint(getString(R.string.enter_email));
//            text_input_layout.setHint(getString(R.string.enter_email));
            if(value.equalsIgnoreCase("Confirm your email")){
                editText.setText("Add google account");
            }else {
                editText.setText(value);
            }
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }
        if (parameter.equalsIgnoreCase("mobile")) {

            toolName.setText(getString(R.string.update_mobile));
            editText.setHint("Enter Mobile No");
            text_input_layout.setHelperText("It is updated to the your account");

//            text_input_layout.setHint("Enter Mobile No");
            editText.setText(value);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }


        if (parameter.equalsIgnoreCase("bio")) {
            toolName.setText("Update a Mini Bio");
            editText.setHint("Enter Mini Bio");
            text_input_layout.setHelperText("It is updated to the your account");
//            text_input_layout.setHint("Enter Mini Bio");

            editText.setText(value);
            editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        }

    }

    @Override
    public void onClick(View v) {

        checker.add("");
        checker.add("");
        checker.add("");
        checker.add("");
        checker.add("");
        checker.add("");
        checker.add("");


        if (v.getId() == R.id.backArrow) {
            onBackPressed();
            finish();
            overridePendingTransition(R.anim.emoji_slide_down, R.anim.emoji_slide_up);
        }
        if (v.getId() == R.id.btnUpdate) {
            if (editText.getText().toString().equals("")) {
                text_input_layout.setError("This field not should be empty");
            }
            // Define your strong validation criteria
            else if (editText.toString().isEmpty()) {
                text_input_layout.setError("This field not should be empty");
                // Show an error message or toast indicating that the name cannot be empty.
            } else if (editText.toString().length() < 2 && parameter.equals("first_name")) {
                // Show an error message or toast indicating that the name must be at least 2 characters long.
                text_input_layout.setError("name must be at least 2 characters long");
            } else if (editText.getText().toString().equals("Confirm your email")) {
                text_input_layout.setError("This field not should be empty");
            } else if (editText.getText().toString().equals("Update Name")) {
                text_input_layout.setError("This field not should be empty");
            } else if (editText.getText().toString().equals("Update Account Email")) {
                text_input_layout.setError("This field not should be empty");
            } else if (editText.getText().toString().equals("Update Mobile No")) {
                text_input_layout.setError("This field not should be empty");
            } else if (editText.getText().toString().equals("Update a Mini Bio")) {
                text_input_layout.setError("This field is not empty");
            } else if (editText.getText().toString().equals("Confirm your email")) {
                text_input_layout.setError("This field not should be empty");
            } else if (editText.getText().toString().equals("Add a mini bio")) {
                text_input_layout.setError("This field not should be empty");
            } else if (editText.getText().toString().equals("Enter Name")) {
                text_input_layout.setError("This field not should be empty");
            } else if (editText.getText().toString().equals("Add account")) {
                text_input_layout.setError("This field not should be empty");
            } else if (editText.getText().toString().equals("Add google account")) {
                text_input_layout.setError("This field not should be empty");
            }
            else {
                if (isInternet) {
                    if (parameter.equals("first_name")) {
                        SharedHelper.putKey(getApplicationContext(), "first_name", editText.getText().toString());
                        updateProfileWithoutImage();
                    } else {
                        SharedHelper.putKey(getApplicationContext(), parameter, editText.getText().toString());
                        updateProfileWithoutImage();
                    }


                }
            }
        }

        if(v.getId() == R.id.editText) {
            startActivityForResult(new Intent(UpdateEmailAddressActivity.this, GoogleLoginActivity.class), APP_REQUEST_CODE);
        }
    }


    private void updateProfileWithoutImage() {

        Dialog dialogCustom = new Dialog(UpdateEmailAddressActivity.this);
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
                SharedHelper.putKey(getApplicationContext(), "id", jsonObject.optString("id"));
                SharedHelper.putKey(getApplicationContext(), "first_name", jsonObject.optString("first_name"));
//                    SharedHelper.putKey(getApplicationContext(), "last_name", jsonObject.optString("last_name"));
                SharedHelper.putKey(getApplicationContext(), "email", jsonObject.optString("email"));
                if (jsonObject.optString("avatar").equals("") || jsonObject.optString("avatar") == null) {
                    SharedHelper.putKey(getApplicationContext(), "picture", "");
                } else {
                    if (jsonObject.optString("avatar").startsWith("http"))
                        SharedHelper.putKey(getApplicationContext(), "picture", jsonObject.optString("avatar"));
                    else
                        SharedHelper.putKey(getApplicationContext(), "picture", URLHelper.BASE + "storage/app/public/" + jsonObject.optString("avatar"));
                }
                SharedHelper.putKey(getApplicationContext(), "sos", jsonObject.optString("sos"));
                SharedHelper.putKey(getApplicationContext(), "gender", jsonObject.optString("gender"));
                SharedHelper.putKey(getApplicationContext(), "mobile", jsonObject.optString("mobile"));
                SharedHelper.putKey(getApplicationContext(), "bio", jsonObject.optString("bio"));
//                        SharedHelper.putKey(context, "wallet_balance", jsonObject.optString("wallet_balance"));
//                        SharedHelper.putKey(context, "payment_mode", jsonObject.optString("payment_mode"));
                callSuccess();
                Toast.makeText(UpdateEmailAddressActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
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
                params.put("emailid", SharedHelper.getKey(getApplicationContext(), "email"));
                params.put("mobile", SharedHelper.getKey(getApplicationContext(), "mobile"));
                params.put("avatar", "");
                params.put("bio", SharedHelper.getKey(getApplicationContext(), "bio"));
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request

                data.getStringExtra("userName");
                data.getStringExtra("userMail");
                data.getStringExtra("userId");
                data.getStringExtra("userToken");

                if (data.getStringExtra("userMail") != null) {

                    String email = data.getStringExtra("userMail");
                    editText.setText(email);


                    if (editText.getText().toString().equals("")) {
                        text_input_layout.setError("This field not should be empty");
                    }
                    // Define your strong validation criteria
                    else if (editText.toString().isEmpty()) {
                        text_input_layout.setError("This field not should be empty");
                        // Show an error message or toast indicating that the name cannot be empty.
                    } else if (editText.toString().length() < 2 && parameter.equals("first_name")) {
                        // Show an error message or toast indicating that the name must be at least 2 characters long.
                        text_input_layout.setError("name must be at least 2 characters long");
                    } else if (editText.getText().toString().equals("Confirm your email")) {
                        text_input_layout.setError("This field not should be empty");
                    } else if (editText.getText().toString().equals("Update Name")) {
                        text_input_layout.setError("This field not should be empty");
                    } else if (editText.getText().toString().equals("Update Account Email")) {
                        text_input_layout.setError("This field not should be empty");
                    } else if (editText.getText().toString().equals("Update Mobile No")) {
                        text_input_layout.setError("This field not should be empty");
                    } else if (editText.getText().toString().equals("Update a Mini Bio")) {
                        text_input_layout.setError("This field is not empty");
                    } else if (editText.getText().toString().equals("Confirm your email")) {
                        text_input_layout.setError("This field not should be empty");
                    } else if (editText.getText().toString().equals("Add a mini bio")) {
                        text_input_layout.setError("This field not should be empty");
                    } else if (editText.getText().toString().equals("Enter Name")) {
                        text_input_layout.setError("This field not should be empty");
                    } else {
                        if (isInternet) {
                            if (parameter.equals("first_name")) {
                                SharedHelper.putKey(getApplicationContext(), "first_name", editText.getText().toString());
                                updateProfileWithoutImage();
                            } else {
                                SharedHelper.putKey(getApplicationContext(), parameter, editText.getText().toString());
                                updateProfileWithoutImage();
                            }


                        }
                    }



                }
            }
        }
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
}
