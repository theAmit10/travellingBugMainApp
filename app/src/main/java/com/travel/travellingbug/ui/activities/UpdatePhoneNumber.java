package com.travel.travellingbug.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.TwilioSMSHelper;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.helper.VolleyMultipartRequest;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class UpdatePhoneNumber extends AppCompatActivity implements View.OnClickListener {

    ImageView backArrow;
    TextView toolName;
    String parameter, value;
    EditText editText;
    TextInputLayout text_input_layout;
    Button btnUpdate, getOtpButton;
    Boolean isInternet;
    ConnectionHelper helper;
    private static final int PERMISSION_REQUEST_SEND_SMS = 1;

    public static final String ACCOUNT_SID = "ACfe83ce9330faf47eb0a246121dfb8fbf";
    public static final String AUTH_TOKEN = "11c78183aa4dddc0cc680e711398d7cd";

//    public static final String ACCOUNT_SID = "ACfe83ce9330faf47eb0a246121dfb8fbf";
////    public static final String AUTH_TOKEN = "11c78183aa4dddc0cc680e711398d7cd";
//    public static final String VERIFY_SERVICE_SID = "VA90d8d0ce62028644472152f6bf392b89";
//    public static final String VERIFIED_NUMBER = "+919564200516";

    EditText etName;
    CountryCodePicker ccp;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    ArrayList<String> checker = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone_number);

        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }


        toolName = findViewById(R.id.toolName);
        backArrow = findViewById(R.id.backArrow);
        editText = findViewById(R.id.editText);
        btnUpdate = findViewById(R.id.btnUpdate);
        getOtpButton = findViewById(R.id.getOtpButton);
        text_input_layout = findViewById(R.id.text_input_layout);

        ccp = findViewById(R.id.ccp);
        editText = findViewById(R.id.editText);
        etName = findViewById(R.id.etName);
        backArrow.setOnClickListener(this);

        helper = new ConnectionHelper(getApplicationContext());
        isInternet = helper.isConnectingToInternet();
        getIntentData();
        btnUpdate.setOnClickListener(this);


//        getOtpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


//        Twilio.init();




        getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etName.getText().toString().equals("") ||
                        etName.getText().toString().equalsIgnoreCase(getString(R.string.first_name))) {
                    displayMessage("Phone Number Required");
                } else if (isInternet) {

//                    dialog = new Dialog(SignUp.this, R.style.AppTheme_NoActionBar);
                    Toast.makeText(UpdatePhoneNumber.this, "Processing", Toast.LENGTH_SHORT).show();


                    String phone = ccp.getSelectedCountryCodeWithPlus() + etName.getText().toString();

//                    SharedHelper.putKey(getApplicationContext(), "mobile_number", phone);
//                    SharedHelper.putKey(getApplicationContext(), "mobile", phone);

//                    String phone = phoneID.substring(1, phoneID.length());


                    Log.v("Phonecode", phone + " ");
//                    registerAPI();
//                    Intent intent = new Intent(SignUp.this, OtpVerification.class);
//                    intent.putExtra("phonenumber", phone);
//                    startActivityForResult(intent, APP_REQUEST_CODE);


                    String phoneNumber = phone;  // Recipient's phone number



                    Random random = new Random();
                    String otp_code = String.format("%06d", random.nextInt(10000));

                    System.out.println("number : "+phoneNumber);
                    System.out.println("number code : "+otp_code);
//                    System.out.printf("OTP : " + otp_code);
                    String verificationCode = "112233";  // Verification code

//                    sendSMS(phoneNumber, verificationCode);

//                    String phoneNumber = "+1234567890";  // Recipient's phone number
//                    String verificationCode = generateVerificationCode();  // Generate the verification code

                    // Check if the SEND_SMS permission is already granted
                    if (ContextCompat.checkSelfPermission(UpdatePhoneNumber.this, Manifest.permission.SEND_SMS)
                            == PackageManager.PERMISSION_GRANTED) {
                        // Permission already granted, proceed with sending SMS
                        TwilioSMSHelper.sendVerificationCode(phoneNumber, verificationCode);
                    } else {
                        // Permission is not granted, request it from the user
                        ActivityCompat.requestPermissions(UpdatePhoneNumber.this, new String[]{Manifest.permission.SEND_SMS},
                                PERMISSION_REQUEST_SEND_SMS);
                        System.out.println("no permission");
                    }







                } else {
                    displayMessage(getString(R.string.something_went_wrong_net));
                }
            }


//                if(!input_mobile_number.getText().toString().trim().isEmpty()){
//                    if((input_mobile_number.getText().toString().trim()).length() == 10) {
////                        Intent intent = new Intent(EnterPhoneNumberScreen.this, VerifyOtpScreen.class);
//                        intent.putExtra("mobile", input_mobile_number.getText().toString());
//                        startActivity(intent);
//
//                        progressBar.setVisibility(View.VISIBLE);
//                        getOtpButton.setVisibility(View.INVISIBLE);

//                        otpSend();
//                        FirebaseAuth.getInstance().getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);

//                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                                "+91" + input_mobile_number.getText().toString(),
//                                60,
//                                TimeUnit.SECONDS,
//                                EnterPhoneNumberScreen.this,
//                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                                    @Override
//                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                        progressBar.setVisibility(View.GONE);
//                                        getOtpButton.setVisibility(View.VISIBLE);
//                                    }
//
//                                    @Override
//                                    public void onVerificationFailed(@NonNull FirebaseException e) {
//                                        progressBar.setVisibility(View.GONE);
//                                        getOtpButton.setVisibility(View.VISIBLE);
//                                        Toast.makeText(EnterPhoneNumberScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                        System.out.println("error " +e.getMessage());
//                                    }
//
//                                    @Override
//                                    public void onCodeSent(@NonNull String backendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                        progressBar.setVisibility(View.GONE);
//                                        getOtpButton.setVisibility(View.VISIBLE);
//                                        Intent intent = new Intent(EnterPhoneNumberScreen.this, VerifyOtpScreen.class);
//                                        intent.putExtra("mobile", input_mobile_number.getText().toString().trim());
//                                        intent.putExtra("backendOtp",backendOtp);
//                                        startActivity(intent);
//                                    }
//                                }
//                        );
//
//
//                    }else{
//                        Toast.makeText(EnterPhoneNumberScreen.this, "Please Enter correct Number ", Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(EnterPhoneNumberScreen.this, "Enter Mobile Number ", Toast.LENGTH_SHORT).show();
//                }
//            }


        });





    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with sending SMS
                TwilioSMSHelper.sendVerificationCode("+919564200516", "112233");
            } else {
                // Permission denied, handle the failure or inform the user
                // You may show a message or disable the SMS functionality
            }
        }
    }

    public void sendSMS(String toPhoneNumber, String verificationCode) {
//        Twilio.init("abcd","1234");

        System.out.println("id : "+ACCOUNT_SID);
        System.out.println("id : "+ AUTH_TOKEN);

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String fromPhoneNumber = "+919564200516";



        try {
//            Message message = Message.creator(
//                    new PhoneNumber(toPhoneNumber),
//                    new PhoneNumber(fromPhoneNumber),
//                    "Your verification code is: " + verificationCode
//            ).create();
//
//            // Handle the message creation result as needed
//            System.out.println("SEND MESSAGE : "+message.toString());
//            System.out.println(message.getSid());

            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber("+919564200516"),
                            new com.twilio.type.PhoneNumber("+18149046843"),
                            "Your message")
                    .create();

            System.out.println(message.getSid());
        } catch (final ApiException e) {
            e.printStackTrace();
            System.err.println(e);
        }




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
            text_input_layout.setHelperText("It is updated to the your account");
            editText.setHint(getString(R.string.enter_email));
//            text_input_layout.setHint(getString(R.string.enter_email));
            editText.setText(value);
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
        }
        if (v.getId() == R.id.btnUpdate) {
            if (editText.getText().toString().equals("")) {
                text_input_layout.setError("This field is not empty");
            } else if (editText.getText().toString().equals("Confirm your email")) {
                text_input_layout.setError("This field is not empty");
            } else if (editText.getText().toString().equals("Update Name")) {
                text_input_layout.setError("This field is not empty");
            } else if (editText.getText().toString().equals("Update Account Email")) {
                text_input_layout.setError("This field is not empty");
            } else if (editText.getText().toString().equals("Update Mobile No")) {
                text_input_layout.setError("This field is not empty");
            } else if (editText.getText().toString().equals("Update a Mini Bio")) {
                text_input_layout.setError("This field is not empty");
            } else if (editText.getText().toString().equals("Confirm your email")) {
                text_input_layout.setError("This field is not empty");
            } else if (editText.getText().toString().equals("Add a mini bio")) {
                text_input_layout.setError("This field is not empty");
            } else if (editText.getText().toString().equals("Enter Name")) {
                text_input_layout.setError("This field is not empty");
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


    private void updateProfileWithoutImage() {

        Dialog dialogCustom = new Dialog(this);
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
                Toast.makeText(this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
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